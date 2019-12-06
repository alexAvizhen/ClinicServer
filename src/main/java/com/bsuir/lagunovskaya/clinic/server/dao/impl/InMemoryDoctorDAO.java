package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryDoctorDAO implements DoctorDAO {
    private Map<Integer, Doctor> idToDoctorMap = new HashMap<>();
    private Integer doctorIdCounter = 1;

    private ClinicDepartmentDAO clinicDepartmentDAO = DAOProvider.getClinicDepartmentDAO();

    private static InMemoryDoctorDAO INSTANCE = null;

    private InMemoryDoctorDAO() {
    }

    public static InMemoryDoctorDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryDoctorDAO();
        }
        return INSTANCE;
    }

    @Override
    public Doctor getDoctorById(Integer id) {
        return idToDoctorMap.get(id);
    }

    @Override
    public Doctor getDoctorByLogin(String login) {
        for (Doctor currentDoctor : idToDoctorMap.values()) {
            if (login.equals(currentDoctor.getLogin())) {
                return currentDoctor;
            }
        }
        return null;
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        doctor.setId(doctorIdCounter);
        idToDoctorMap.put(doctorIdCounter, doctor);
        doctorIdCounter++;
        return doctor;
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        if (idToDoctorMap.containsKey(doctor.getId())) {
            idToDoctorMap.put(doctor.getId(), doctor);
        } else {
            throw new UnsupportedOperationException("Doctor with id " + doctor.getId() + " was not detected");
        }
        return doctor;
    }

    @Override
    public Doctor deleteDoctorById(Integer id) {
        return idToDoctorMap.remove(id);
    }

    @Override
    public Collection<Doctor> getAllDoctors() {
        return idToDoctorMap.values();
    }
}
