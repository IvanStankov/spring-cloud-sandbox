package com.ivan.scs.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ConfigClientController {

    @Value("${config-client.message}")
    private String greeting;

    @GetMapping("/greeting")
    public String sayHi() {
        return this.greeting + " " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
