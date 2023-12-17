//package com.github.savely03.sse_project.service;
//
//import com.github.savely03.sse_project.model.DataTest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class ConsumerServiceImpl {
//
//    @KafkaListener(topics = "TEST.TOPIC", groupId = "group-test")
//    public void consume(@Payload DataTest dataTest) {
//        log.info("Got object {}", dataTest);
//    }
//
//}
