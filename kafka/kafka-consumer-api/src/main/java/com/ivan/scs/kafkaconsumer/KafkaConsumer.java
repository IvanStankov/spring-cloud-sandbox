package com.ivan.scs.kafkaconsumer;

import com.ivan.scs.measurement.Measurement;
import com.ivan.scs.util.UnitsConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaConsumer implements ConsumerSeekAware {

    private static final String TOPIC = "measurements";

    @Override
    public void onPartitionsAssigned(final Map<TopicPartition, Long> assignments, final ConsumerSeekCallback callback) {
        final Set<TopicPartition> partitions = assignments.keySet();
//        callback.seekToBeginning(partitions);
        log.info("Set offset to the beginning: {}", partitions.stream()
                .map(TopicPartition::toString)
                .collect(Collectors.joining("|")));
    }

    @KafkaListener(id = "measurement-logger",
            topics = TOPIC,
            groupId = "measurement-group-3",
            clientIdPrefix = "ivan-client-prefix",
            concurrency = "2"
    )
    public void consumeFirstGroup(final Measurement measurement) throws InterruptedException {
        log.info("Start - Received a measurement {}: {}", System.identityHashCode(measurement), measurement);
//        TimeUnit.SECONDS.sleep(3);
        log.info("End - the measurement {} processed", System.identityHashCode(measurement));
    }

    @KafkaListener(
            id = "measurement-fahrenheit-celsius-converter",
            topics = TOPIC,
            groupId = "ivan-group-100"
    )
    @SendTo("measurement-fahrenheit")
    public Measurement consumeSecondGroup(final Measurement measurement) {
        final int fahrenheitTemp = UnitsConverter.celsiusToFahrenheit(measurement.getTemperature());
        log.info("Resending a measurement to another topic: {} C -> {} F", measurement.getTemperature(), fahrenheitTemp);
        measurement.setTemperature(fahrenheitTemp);
        return measurement;
    }

    @KafkaListener(topics = "failing-consumer")
    public void failingConsumer(final String message) {
        log.info("Failing message: '{}'", message);
        if (true) {
            throw new RuntimeException("I am failing :(");
        }
    }

}
