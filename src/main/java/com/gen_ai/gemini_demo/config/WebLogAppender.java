package com.gen_ai.gemini_demo.config;

import org.springframework.stereotype.Component;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class WebLogAppender extends AppenderBase<ILoggingEvent> {
	
	private final Sinks.Many<String> logSink = Sinks.many().multicast().onBackpressureBuffer();
    public Flux<String> getLogStream() { return logSink.asFlux(); }

    @Override
    protected void append(ILoggingEvent event) {
        logSink.tryEmitNext(event.getFormattedMessage());
    }

    @PostConstruct
    public void init() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)
                org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        this.setContext(root.getLoggerContext());
        this.start();
        root.addAppender(this);
    }

}
