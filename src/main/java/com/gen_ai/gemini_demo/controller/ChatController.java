package com.gen_ai.gemini_demo.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gen_ai.gemini_demo.dto.ProjectStructure;
import com.gen_ai.gemini_demo.service.ChatService;
import com.gen_ai.gemini_demo.service.CodeScannerService;

@RestController
public class ChatController {
	
	private final ChatService chatService;

	public ChatController(ChatService chatService, CodeScannerService scannerService) {
		this.chatService = chatService;
		
	}
	
//	http://localhost:5000/chat?prompt=who%20is%20iron%20man
	@GetMapping("/chat")
	public String chat(@RequestParam String prompt) {
		return chatService.ask(prompt);
	}
	
	

}
