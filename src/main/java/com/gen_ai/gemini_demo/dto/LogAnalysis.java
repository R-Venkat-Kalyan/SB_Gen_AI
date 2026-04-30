package com.gen_ai.gemini_demo.dto;

public record LogAnalysis(
		String generalSummary, String errorType, String rootCauseAnalysis, String fileLocation,
		String suggestedFixCode, String stepByStepPlan) {

}
