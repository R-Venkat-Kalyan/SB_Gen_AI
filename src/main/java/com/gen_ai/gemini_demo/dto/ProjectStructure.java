package com.gen_ai.gemini_demo.dto;

import java.util.List;

public record ProjectStructure(
		String userSummary,           // App purpose for normal users
	    String technicalOverview,     // Architecture summary for developers
	    List<RouteDetail> apiRoutes,  // List of all routes found
	    String dataFlowDescription,   // How data moves (Controller -> Service -> Repo)
	    String missingComponents      // Mention if DB/Repo or certain layers are missing
	) {}

	record RouteDetail(
			String path, String method, String purpose, String howToCall)  {}
