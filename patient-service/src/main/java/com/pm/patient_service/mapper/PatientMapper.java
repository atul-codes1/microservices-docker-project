package com.pm.patient_service.mapper;

import com.pm.patient_service.dto.CreatePatientRequest;
import com.pm.patient_service.dto.PatientResponse;
import com.pm.patient_service.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponse toDto(Patient patient){

        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(patient.getId().toString());
        patientResponse.setName(patient.getName());
        patientResponse.setAddress(patient.getAddress());
        patientResponse.setEmail(patient.getEmail());
        patientResponse.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientResponse;
    }
    public static Patient toModel(CreatePatientRequest createPatientRequest){

        Patient patient = new Patient();
        patient.setName(createPatientRequest.getName());
        patient.setAddress(createPatientRequest.getAddress());
        patient.setEmail(createPatientRequest.getEmail());
        patient.setDateOfBirth(LocalDate.parse(createPatientRequest.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(createPatientRequest.getRegisteredDate()));
        return patient;
    }
}
