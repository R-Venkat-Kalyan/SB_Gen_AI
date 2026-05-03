package com.gen_ai.gemini_demo.controller;

import com.gen_ai.gemini_demo.config.WebLogAppender;
import com.gen_ai.gemini_demo.dto.LogAnalysis;
import com.gen_ai.gemini_demo.dto.ProjectIntelligence;
import com.gen_ai.gemini_demo.dto.ProjectStructure;
import com.gen_ai.gemini_demo.service.ProjectIntelligenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;

//  REST Controller for AI-driven project analysis and real-time log streaming.
@RestController
@RequiredArgsConstructor
public class IntelligenceApiController {
    private final ProjectIntelligenceService aiService;
    private final WebLogAppender logAppender;

    // Streams live logs to the /logs page
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream() {
        return logAppender.getLogStream();
    }

    // Analyzes the physical log file using AI
    @GetMapping("/api/run-analysis")
    public LogAnalysis analyzeLogs() throws Exception {
        return aiService.analyzeProjectLogs();
    }

    // Performs a technical audit of the project structure
    @GetMapping("/api/code-scan")
    public ProjectStructure scanProject() throws IOException {
        return aiService.performCodebaseScan();
    }

    // Fetches the 3-Persona (User/Dev/Arch) JSON report
    @GetMapping("/api/data")
    public ProjectIntelligence getData() throws IOException {
        return aiService.getFullIntelligence();
    }

    // Handles the Chat Terminal interactions
//    @PostMapping("/ask")
//    public String askQuestion(@RequestBody String userQuestion) throws IOException {
//        return aiService.chatWithContext(userQuestion);
//    }

    @PostMapping(value = "/ask", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> askQuestion(@RequestBody String userQuestion) {
        try {
            String result = aiService.chatWithContext(userQuestion);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Controller Error: " + e.getMessage());
        }
    }
}
