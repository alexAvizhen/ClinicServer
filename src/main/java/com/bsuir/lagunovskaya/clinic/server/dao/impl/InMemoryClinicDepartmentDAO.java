package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryClinicDepartmentDAO implements ClinicDepartmentDAO {

    private Map<Integer, ClinicDepartment> idToClinicDepartmentMap = new HashMap<>();
    private Integer clinicDepartmentsIdCounter = 1;

    private static InMemoryClinicDepartmentDAO INSTANCE = null;

    private InMemoryClinicDepartmentDAO() {
    }

    public static InMemoryClinicDepartmentDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryClinicDepartmentDAO();
        }
        return INSTANCE;
    }

    @Override
    public ClinicDepartment createClinicDepartment(ClinicDepartment clinicDepartment) {
        clinicDepartment.setId(clinicDepartmentsIdCounter);
        idToClinicDepartmentMap.put(clinicDepartment.getId(), clinicDepartment);
        clinicDepartmentsIdCounter++;
        return clinicDepartment;
    }

    @Override
    public ClinicDepartment updateClinicDepartment(ClinicDepartment clinicDepartment) {
        if (idToClinicDepartmentMap.containsKey(clinicDepartment.getId())) {
            idToClinicDepartmentMap.put(clinicDepartment.getId(), clinicDepartment);
        } else {
            throw new UnsupportedOperationException("Department with id " + clinicDepartment.getId() + " was not detected");
        }
        return clinicDepartment;
    }

    @Override
    public ClinicDepartment getClinicDepartmentById(Integer id) {
        return idToClinicDepartmentMap.get(id);
    }

    @Override
    public ClinicDepartment getClinicDepartmentByName(String name) {
        if (name == null) {
            return null;
        }
        for (ClinicDepartment currentClinicDepartment : idToClinicDepartmentMap.values()) {
            if (name.equals(currentClinicDepartment.getName())) {
                return currentClinicDepartment;
            }
        }
        return null;
    }

    @Override
    public Collection<ClinicDepartment> getAllClinicDepartments() {
        return idToClinicDepartmentMap.values();
    }
}
