package com.ivan.scs.sleuth.so;

import brave.baggage.BaggageField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SleuthServiceOneController {

    private final BaggageField randomIntBaggageField;

    @GetMapping("/service-one")
    public String serviceOne() {
        final int randomInt = new Random().nextInt(100);
        this.randomIntBaggageField.updateValue(String.valueOf(randomInt));

        log.info("ServiceOne invocation: {}", randomInt);

        return "ServiceOne-" + randomInt;
    }

}
