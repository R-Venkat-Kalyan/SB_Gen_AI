package com.gen_ai.gemini_demo.config;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class LogStreamer {

	// A multicast sink allows multiple browser tabs to listen to the same log stream
    private final Sinks.Many<String> logSink = Sinks.many().multicast().onBackpressureBuffer();

    public void push(String message) {
        logSink.tryEmitNext(message);
    }

    public Flux<String> getLogFlux() {
        return logSink.asFlux();
    }
}
