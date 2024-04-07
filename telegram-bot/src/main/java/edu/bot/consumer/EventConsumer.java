package edu.bot.consumer;

import edu.bot.service.EventService;
import edu.common.dto.event.AbstractEventTo;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final EventService eventService;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, AbstractEventTo> consumerRecord) {
        Long chatId = Long.valueOf(consumerRecord.key());
        AbstractEventTo eventTo = consumerRecord.value();
        eventService.sendEvent(chatId, eventTo);
    }
}
