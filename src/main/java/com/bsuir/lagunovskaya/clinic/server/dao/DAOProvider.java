package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryAppointmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryDoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryPatientDAO;

public class DAOProvider {

    public static DoctorDAO getDoctorDAO() {
        return InMemoryDoctorDAO.getInstance();
    }

    public static PatientDAO getPatientDAO() {
        return InMemoryPatientDAO.getInstance();
    }

    public static ClinicDAO getClinicDAO() {
        return InMemoryClinicDAO.getInstance();
    }

    public static ClinicDepartmentDAO getClinicDepartmentDAO() {
        return InMemoryClinicDepartmentDAO.getInstance();
    }

    public static AppointmentDAO getAppointmentDAO() {
        return InMemoryAppointmentDAO.getInstance();
    }
}
