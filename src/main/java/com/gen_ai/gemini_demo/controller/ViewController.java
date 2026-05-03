package com.gen_ai.gemini_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all UI routing for the application.
 * Returns the logical view names for Thymeleaf templates.
 */
@Controller
public class ViewController {

    @GetMapping("/") public String index() { return "index"; }

    @GetMapping("/dashboard") public String dashboard() { return "dashboard"; }

    @GetMapping("/user") public String userGuide() { return "user"; }

    @GetMapping("/developer") public String developerKt() { return "developer"; }

    @GetMapping("/architect") public String architectAudit() { return "architect"; }

    @GetMapping("/code-docs") public String codeDocs() { return "code-docs"; }

    @GetMapping("/terminal") public String chatTerminal() { return "chat-terminal"; }

    @GetMapping("/logs") public String liveLogs() { return "logs"; }

    @GetMapping("/analyze") public String logAnalysisView() { return "analysis"; }
}

