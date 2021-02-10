package com.ivan.scs.sleuth.so;

import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SleuthServiceOneApplication {

    @Bean
    public BaggageField randomIntBaggageField() {
        return BaggageField.create("random-int");
    }

    @Bean
    public CurrentTraceContext.ScopeDecorator mdcScopeDecorator() {
        return MDCScopeDecorator.newBuilder()
                .clear()
                .add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(randomIntBaggageField())
                        .flushOnUpdate()
                        .build())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SleuthServiceOneApplication.class, args);
    }

}
