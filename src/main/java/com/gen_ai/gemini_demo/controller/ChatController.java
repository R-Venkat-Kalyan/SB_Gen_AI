package com.gen_ai.gemini_demo.controller;

import java.io.IOException;

import com.gen_ai.gemini_demo.service.ProjectIntelligenceService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import com.gen_ai.gemini_demo.dto.ProjectStructure;
import com.gen_ai.gemini_demo.service.ChatService;
import com.gen_ai.gemini_demo.service.CodeScannerService;

@RestController
public class ChatController {
	
	private final ChatService chatService;
	private final ProjectIntelligenceService intelligenceService;
	private final ChatClient chatClient;

	public ChatController(ChatService chatService,
						  ProjectIntelligenceService intelligenceService,
						  ChatClient.Builder builder) {
		this.chatService = chatService;
		this.intelligenceService = intelligenceService;
		this.chatClient = builder.build();
		
	}
	
//	http://localhost:5000/chat?prompt=who%20is%20iron%20man
	@GetMapping("/chat")
	public String chat(@RequestParam String prompt) {
		return chatService.ask(prompt);
	}


	@GetMapping("/terminal")
	public String chatPage() {
		return "chat-terminal";
	}

	@PostMapping("/ask")
	@ResponseBody
	public String askQuestion(@RequestBody String userQuestion) throws java.io.IOException {
		// 1. Get the codebase context (Cached in service)
		String codebase = intelligenceService.readCodebaseForChat();

		// 2. Query the AI with the codebase as context
		return chatClient.prompt()
				.system("You are 'Project-IQ', an expert AI assistant specialized in this specific Spring Boot project. " +
						"Use the provided codebase to answer any technical or functional questions accurately. " +
						"If asked about logic, point to the specific file. Codebase: " + codebase)
				.user(userQuestion)
				.call()
				.content();
	}
	
	

}
