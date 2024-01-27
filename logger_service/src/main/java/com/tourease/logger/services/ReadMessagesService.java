package com.tourease.logger.services;

import com.tourease.logger.models.entities.Chronology;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;

@Service
@AllArgsConstructor
@Slf4j
public class ReadMessagesService {
    private final ChronologyService chronologyService;

    @KafkaListener(topics = "gateway_service", groupId = "gateway_service")
    public void listenGatewayService(ConsumerRecord<String, String> record) {
        createChronology(record);
    }

    @KafkaListener(topics = "user_service", groupId = "user_service")
    public void listenUserService(ConsumerRecord<String, String> record) {
        createChronology(record);
    }

    @KafkaListener(topics = "core_service", groupId = "core_service")
    public void listenCoreService(ConsumerRecord<String, String> record) {
        createChronology(record);
    }

    @KafkaListener(topics = "hotel_service", groupId = "hotel_service")
    public void listenHotelService(ConsumerRecord<String, String> record) {
        createChronology(record);
    }

    @KafkaListener(topics = "transport_service", groupId = "transport_service")
    public void listenTransportService(ConsumerRecord<String, String> record) {
        createChronology(record);
    }

    @KafkaListener(topics = "configuration_service", groupId = "configuration_service")
    public void listenConfigurationService(ConsumerRecord<String, String> record) {
        createChronology(record);
    }

    private void createChronology(ConsumerRecord<String, String> record) {
        Chronology chronology = new Chronology();
        chronology.setEmail(record.key());
        chronology.setLog(record.value());
        chronology.setCreatedOn(Instant.ofEpochMilli(record.timestamp()).atZone(ZoneId.systemDefault()).toOffsetDateTime());

        chronologyService.saveChronology(chronology);

        log.info(chronology.getEmail()+" "+chronology.getLog()+" "+chronology.getCreatedOn());
    }
}
