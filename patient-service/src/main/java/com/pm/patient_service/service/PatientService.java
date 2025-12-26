package com.pm.patient_service.service;

import com.pm.patient_service.dto.CreatePatientRequest;
import com.pm.patient_service.dto.PatientResponse;
import com.pm.patient_service.dto.UpdatePatientRequest;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.KafkaProducer;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public List<PatientResponse> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponse> patientResponses = patients.stream().map(patient -> PatientMapper.toDto(patient)).toList();
        return patientResponses;
    }

    public PatientResponse createPatient(CreatePatientRequest createPatientRequest) {

        if (patientRepository.existsByEmail(createPatientRequest.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + "already exists - " + createPatientRequest.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(createPatientRequest));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        kafkaProducer.sendEvent(newPatient);
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponse updatePatient(UUID id, @Valid UpdatePatientRequest updatePatientRequest) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
        if (patientRepository.existsByEmailAndIdNot(updatePatientRequest.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email " + "already exists - " + updatePatientRequest.getEmail());
        }
        patient.setName(updatePatientRequest.getName());
        patient.setAddress(updatePatientRequest.getAddress());
        patient.setEmail(updatePatientRequest.getEmail());
        patient.setDateOfBirth(LocalDate.parse(updatePatientRequest.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDto(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
