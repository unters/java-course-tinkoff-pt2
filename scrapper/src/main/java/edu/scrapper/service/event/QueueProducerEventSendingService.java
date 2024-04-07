package edu.scrapper.service.event;

import edu.common.dto.event.AbstractEventTo;
import edu.scrapper.service.EventSendingService;
import org.springframework.kafka.core.KafkaTemplate;

public class QueueProducerEventSendingService implements EventSendingService {

    private final KafkaTemplate<String, AbstractEventTo> kafkaTemplate;
    private final String topic;

    public QueueProducerEventSendingService(
        KafkaTemplate<String, AbstractEventTo> kafkaTemplate,
        String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void send(Long chatId, AbstractEventTo eventTo) {
        kafkaTemplate.send(topic, chatId.toString(), eventTo);
    }
}
