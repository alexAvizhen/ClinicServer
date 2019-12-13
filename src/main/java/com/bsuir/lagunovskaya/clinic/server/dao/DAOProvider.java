package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.server.ClinicServer;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.DataBaseClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.DateBaseAppointmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.DateBaseClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.DateBaseDoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.DateBasePatientDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryAppointmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryDoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.impl.InMemoryPatientDAO;

public class DAOProvider {
    public static boolean usedDb = Boolean.parseBoolean(ClinicServer.properties.getProperty("use.db"));

    public static DoctorDAO getDoctorDAO() {
        if (DAOProvider.usedDb) {
            return DateBaseDoctorDAO.getInstance();
        } else {
            return InMemoryDoctorDAO.getInstance();
        }
    }

    public static PatientDAO getPatientDAO() {
        if (DAOProvider.usedDb) {
            return DateBasePatientDAO.getInstance();
        } else {
            return InMemoryPatientDAO.getInstance();
        }
    }

    public static ClinicDAO getClinicDAO() {
        if (usedDb) {
            return DataBaseClinicDAO.getInstance();
        } else {
            return InMemoryClinicDAO.getInstance();
        }
    }

    public static ClinicDepartmentDAO getClinicDepartmentDAO() {
        if (usedDb) {
            return DateBaseClinicDepartmentDAO.getInstance();
        } else {
            return InMemoryClinicDepartmentDAO.getInstance();
        }
    }

    public static AppointmentDAO getAppointmentDAO() {
        if (usedDb) {
            return DateBaseAppointmentDAO.getInstance();
        } else {
            return InMemoryAppointmentDAO.getInstance();
        }
    }
}
