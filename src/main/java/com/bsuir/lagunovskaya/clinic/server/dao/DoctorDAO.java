package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;

import java.util.Collection;

public interface DoctorDAO {
    Doctor getDoctorById(Integer id);

    Doctor getDoctorByLogin(String login);

    Doctor createDoctor(Doctor doctor);

    void updateDoctor(Doctor doctor);

    void deleteDoctorById(Integer id);

    Collection<Doctor> getAllDoctors();
}
