package com.ivan.scs.streamkafkastreams.temperature;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemperatureStatistics {

    private final long avgWarm;
    private final long avgCold;
    private final LocalDateTime localDateTime;

}
