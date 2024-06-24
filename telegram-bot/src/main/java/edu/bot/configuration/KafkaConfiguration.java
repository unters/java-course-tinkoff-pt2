package edu.bot.configuration;

import edu.common.dto.event.AbstractEventTo;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

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
    public ConcurrentKafkaListenerContainerFactory<String, AbstractEventTo> kafkaListenerContainerFactory(
        @Value("${kafka.bootstrap.servers}")
        String bootstrapServers,
        @Value("${kafka.group.id}")
        String groupId
    ) {
        Map<String, Object> properties = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG, groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        );
        ConsumerFactory<String, AbstractEventTo> consumerFactory = new DefaultKafkaConsumerFactory<>(properties);

        ConcurrentKafkaListenerContainerFactory<String, AbstractEventTo> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
