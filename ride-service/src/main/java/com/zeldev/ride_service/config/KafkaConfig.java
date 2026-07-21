package com.zeldev.ride_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    //topic used by ride service to publish ride requested events, and matching service to consume it
    @Bean
    public NewTopic rideRequestedTopic() {
        return TopicBuilder
                .name("ride.requested")
                .partitions(3)
                .replicas(1)
                .build();
    }

    //topic used by matching service to publish ride matched events, and ride service to consume it
    @Bean
    public NewTopic rideMatchedTopic() {
        return TopicBuilder
                .name("ride.matched")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
