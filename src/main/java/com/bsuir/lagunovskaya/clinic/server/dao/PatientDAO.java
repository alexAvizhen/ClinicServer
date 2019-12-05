package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;

import java.util.Collection;

public interface PatientDAO {

    Patient getPatientById(Integer id);

    Patient getPatientByLogin(String login);

    Patient createPatient(Patient patient);

    Patient updatePatient(Patient patient);

    Patient deletePatientById(Integer id);

    Collection<Patient> getAllPatients();
}
