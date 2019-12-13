package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;

import java.util.Collection;

public interface PatientDAO {

    Patient getPatientById(Integer id);

    Patient getPatientByLogin(String login);

    Patient createPatient(Patient patient);

    void updatePatient(Patient patient);

    void deletePatientById(Integer id);

    Collection<Patient> getAllPatients();
}
