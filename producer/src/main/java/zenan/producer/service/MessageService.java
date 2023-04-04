package zenan.producer.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zenan.producer.model.MessageKafka;
import zenan.producer.model.Payload;

@Service
public class MessageService {

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Value("${spring.kafka.producer.message-type}")
    private String messageType;

    KafkaTemplate<String, MessageKafka<Payload>> kafkaTemplate;

    public MessageService(KafkaTemplate<String, MessageKafka<Payload>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Payload payload) {
        kafkaTemplate.send(topic, new MessageKafka<>(messageType, payload));
    }

    public void sendMessageWithHeader(Payload payload) {
        var producerRecord = new ProducerRecord<String, MessageKafka<Payload>>(topic, new MessageKafka<>(messageType, payload));
        kafkaTemplate.send(producerRecord);
    }

    @Transactional
    public void sendMessageWithException(Payload payload) {
        sendMessage(payload);
        throw new RuntimeException("exception occurred, message should have been aborted");
    }
}
