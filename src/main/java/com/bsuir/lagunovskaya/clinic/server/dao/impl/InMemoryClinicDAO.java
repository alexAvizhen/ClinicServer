package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDAO;
import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;

import java.util.HashMap;
import java.util.Map;

public class InMemoryClinicDAO implements ClinicDAO {

    private Map<Integer, Clinic> idToClinicMap = new HashMap<>();
    private Integer clinicIdCounter = 1;

    private static InMemoryClinicDAO INSTANCE = null;

    private InMemoryClinicDAO() {
    }

    public static InMemoryClinicDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryClinicDAO();
        }
        return INSTANCE;
    }

    @Override
    public Clinic createClinic(Clinic clinic) {
        clinic.setId(clinicIdCounter);
        idToClinicMap.put(clinic.getId(), clinic);
        clinicIdCounter++;
        return clinic;
    }

    @Override
    public Clinic getClinicById(Integer id) {
        return idToClinicMap.get(id);
    }

    @Override
    public Clinic updateClinic(Clinic clinic) {
        return null;
    }
}
