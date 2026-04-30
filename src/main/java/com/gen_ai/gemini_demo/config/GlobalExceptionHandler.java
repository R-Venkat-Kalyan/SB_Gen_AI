package com.gen_ai.gemini_demo.config;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
  @ExceptionHandler({AsyncRequestNotUsableException.class, IOException.class})
  public void handleAsyncAbort() {
      // Just catch it. This happens when the browser tab is closed or refreshed.
      // No need to print a 200-line stack trace.
  }

}
