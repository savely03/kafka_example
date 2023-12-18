package com.github.savely03.sse_project.service;

import com.github.savely03.sse_project.model.DataTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class ConsumerServiceImpl {

    private final Sinks.Many<ServerSentEvent<DataTest>> sink = Sinks.many().multicast().onBackpressureBuffer();

    @KafkaListener(topics = "${SUBSCRIBED_TOPIC}", groupId = "group-test")
    public void consume(@Payload DataTest dataTest) {
        sink.tryEmitNext(ServerSentEvent.<DataTest>builder()
                .data(dataTest)
                .build());
    }

    public Flux<ServerSentEvent<DataTest>> get() {
        return sink.asFlux()
                .doOnNext(value -> log.info("received value={}", value))
                .doOnError(throwable -> log.error("something went wrong while consuming : {}", throwable.getMessage()));
    }

}
