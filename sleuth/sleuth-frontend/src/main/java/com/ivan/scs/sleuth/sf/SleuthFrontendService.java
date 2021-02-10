package com.ivan.scs.sleuth.sf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SleuthFrontendService {

    @NewSpan
    public String getHours() {
        log.info("Returning hours");
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH"));
    }

    @ContinueSpan(log = "aaaaaa")
    public String getMinutes(@SpanTag("some-param") final int i) {
        log.info("Returning minutes({})", i);
        return LocalTime.now().format(DateTimeFormatter.ofPattern("mm"));
    }

}
