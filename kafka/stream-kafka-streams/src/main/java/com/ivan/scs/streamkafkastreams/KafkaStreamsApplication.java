package com.ivan.scs.streamkafkastreams;

import com.ivan.scs.measurement.Measurement;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class KafkaStreamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsApplication.class);
    }

    @Bean
    public Consumer<KStream<String, String>> firstTry() {
        return kStream -> kStream
                .map((k, v) -> new KeyValue<>(k, v.toUpperCase()))
                .peek((k, v) -> log.info("Stream message: {} - {}", k, v))
                .groupByKey()
                .reduce((value1, value2) -> value1 + value2, Materialized.as("concat-store"))
                .toStream()
                .foreach((k, v) -> System.out.printf("%s - %s\n", k, v));
    }

    // ----------------------------------------------------------------------------------------------
    @Bean
    public Consumer<KStream<String, Measurement>> temperatureSplitterCounter() {
        return kStream -> kStream
                .groupBy(this::warmColdMapper,
                        Grouped.<String, Measurement>as("warm-cold-grouped")
                                .withValueSerde(new JsonSerde<>(Measurement.class)))
                .count(Named.as("counter"), Materialized.as("counter-store"))
                .toStream(Named.as("to-stream"))
                .peek((k, v) -> log.info("Count: {} - {}", k, v), Named.as("count-logger"));
    }

    // ----------------------------------------------------------------------------------------------
    @Bean
    public Function<KStream<String, Measurement>, KStream<String, Measurement>> warmColdSplitter() {
        return kStream -> kStream
                .selectKey(this::warmColdMapper, Named.as("warm-cold-key-selector"))
                .peek((k, v) -> log.info("Split: {} - {}", k, v), Named.as("warm-cold-splitter-logger"));
    }

    @Bean
    public Function<KStream<String, Measurement>, KStream<String, Long>> temperatureCounter() {
        return kStream -> kStream
                .groupByKey(Grouped.as("temperature-counter-group-by-key"))
                .count(Named.as("temperature-counter-aggregator"))
                .toStream()
                .peek((k, v) -> log.info("Count: {} - {}", k, v));
    }

    @Bean
    public Function<KStream<String, Measurement>, KStream<String, Long>> temperatureAdder() {
        return kStream -> kStream
                .groupByKey(Grouped.as("temperature-adder-group-by-key"))
                .aggregate(() -> 0L, (k, v, va) -> va + v.getTemperature(),
                        Materialized.<String, Long>as(Stores.persistentKeyValueStore("temperature-adder-store"))
                                .withValueSerde(Serdes.Long()))
                .toStream()
                .peek((k, v) -> log.info("Add: {} - {}", k, v));
    }

    @Bean
    public BiFunction<KTable<String, Long>, KTable<String, Long>, KStream<String, Long>> temperatureAvg() {
        return (added, counted) ->
                added.join(counted, (v1, v2) -> v1 / v2,
                        Materialized.<String, Long>as(Stores.persistentKeyValueStore("temperature-avg-store"))
                                .withValueSerde(Serdes.Long())
                )
                        .toStream()
                        .peek((k, v) -> log.info("Avg: {} - {}", k, v));
    }

    // ----------------------------------------------------------------------------------------------
    private String warmColdMapper(final String key, final Measurement value) {
        return value.getTemperature() > 0 ? "warm" : "cold";
    }
}
