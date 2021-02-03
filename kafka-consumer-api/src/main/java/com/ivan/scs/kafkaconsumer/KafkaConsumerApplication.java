package com.ivan.scs.kafkaconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@SpringBootApplication
public class KafkaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApplication.class);
    }

    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public RecordInterceptor<?, ?> interceptor() {
        return record -> {
            log.info("Intercepting a message {} - {}", record.key(), record.value());
            return record;
        };
    }

    @Bean
    public ErrorHandler errorHandler(final KafkaOperations<?, ?> template) {
        // It pushes error messages to DLQ after 2 attempts
        return new SeekToCurrentErrorHandler(
                new DeadLetterPublishingRecoverer(template),
                new FixedBackOff(500L, 2L)
        );
    }

}
