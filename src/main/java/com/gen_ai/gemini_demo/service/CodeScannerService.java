package com.gen_ai.gemini_demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.gen_ai.gemini_demo.dto.ProjectStructure;

@Service
public class CodeScannerService {
	
	private final ChatClient chatClient;
    // Base path for your Java files
    private final Path projectRoot = Paths.get("src/main");

    public CodeScannerService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ProjectStructure analyzeCodebase() throws IOException {
        String codebaseContext = readAllSourceFiles();

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
                {code}
                """).param("code", codebaseContext))
            .call()
            .entity(ProjectStructure.class);
    }

    private String readAllSourceFiles() throws IOException {
        try (Stream<Path> paths = Files.walk(projectRoot)) {
            return paths
                .filter(Files::isRegularFile)
                // SECURITY: Explicitly skip sensitive files
                .filter(path -> {
                    String name = path.getFileName().toString().toLowerCase();
                    return !name.endsWith(".properties") && 
                           !name.endsWith(".yml") && 
                           !name.endsWith(".env") &&
                           !name.contains("secret");
                })
                .map(path -> {
                    try {
                        return "--- FILE: " + path.toString() + " ---\n" + Files.readString(path);
                    } catch (IOException e) { return ""; }
                })
                .collect(Collectors.joining("\n\n"));
        }
    }

}
