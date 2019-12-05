package com.bsuir.lagunovskaya.clinic.server.service;

import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;

import java.util.List;

public class ClinicService {

    private boolean isClinicCreated = false;
    private ClinicDAO clinicDAO = DAOProvider.getClinicDAO();
    private ClinicDepartmentDAO clinicDepartmentDAO = DAOProvider.getClinicDepartmentDAO();
    private DoctorDAO doctorDAO = DAOProvider.getDoctorDAO();
    private PatientDAO patientDAO = DAOProvider.getPatientDAO();


    public Clinic createClinic() {
        if (!isClinicCreated) {
            Clinic clinic = new Clinic("Lidskaya, 14", "Полкиклинника на каменной горке");
            return clinicDAO.createClinic(clinic);
        }
        return clinicDAO.getClinicById(1);
    }

    public ClinicDepartment createClinicDepartment(Clinic clinic, String departmentName, List<String> streets) {
        ClinicDepartment clinicDepartment = new ClinicDepartment(clinic, departmentName, streets);
        ClinicDepartment createdClinicDep = clinicDepartmentDAO.createClinicDepartment(clinicDepartment);
        clinic.getClinicDepartments().add(createdClinicDep);
        return createdClinicDep;
    }

    public Doctor createDoctor(Integer index, ClinicDepartment clinicDepartment) {
        String testStr = "test_doc" + index;
        Doctor doctor = new Doctor(testStr, testStr, clinicDepartment);
        Doctor createdDoctor = doctorDAO.createDoctor(doctor);
        clinicDepartment.getDoctors().add(createdDoctor);
        return createdDoctor;
    }

    public Patient createPatient(Integer index, ClinicDepartment clinicDepartment) {
        String testStr = "test_pat" + index;
        Patient patient = new Patient(testStr, testStr, clinicDepartment);
        Patient createdPatient = patientDAO.createPatient(patient);
        clinicDepartment.getPatients().add(createdPatient);
        return createdPatient;
    }

    public Doctor createDoctor(ClinicDepartment clinicDepartment) {
        String testStr = "t";
        Doctor doctor = new Doctor(testStr, testStr, clinicDepartment);
        Doctor createdDoctor = doctorDAO.createDoctor(doctor);
        clinicDepartment.getDoctors().add(createdDoctor);
        return createdDoctor;
    }
}