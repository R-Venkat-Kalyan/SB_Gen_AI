package com.gen_ai.gemini_demo.service;

import com.gen_ai.gemini_demo.dto.LogAnalysis;
import com.gen_ai.gemini_demo.dto.ProjectIntelligence;
import com.gen_ai.gemini_demo.dto.ProjectStructure;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectIntelligenceService {

    private final ChatClient chatClient;
    private final Path projectRoot = Paths.get("src/main");
    private final Path logPath = Paths.get("logs/application.log");

    private String cachedRawCodebase;
    private ProjectIntelligence cachedIq;

    public ProjectIntelligenceService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    //Works locally Only
//    public String getRawCodebase() throws IOException {
//        if (this.cachedRawCodebase != null) return this.cachedRawCodebase;
//        try (Stream<Path> paths = Files.walk(projectRoot)) {
//            this.cachedRawCodebase = paths.filter(Files::isRegularFile)
//                    .filter(p -> (p.toString().endsWith(".java") || p.toString().endsWith(".html"))
//                            && !p.toString().contains("properties"))
//                    .map(p -> {
//                        try { return "\n--- FILE: " + p + " ---\n" + Files.readString(p); }
//                        catch (IOException e) { return ""; }
//                    }).collect(Collectors.joining("\n"));
//        }
//        return this.cachedRawCodebase;
//    }

    public String getRawCodebase() {
        // Log if we are skipping the scan because of the cache
        if (this.cachedRawCodebase != null) {
            System.out.println("DEBUG: Skipping scan. Codebase is already cached (Size: " + this.cachedRawCodebase.length() + ")");
            return this.cachedRawCodebase;
        }

        StringBuilder sb = new StringBuilder();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            System.out.println("********** INITIALIZING AI SCAN **********");

            // Use your specific package structure to avoid the 30k library files
            Resource[] projectResources = resolver.getResources("classpath*:com/gen_ai/gemini_demo/**/*.*");
            Resource[] rootResources = resolver.getResources("classpath*:pom.xml");
            Resource[] htmlResources = resolver.getResources("classpath*:templates/**/*.html");

            int totalFound = projectResources.length + rootResources.length + htmlResources.length;
            System.out.println("DEBUG: Resources found on classpath: " + totalFound);

            processList(projectResources, sb);
            processList(rootResources, sb);
            processList(htmlResources, sb);

            this.cachedRawCodebase = sb.toString();

            System.out.println("DEBUG: Scan Complete. Final Count: " + this.cachedRawCodebase.length() + " chars.");
            System.out.println("********** SCAN FINISHED **********");

        } catch (IOException e) {
            System.err.println("CRITICAL SCAN ERROR: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
        return this.cachedRawCodebase;
    }

    private void processList(Resource[] resources, StringBuilder sb) throws IOException {
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename == null || filename.endsWith(".class")) continue;

            // Still allow only the files you want
            if (filename.endsWith(".java") || filename.endsWith(".html") || filename.equals("pom.xml")) {
                String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                sb.append("\n--- FILE: ").append(resource.getDescription()).append(" ---\n");
                sb.append(content);
            }
        }
    }

    /**
     * Requirement 1: Persona-based analysis using the expert trio prompt.
     */
    public ProjectIntelligence getFullIntelligence() throws IOException {
        if (cachedIq != null) return cachedIq;
        String codebase = getRawCodebase();

        return this.cachedIq = chatClient.prompt()
                .user(u -> u.text("""
                        Act as a trio of Software Industry Experts (Product Manager, Lead Developer, and System Architect).
                        Analyze the provided Spring Boot Maven codebase and generate a high-fidelity JSON report.
                        
                        1. USER VIEW (Product Manager):
                           - Identify only user-facing features.
                           - For each feature, explain the "Journey": Which URL to hit, what UI elements (buttons/forms) to look for, and what the result is.
                           - Speak in plain English. Ignore `@Service` or `@Repository` logic.
                           - If the app is a backend utility with no UI, state "Technical Utility: Limited End-User Interface".
                        
                        2. DEVELOPER VIEW (Lead Dev):
                           - Provide a "Knowledge Transfer" (KT) overview.
                           - Map every Controller route to its Service method and its Business Purpose.
                           - Explain internal flows (e.g., "Triggers SMTP for notifications", "Persistence via JPA").
                           - DO NOT expose secrets/credentials, but explain HOW they are configured (e.g., "@Value annotation").
                        
                        3. ARCHITECT VIEW (System Architect):
                           - Perform a "Standard Compliance Audit".
                           - Evaluate against: SOLID, DRY, KISS, and YAGNI.
                           - Identify "Logic Bugs" (e.g., missing null checks, inefficient loops).
                           - Identify "Bad UI Standards" (e.g., lack of responsive tags, hardcoded styles in HTML).
                           - Provide exact 'badCode' snippets from the input and a detailed 'fix' refactor.
                        
                        JSON STRUCTURE: Match the ProjectIntelligence record hierarchy exactly.
                        
                        CODEBASE:
                        """ + codebase))
                .call().entity(ProjectIntelligence.class);
    }

    /**
     * Requirement 2: Senior Architect code scan.
     */
    public ProjectStructure performCodebaseScan() throws IOException {
        String code = getRawCodebase();
        return chatClient.prompt()
                .user(u -> u.text("""
                        You are a Senior Architect. I have provided the source code of my Spring Boot project.
                        
                        TASKS:
                        1. User Summary: Explain the app's purpose to a non-technical person.
                        2. Technical Overview: Explain the stack and architecture to a developer.
                        3. Data Flow: Detail how data moves between layers.
                        4. DB Check: If you do NOT find any Repository interfaces or Database configurations, 
                           explicitly set 'missingComponents' to "No database communication found".
                        5. API Mapping: List every @RequestMapping found in the controllers.
                        6. List all API routes, their methods, and how to use them.
                        7. Describe the data flow (Controller -> Service -> Repo).
                        
                        CODEBASE:
                        """ + code))
                .call().entity(ProjectStructure.class);
    }

    /**
     * Requirement 3: Chat terminal with project context.
     */
    public String chatWithContext(String userQuestion) {
        try {
            // 1. Get the codebase
            String codebase = getRawCodebase();

            // 2. Log basic stats for AWS debugging
            System.out.println("DEBUG: Request received. Codebase size: " + (codebase != null ? codebase.length() : 0) + " chars.");
            System.out.println("DEBUG: User Question: " + userQuestion);

            // 3. Execute the AI call within the try-catch
            System.out.println("DEBUG: Sending request to Gemini API...");

            String response = chatClient.prompt()
                    .system("You are 'Project-IQ', an expert AI assistant specialized in this specific Spring Boot project. " +
                            "Use the provided codebase to answer any technical or functional questions accurately. " +
                            "If asked about logic, point to the specific file. Codebase: " + codebase)
                    .user(userQuestion)
                    .call()
                    .content();

            System.out.println("DEBUG: Received response from Gemini successfully.");
            return response;

        } catch (Exception e) {
            // This will print the EXACT error (401, 404, Connection Timeout) in web.stdout.log
            System.err.println("AI ERROR DETECTED: " + e.getMessage());
            e.printStackTrace(); // This sends the full stack trace to your AWS logs
            return "AI Communication Error: " + e.getMessage();
        }
    }

    /**
     * Requirement 4: Java Debugger log analysis.
     */
    public LogAnalysis analyzeProjectLogs() throws Exception {
        if (!Files.exists(logPath)) return new LogAnalysis("No logs found.", "N/A", "N/A", "N/A", "", "");
        List<String> lines = Files.readAllLines(logPath);
        String context = lines.stream().skip(Math.max(0, lines.size() - 200)).collect(Collectors.joining("\n"));

        return chatClient.prompt()
                .user(u -> u.text("""
                        You are an expert Java Debugger. Analyze the following logs.
                        1. Provide a 'generalSummary' of normal app activity.
                        2. If an error exists, identify 'errorType', 'rootCauseAnalysis', and 'fileLocation'.
                        3. Provide 'suggestedFixCode' and a 'stepByStepPlan'.
                        Return as structured JSON.
                        
                        LOGS:
                        """ + context))
                .call().entity(LogAnalysis.class);
    }

}