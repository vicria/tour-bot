//package ar.vicria.subte.microservice.services.kafka;
//
//import ar.vicria.subte.dto.RouteDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.converter.StringJsonMessageConverter;
//
//@TestConfiguration
//public class TestWithKafka {
//
//    @Bean
//    public KafkaTemplate<String, RouteDto> clusterEventKafkaTemplate(
//            ProducerFactory<String, RouteDto> producerFactory,
//            ObjectMapper objectMapper) {
//        KafkaTemplate<String, RouteDto> kafkaTemplate = new KafkaTemplate<>(producerFactory);
//        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter(objectMapper));
//        return kafkaTemplate;
//    }
//
//}
