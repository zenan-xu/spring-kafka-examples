package zenan.consumer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import zenan.consumer.model.MessageKafka;
import zenan.consumer.model.Payload;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(
            topics = "${spring.kafka.consumer.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void kafkaConsumer(MessageKafka<Payload> message) {

    }
}
