package com.github.savely03.sse_project.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ConsumerService {
    Flux<ServerSentEvent<String>> consume();

}
