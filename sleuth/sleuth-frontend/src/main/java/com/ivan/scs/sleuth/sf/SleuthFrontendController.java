package com.ivan.scs.sleuth.sf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SleuthFrontendController {

    @Value("${service.one.url}")
    private String serviceOneUrl;
    @Value("${service.two.url}")
    private String serviceTwoUrl;

    private final RestTemplate restTemplate;
    private final SleuthFrontendService sleuthFrontendService;

    @GetMapping("/frontend")
    public String frontend() {

        final String serviceOneResponse = this.restTemplate.getForObject(this.serviceOneUrl, String.class);
        final String serviceTwoResponse = this.restTemplate.getForObject(this.serviceTwoUrl, String.class);
        log.info("Frontend invocation: {}", serviceTwoResponse);

        return "Frontend("
                + this.sleuthFrontendService.getHours()
                + ":"
                + this.sleuthFrontendService.getMinutes(new Random().nextInt(10))
                + ") "
                + serviceOneResponse
                + " - "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
