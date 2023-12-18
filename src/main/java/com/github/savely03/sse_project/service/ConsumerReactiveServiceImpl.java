package com.github.savely03.sse_project.service;

import com.github.savely03.sse_project.model.DataTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class ConsumerReactiveServiceImpl implements ConsumerService {

    private final KafkaReceiver<String, DataTest> receiver;
    private final Sinks.Many<ServerSentEvent<DataTest>> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public Flux<ServerSentEvent<DataTest>> consume() {
        sink.tryEmitNext(ServerSentEvent.<DataTest>builder().id("f").data(new DataTest()).build());
        return receiver.receive()
                .doOnNext(consumerRecord ->
                        log.info("received key={}, value={}",
                                consumerRecord.key(),
                                consumerRecord.value())
                ).map(consumerRecord -> {
                    consumerRecord.receiverOffset().acknowledge();
                    return ServerSentEvent.<DataTest>builder()
                            .id(consumerRecord.key())
                            .data(consumerRecord.value())
                            .build();
                })
                .doOnError(throwable -> log.error("something went wrong while consuming : {}", throwable.getMessage()));

    }

}
