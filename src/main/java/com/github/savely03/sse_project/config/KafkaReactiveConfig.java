package com.github.savely03.sse_project.config;


import com.github.savely03.sse_project.model.DataTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaReactiveConfig {

    @Value("${SUBSCRIBED_TOPIC}")
    private String topic;

    @Bean
    public Map<String, Object> receiverProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-test");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ReceiverOptions<String, DataTest> receiverOptions() {
        ReceiverOptions<String, DataTest> receiverOptions = ReceiverOptions.<String, DataTest>create(receiverProperties())
                .withKeyDeserializer(new StringDeserializer())
                .withValueDeserializer(new JsonDeserializer<>(DataTest.class));
        return receiverOptions.subscription(Collections.singletonList(topic))
                .addAssignListener(partitions -> log.info("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.info("onPartitionsRevoked {}", partitions));
    }

    @Bean
    public KafkaReceiver<String, DataTest> reactiveKafkaConsumerTemplate(
            ReceiverOptions<String, DataTest> receiverOptions) {
        return KafkaReceiver.create(receiverOptions);
    }

}
