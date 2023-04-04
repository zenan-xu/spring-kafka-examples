package zenan.producer.config;

import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import zenan.producer.model.MessageKafka;
import zenan.producer.model.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.tx-id-prefix}")
    private String transactionIdPrefix;
    @Value("${spring.kafka.producer.sasl-jaas-config}")
    private String saslJaasConfig;

    private ProducerFactory<String, MessageKafka<Payload>> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = kafkaProperties.buildProducerProperties();
        config.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        config.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        DefaultKafkaProducerFactory<String, MessageKafka<Payload>> defaultKafkaProducerFactory
                = new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new JsonSerializer<>());
        defaultKafkaProducerFactory.setTransactionIdPrefix(transactionIdPrefix);
        return defaultKafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate<String, MessageKafka<Payload>> messageKafkaKafkaTemplate(KafkaProperties kafkaProperties) {
        List<String> test = new ArrayList<>();
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }

}
