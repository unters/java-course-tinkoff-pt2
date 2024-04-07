package edu.scrapper.configuration;

import edu.common.dto.event.AbstractEventTo;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public NewTopic topic(
        @Value("${kafka.topic.name}")
        String topicName,
        @Value("${kafka.topic.partitions}")
        int numberOfPartitions,
        @Value("${kafka.topic.replicas}")
        int numberOfReplicas
    ) {
        return TopicBuilder
            .name(topicName)
            .partitions(numberOfPartitions)
            .replicas(numberOfReplicas)
            .build();
    }

    @Bean
    public KafkaTemplate<String, AbstractEventTo> kafkaTemplate(
        @Value("${kafka.bootstrap.servers}")
        String bootstrapServers
    ) {
        Map<String, Object> properties = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        ProducerFactory<String, AbstractEventTo> producerFactory = new DefaultKafkaProducerFactory<>(properties);
        return new KafkaTemplate<>(producerFactory);
    }
}
