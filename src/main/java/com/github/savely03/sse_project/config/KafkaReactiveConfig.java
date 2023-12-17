package com.github.savely03.sse_project.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.savely03.sse_project.model.DataTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaReactiveConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Value("${SUBSCRIBED_TOPIC}")
    private String topic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

//    @Bean
//    public Map<String, Object> receiverProperties() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                "localhost:29092");
//        props.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                "group-test");
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        return props;
//    }

    @Bean
    public ReceiverOptions<String, String> receiverOptions(KafkaProperties kafkaProperties) {
        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return receiverOptions.subscription(Collections.singletonList(topic))
                .addAssignListener(partitions -> log.info("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.info("onPartitionsRevoked {}", partitions));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, String> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }

}
