package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryPatientDAO implements PatientDAO {

    private Map<Integer, Patient> idToPatientMap = new HashMap<>();
    private Integer patientIdCounter = 1;

    private static InMemoryPatientDAO INSTANCE = null;

    private InMemoryPatientDAO() {
    }

    public static InMemoryPatientDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryPatientDAO();
        }
        return INSTANCE;
    }
    @Override
    public Patient getPatientById(Integer id) {
        return idToPatientMap.get(id);
    }

    @Override
    public Patient getPatientByLogin(String login) {
        for (Patient currentPatient : idToPatientMap.values()) {
            if (login.equals(currentPatient.getLogin())) {
                return currentPatient;
            }
        }
        return null;
    }

    @Override
    public Patient createPatient(Patient patient) {
        patient.setId(patientIdCounter);
        idToPatientMap.put(patientIdCounter, patient);
        patientIdCounter++;
        return patient;
    }

    @Override
    public void updatePatient(Patient patient) {
        if (idToPatientMap.containsKey(patient.getId())) {
            idToPatientMap.put(patient.getId(), patient);
        } else {
            throw new UnsupportedOperationException("Patient with id " + patient.getId() + " was not detected");
        }
    }

    @Override
    public void deletePatientById(Integer id) {
        idToPatientMap.remove(id);
    }

    @Override
    public Collection<Patient> getAllPatients() {
        return idToPatientMap.values();
    }
}
