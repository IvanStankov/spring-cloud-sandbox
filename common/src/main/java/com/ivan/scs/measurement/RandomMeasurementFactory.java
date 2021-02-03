package com.ivan.scs.measurement;

import java.time.LocalDateTime;
import java.util.Random;

public class RandomMeasurementFactory implements MeasurementFactory {

    @Override
    public Measurement createMeasurement() {
        final Random random = new Random();
        return new Measurement(
                -50 + random.nextInt(100),
                40 + random.nextInt(50),
                LocalDateTime.now()
        );
    }
}
