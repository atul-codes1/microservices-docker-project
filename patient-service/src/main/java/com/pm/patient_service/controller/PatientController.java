package com.pm.patient_service.controller;

import com.pm.patient_service.dto.CreatePatientRequest;
import com.pm.patient_service.dto.PatientResponse;
import com.pm.patient_service.dto.UpdatePatientRequest;
import com.pm.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
@Tag(name = "Patient-Service",description = "All API Endpoint to manage patient-service")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientResponse>> getPatients() {
        List<PatientResponse> patients = patientService.getPatients();

        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest createPatientRequest) {
        PatientResponse patientResponse = patientService.createPatient(createPatientRequest);

        return ResponseEntity.ok().body(patientResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update the patient")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable UUID id,@Valid @RequestBody UpdatePatientRequest updatePatientRequest) {
        PatientResponse patientResponse = patientService.updatePatient(id,updatePatientRequest);

        return ResponseEntity.ok().body(patientResponse);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the patient")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);

        return ResponseEntity.ok("Patient deleted successfully");
    }
}
