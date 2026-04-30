package com.gen_ai.gemini_demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.gen_ai.gemini_demo.dto.LogAnalysis;


@Service
public class LogAnalyzerService {
	
  private final ChatClient chatClient;
  private static final Path LOG_PATH = Paths.get("logs/application.log");

  public LogAnalyzerService(ChatClient.Builder builder) {
      this.chatClient = builder.build();
  }

  public LogAnalysis analyzeAllLogs() throws Exception {
      if (!Files.exists(LOG_PATH)) {
          return new LogAnalysis("No logs found.", "N/A", "N/A", "N/A", "", "Run app first.");
      }

      List<String> lines = Files.readAllLines(LOG_PATH);
      String context = lines.stream()
              .skip(Math.max(0, lines.size() - 200))
              .collect(Collectors.joining("\n"));

      return chatClient.prompt()
              .user(u -> u.text("""
              You are an expert Java Debugger. Analyze the following logs.
              1. Provide a 'generalSummary' of normal app activity.
              2. If an error exists, identify 'errorType', 'rootCauseAnalysis', and 'fileLocation'.
              3. Provide 'suggestedFixCode' and a 'stepByStepPlan'.
              Return as structured JSON.

              LOGS:
              """ + context))
              .call()
              .entity(LogAnalysis.class);
  }
}

