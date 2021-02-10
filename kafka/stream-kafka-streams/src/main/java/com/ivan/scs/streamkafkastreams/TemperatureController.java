package com.ivan.scs.streamkafkastreams;

import com.ivan.scs.streamkafkastreams.temperature.TemperatureStatistics;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/temperature")
public class TemperatureController {

    private final InteractiveQueryService interactiveQueryService;

    @GetMapping("/statistics")
    public TemperatureStatistics statistics() {

        final ReadOnlyKeyValueStore<Object, Object> queryableStore =
                this.interactiveQueryService.getQueryableStore("temperature-avg-store", QueryableStoreTypes.keyValueStore());

        return TemperatureStatistics.builder()
                .avgWarm((Long) queryableStore.get("warm"))
                .avgCold((Long) queryableStore.get("cold"))
                .localDateTime(LocalDateTime.now())
                .build();
    }

}
