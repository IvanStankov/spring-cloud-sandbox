package com.ivan.scs.kafkaproducer;

import com.ivan.scs.measurement.Measurement;
import com.ivan.scs.measurement.MeasurementFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/kafka-producer")
public class KafkaProducerController {

    private static final String TOPIC = "measurements";

    private final MeasurementFactory measurementFactory;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/sync")
    public String sendMessageSync() {
        final Measurement measurement = this.measurementFactory.createMeasurement();
        final String key = String.valueOf(System.identityHashCode(measurement));
        this.kafkaTemplate.send(TOPIC, key, measurement);
        return "sendMessageSync(): " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @PostMapping("/async")
    public String sendMessageAsync() {
        final Measurement measurement = this.measurementFactory.createMeasurement();
        final String key = String.valueOf(System.identityHashCode(measurement));

        this.kafkaTemplate.send(TOPIC, key, measurement)
                .addCallback(res -> log.info("Measurement has been sent to the topic"),
                        ex -> log.error("Something happened", ex));

        log.info("Measurement message sending initiated");
        return "sendMessageAsync(): " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @PostMapping("/failing-consumer")
    public String sendMessageFailingConsumer() {
        this.kafkaTemplate.send("failing-consumer", "Hello, failing consumer!");
        return "sendMessageFailingConsumer(): " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
