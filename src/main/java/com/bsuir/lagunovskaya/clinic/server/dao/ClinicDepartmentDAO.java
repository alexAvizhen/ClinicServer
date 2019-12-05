package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;

import java.util.Collection;

public interface ClinicDepartmentDAO {

    ClinicDepartment createClinicDepartment(ClinicDepartment clinicDepartment);

    ClinicDepartment getClinicDepartmentById(Integer id);

    ClinicDepartment getClinicDepartmentByName(String name);

    Collection<ClinicDepartment> getAllClinicDepartments();
}
