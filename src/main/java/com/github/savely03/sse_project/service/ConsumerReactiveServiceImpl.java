package com.github.savely03.sse_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class ConsumerReactiveServiceImpl implements ConsumerService {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

    @Override
    public Flux<ServerSentEvent<String>> consume() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                .doOnNext(consumerRecord -> log.info("received key={}, value={}",
                                consumerRecord.key(),
                                consumerRecord.value()
                        )
                )
                .map(consumerRecord -> ServerSentEvent.<String>builder()
                        .id(consumerRecord.key())
                        .data(consumerRecord.value())
                        .build())
                .doOnError(throwable -> log.error("something went wrong while consuming : {}", throwable.getMessage()));
    }

}
