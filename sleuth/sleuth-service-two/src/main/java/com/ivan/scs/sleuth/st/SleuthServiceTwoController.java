package com.ivan.scs.sleuth.st;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
public class SleuthServiceTwoController {

    @GetMapping("/service-two")
    public String serviceTwo() {
        final int randomInt = new Random().nextInt(100);
        log.info("ServiceTwo invocation: {}", randomInt);
        return "ServiceTwo-" + randomInt;
    }

}
