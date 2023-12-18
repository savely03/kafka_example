package com.github.savely03.sse_project.service;

import com.github.savely03.sse_project.model.DataTest;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ConsumerService {
    Flux<ServerSentEvent<DataTest>> consume();

}
