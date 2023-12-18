package com.github.savely03.sse_project.controller;

import com.github.savely03.sse_project.model.DataTest;
import com.github.savely03.sse_project.service.ConsumerService;
import com.github.savely03.sse_project.service.ConsumerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final ConsumerService consumerService;
    private final ConsumerServiceImpl consumerServiceImpl;

    @GetMapping(value = "/hello", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<DataTest>> hello() {
        return consumerServiceImpl.get();
    }

}
