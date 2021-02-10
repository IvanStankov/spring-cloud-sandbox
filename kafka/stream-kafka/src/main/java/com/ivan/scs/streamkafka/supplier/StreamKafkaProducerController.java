package com.ivan.scs.streamkafka.supplier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/kafka-stream-producer")
public class StreamKafkaProducerController {

    private final StreamBridge streamBridge;

    @PostMapping("/stream-bridge")
    public String sendMessageSteamBridge(@RequestBody final String message) {
        this.streamBridge.send("fromBridge-out-0", message + " - " + new Random().nextInt(1000));
        return "sendMessageStreamBridge(): " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @PostMapping("/failing-consumer")
    public String sendMessageFailingConsumer() {
        this.streamBridge.send("failingConsumerSender-out-0", "Hello, failing consumer!");
        return "sendMessageFailingConsumer(): " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
