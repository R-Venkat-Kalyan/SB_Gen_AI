package com.gen_ai.gemini_demo.controller;

import java.io.IOException;

import com.gen_ai.gemini_demo.dto.ProjectIntelligence;
import com.gen_ai.gemini_demo.service.ProjectIntelligenceService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gen_ai.gemini_demo.config.WebLogAppender;
import com.gen_ai.gemini_demo.dto.LogAnalysis;
import com.gen_ai.gemini_demo.dto.ProjectStructure;
import com.gen_ai.gemini_demo.service.CodeScannerService;
import com.gen_ai.gemini_demo.service.LogAnalyzerService;

import reactor.core.publisher.Flux;

@Controller
public class LiveLogController {
	private final WebLogAppender appender;
	private final LogAnalyzerService aiService;
	private final CodeScannerService codeScannerService;
	private final ProjectIntelligenceService intelligenceService;

	public LiveLogController(WebLogAppender appender, LogAnalyzerService aiService,
							 CodeScannerService scannerService, ProjectIntelligenceService intelligenceService) {
		this.appender = appender;
		this.aiService = aiService;
		this.codeScannerService = scannerService;
		this.intelligenceService = intelligenceService;
	}

	@GetMapping("/logs")
	public String showLogs() {
		return "logs";
	}

	@GetMapping("/analyze")
	public String showAnalysis() {
		return "analysis";
	}

	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<String> stream() {
		return appender.getLogStream();
	}

	@GetMapping("/api/run-analysis")
	@ResponseBody
	public LogAnalysis runAi() throws Exception {
		return aiService.analyzeAllLogs();
	}
	
	@GetMapping("/api/code-scan")
	@ResponseBody
	public ProjectStructure scanProject() throws IOException {
	    return codeScannerService.analyzeCodebase();
	}

	@GetMapping("/code-docs")
	public String showDocsPage() {
	    return "code-docs"; // Points to new HTML template
	}

	@GetMapping("/dashboard") public String dash() { return "dashboard"; }
	@GetMapping("/user") public String user() { return "user"; }
	@GetMapping("/developer") public String dev() { return "developer"; }
	@GetMapping("/architect") public String arch() { return "architect"; }

	@GetMapping("/api/data")
	@ResponseBody public ProjectIntelligence getData() throws IOException {
		return intelligenceService.getFullIntelligence();
	}

}
