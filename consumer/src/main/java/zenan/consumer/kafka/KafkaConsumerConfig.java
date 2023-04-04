package zenan.consumer.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import zenan.consumer.model.MessageKafka;
import zenan.consumer.model.Payload;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.message-type}")
    private String messageType;
//    @Value("${spring.kafka.consumer.sasl-jaas-config}")
//    private String saslJaasConfig;

    private JsonDeserializer<MessageKafka<Payload>> kafkaPayloadDeserializer() {
        var objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(MessageKafka.class, Payload.class);
        return new JsonDeserializer<>(type, objectMapper, false);
    }

    private ConsumerFactory<String, MessageKafka<Payload>> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = kafkaProperties.buildConsumerProperties();
//        config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
//        config.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        ErrorHandlingDeserializer<MessageKafka<Payload>> errorHandlingDeserializer
                = new ErrorHandlingDeserializer<>(kafkaPayloadDeserializer());
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageKafka<Payload>> kafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, MessageKafka<Payload>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaProperties));
        // refuse all messages that don't equal to the message type
        factory.setRecordFilterStrategy(strategy -> !strategy.value().getType().equalsIgnoreCase(messageType));

        // add retry strategy : for ex no retry
        // Use SeekToCurrentErrorHandler before 2.7.x of spring kafka (2.5.x of spring boot)
//        factoryListener.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(0, 0)));
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(0, 0)));
        return factory;
    }
}
