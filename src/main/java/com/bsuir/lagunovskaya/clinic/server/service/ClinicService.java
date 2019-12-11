package com.bsuir.lagunovskaya.clinic.server.service;

import com.bsuir.lagunovskaya.clinic.communication.entity.Appointment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.dao.AppointmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClinicService {

    private boolean isClinicCreated = false;
    private ClinicDAO clinicDAO = DAOProvider.getClinicDAO();
    private ClinicDepartmentDAO clinicDepartmentDAO = DAOProvider.getClinicDepartmentDAO();
    private DoctorDAO doctorDAO = DAOProvider.getDoctorDAO();
    private PatientDAO patientDAO = DAOProvider.getPatientDAO();
    private AppointmentDAO appointmentDAO = DAOProvider.getAppointmentDAO();


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

    public void createOrUpdateDepartment(ClinicDepartment clinicDepartment) {
        if (clinicDepartment.getId() == null) {
            clinicDepartmentDAO.createClinicDepartment(clinicDepartment);
        } else {
            clinicDepartmentDAO.updateClinicDepartment(clinicDepartment);
        }
    }

    public void createOrUpdateDoctor(Doctor doctor) {
        if (doctor.getId() == null) {
            createDoctor(doctor);
        } else {
            updateDoctor(doctor);
        }
    }

    private void createDoctor(Doctor doctor) {
        doctorDAO.createDoctor(doctor);
        ClinicDepartment doctorClinicDepartment = doctor.getClinicDepartment();
        doctorClinicDepartment.getDoctors().add(doctor);

        clinicDepartmentDAO.updateClinicDepartment(doctorClinicDepartment);
    }

    private void updateDoctor(Doctor doctor) {
        Doctor oldDoctor = doctorDAO.getDoctorById(doctor.getId());
        Integer oldDoctorClinicDepartmentId = oldDoctor.getClinicDepartment().getId();
        ClinicDepartment oldDoctorClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(oldDoctorClinicDepartmentId);
        oldDoctorClinicDepartment.getDoctors().remove(oldDoctor);
        clinicDepartmentDAO.updateClinicDepartment(oldDoctorClinicDepartment);
        doctorDAO.updateDoctor(doctor);
        Integer newDoctorClinicDepartmentId = doctor.getClinicDepartment().getId();
        ClinicDepartment newDoctorClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(newDoctorClinicDepartmentId);
        newDoctorClinicDepartment.getDoctors().add(doctor);
        clinicDepartmentDAO.updateClinicDepartment(newDoctorClinicDepartment);

    }

    public void createOrUpdatePatient(Patient patient) {
        if (patient.getId() == null) {
            createPatient(patient);
        } else {
            updatePatient(patient);
        }
    }

    private void updatePatient(Patient patient) {
        Patient oldPatient = patientDAO.getPatientById(patient.getId());
        Integer oldPatientClinicDepartmentId = oldPatient.getClinicDepartment().getId();
        ClinicDepartment oldPatientClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(oldPatientClinicDepartmentId);
        oldPatientClinicDepartment.getPatients().remove(oldPatient);
        clinicDepartmentDAO.updateClinicDepartment(oldPatientClinicDepartment);
        patientDAO.updatePatient(patient);
        Integer newPatientClinicDepartmentId = patient.getClinicDepartment().getId();
        ClinicDepartment newPatientClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(newPatientClinicDepartmentId);
        newPatientClinicDepartment.getPatients().add(patient);
        clinicDepartmentDAO.updateClinicDepartment(newPatientClinicDepartment);
    }

    private void createPatient(Patient patient) {
        patientDAO.createPatient(patient);
        ClinicDepartment patientClinicDepartment = patient.getClinicDepartment();
        patientClinicDepartment.getPatients().add(patient);

        clinicDepartmentDAO.updateClinicDepartment(patientClinicDepartment);
    }

    public void removeDoctorById(Integer doctorId) {
        Doctor doctorById = doctorDAO.getDoctorById(doctorId);
        if (doctorById != null) {
            ClinicDepartment doctorClinicDepartment = doctorById.getClinicDepartment();
            doctorClinicDepartment.getDoctors().remove(doctorById);
            clinicDepartmentDAO.updateClinicDepartment(doctorClinicDepartment);
            doctorDAO.deleteDoctorById(doctorById.getId());
        }
    }

    public void removePatientById(Integer patientId) {
        Patient patientById = patientDAO.getPatientById(patientId);
        if (patientById != null) {
            ClinicDepartment patientClinicDepartment = patientById.getClinicDepartment();
            patientClinicDepartment.getPatients().remove(patientById);
            clinicDepartmentDAO.updateClinicDepartment(patientClinicDepartment);
            patientDAO.deletePatientById(patientById.getId());
        }
    }

    public void createAppointment(String doctorLogin, String patientLogin, Date appointmentDate, String commentToAppointment) {
        Doctor doctorByLogin = doctorDAO.getDoctorByLogin(doctorLogin);
        Patient patientByLogin = patientDAO.getPatientByLogin(patientLogin);
        Appointment newAppointment = new Appointment(doctorByLogin, patientByLogin, appointmentDate, commentToAppointment);
        appointmentDAO.createAppointment(newAppointment);
    }

    public Collection<Appointment> getAppointmentsByUserLogin(String userLogin) {
        List<Appointment> resultAppointments = new ArrayList<>();
        for (Appointment appointment : appointmentDAO.getAllAppointments()) {
            if (userLogin.equals(appointment.getDoctor().getLogin()) || userLogin.equals(appointment.getPatient().getLogin())) {
                resultAppointments.add(appointment);
            }
        }

        return resultAppointments;
    }

    public Map<String, Integer> getDateAsStrToAmountOfAppointmentsStats(Date startDate, Date endDate) {
        Map<String, Integer> dateAsStrToAmountMap = new HashMap<>();
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        for (Appointment appointment : appointmentDAO.getAllAppointments()) {
            if (startDate.before(appointment.getAppointmentDate()) && endDate.after(appointment.getAppointmentDate())) {
                String dateAsStr = outputFormatter.format(appointment.getAppointmentDate());
                Integer currentAmount = dateAsStrToAmountMap.get(dateAsStr);
                if (currentAmount == null) {
                    currentAmount = 0;
                }
                currentAmount++;
                dateAsStrToAmountMap.put(dateAsStr, currentAmount);
            }
        }
        return dateAsStrToAmountMap;
    }
}
