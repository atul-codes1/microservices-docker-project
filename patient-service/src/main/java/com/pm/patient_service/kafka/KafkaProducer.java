package com.pm.patient_service.kafka;

import com.pm.patient_service.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public void sendEvent(Patient patient){
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CERATED")
                .build();

        try{
            kafkaTemplate.send("patient", patientEvent.toByteArray());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
