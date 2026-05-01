package com.gen_ai.gemini_demo.dto;

import java.util.List;

public record ProjectIntelligence(UserView userView, DeveloperView developerView, ArchitectView architectView) { }

// User Persona
record UserView(String appPurpose, List<UserStep> steps, String guidanceNotes) {}
record UserStep(String pageName, String url, String details, String uiActions) {}

// Developer Persona
record DeveloperView(String archSummary, List<DevRoute> routes, String techStack) {}
record DevRoute(String endpoint, String method, String logic, String renderedUi, String purpose) {}

// Architect Persona
record ArchitectView(String auditScore, List<CodeIssue> issues, String suggestions) {}
record CodeIssue(String rule, String severity, String badCode, String fix) {}
