package com.gen_ai.gemini_demo.service;

import com.gen_ai.gemini_demo.dto.ProjectIntelligence;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectIntelligenceService {

    private final ChatClient chatClient;
    private ProjectIntelligence cachedIq; // Simple cache to prevent redundant AI calls

    public ProjectIntelligenceService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ProjectIntelligence getFullIntelligence() throws IOException {
        if (cachedIq != null) return cachedIq;

        String codebase;
        try (Stream<Path> paths = Files.walk(Paths.get("src/main"))) {
            codebase = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java") || p.toString().endsWith(".html"))
                    .filter(p -> !p.toString().contains("properties") && !p.toString().contains("env"))
                    .map(p -> {
                        try {
                            return "\n--- FILE: " + p + " ---\n" + Files.readString(p);
                        } catch (IOException e) { return ""; }
                    }).collect(Collectors.joining("\n"));
        }

        this.cachedIq = chatClient.prompt()
                .user(u -> u.text("""
                        Act as a trio of Software Industry Experts (Product Manager, Lead Developer, and System Architect).
                        Analyze the provided Spring Boot Maven codebase and generate a high-fidelity JSON report.
                
                        1. USER VIEW (Product Manager):
                           - Identify only user-facing features.
                           - For each feature, explain the "Journey": Which URL to hit, what UI elements (buttons/forms) to look for, and what the result is.
                           - Speak in plain English. Ignore `@Service` or `@Repository` logic.s
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
                .call()
                .entity(ProjectIntelligence.class);

        return cachedIq;
    }

    public String readCodebaseForChat() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get("src/main"))) {
            return paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java") || p.toString().endsWith(".html"))
                    .filter(p -> !p.toString().contains("properties"))
                    .map(p -> {
                        try { return "\nFILE: " + p + "\n" + Files.readString(p); }
                        catch (IOException e) { return ""; }
                    }).collect(Collectors.joining("\n"));
        }
    }
}
