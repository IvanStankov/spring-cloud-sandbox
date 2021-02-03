package com.ivan.scs.streamkafka.supplier;

import com.ivan.scs.measurement.Measurement;
import com.ivan.scs.measurement.MeasurementFactory;
import com.ivan.scs.measurement.RandomMeasurementFactory;
import com.ivan.scs.util.UnitsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@Slf4j
public class StreamKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamKafkaApplication.class);
    }

    @Bean
    public Supplier<String> randomNumber() {
        // Publishes a message each second
        return () -> {
            final String message = "Current number: " + new Random().nextInt(100);
            log.info(message);
            return message;
        };
    }

    @Bean
    public Supplier<Flux<Measurement>> measurementCelsius(final MeasurementFactory measurementFactory) {
        return () -> Flux.interval(Duration.ofSeconds(2))
                .map(l -> measurementFactory.createMeasurement())
                .doOnNext(measurement -> log.info(measurement.toString()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Bean
    public Function<Measurement, Measurement> measurementFahrenheit() {
        // Reads a message from the topic and publishes it to another topic
        return measurement -> {
            measurement.setTemperature(UnitsConverter.celsiusToFahrenheit(measurement.getTemperature()));
            return measurement;
        };
    }

    @Bean
    public Consumer<Measurement> fahrenheitSink() {
        return measurement -> {
            log.info("Sink for fahrenheit temperature: {}", measurement.getTemperature());
        };
    }

    @Bean
    public Consumer<Flux<Measurement>> fahrenheitFluxSink() {
        return flux -> flux
                .filter(m -> m.getHumidity() > 60)
                .map(m -> "Humidity is too high: " + m.getHumidity())
                .subscribe(log::info);
    }

    @Bean
    public Consumer<String> failingConsumer() {
        return message -> {
            log.info("Failing message: '{}'", message);
            if (true) {
                throw new RuntimeException("I am failing :(");
            }
        };
    }

    @Bean
    public MeasurementFactory measurementFactory() {
        return new RandomMeasurementFactory();
    }

}
