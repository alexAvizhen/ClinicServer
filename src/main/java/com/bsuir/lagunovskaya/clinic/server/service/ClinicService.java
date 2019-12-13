package com.bsuir.lagunovskaya.clinic.server.service;

import com.bsuir.lagunovskaya.clinic.communication.entity.Appointment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.communication.entity.User;
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
            Clinic clinic = new Clinic("Лидская, 14", "Полкиклинника на каменной горке");
            return clinicDAO.createClinic(clinic);
        }
        return clinicDAO.getClinicById(1);
    }

    public ClinicDepartment createClinicDepartment(Clinic clinic, String departmentName, List<String> streets) {
        ClinicDepartment clinicDepartment = new ClinicDepartment(clinic.getId(), departmentName, streets);
        ClinicDepartment createdClinicDep = clinicDepartmentDAO.createClinicDepartment(clinicDepartment);
        clinic.getClinicDepartmentIds().add(createdClinicDep.getId());
        clinicDAO.updateClinic(clinic);
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
        Doctor createdDoctor = doctorDAO.createDoctor(doctor);
        Integer clinicDepartmentId = doctor.getClinicDepartmentId();
        ClinicDepartment doctorClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(clinicDepartmentId);
        doctorClinicDepartment.getDoctorIds().add(createdDoctor.getId());

        clinicDepartmentDAO.updateClinicDepartment(doctorClinicDepartment);
    }

    private void updateDoctor(Doctor doctor) {
        Doctor oldDoctor = doctorDAO.getDoctorById(doctor.getId());
        Integer oldDoctorClinicDepartmentId = oldDoctor.getClinicDepartmentId();
        ClinicDepartment oldDoctorClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(oldDoctorClinicDepartmentId);
        oldDoctorClinicDepartment.getDoctorIds().remove(oldDoctor.getId());
        clinicDepartmentDAO.updateClinicDepartment(oldDoctorClinicDepartment);
        doctorDAO.updateDoctor(doctor);
        Integer newDoctorClinicDepartmentId = doctor.getClinicDepartmentId();
        ClinicDepartment newDoctorClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(newDoctorClinicDepartmentId);
        newDoctorClinicDepartment.getDoctorIds().add(doctor.getId());
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
        Integer oldPatientClinicDepartmentId = oldPatient.getClinicDepartmentId();
        ClinicDepartment oldPatientClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(oldPatientClinicDepartmentId);
        oldPatientClinicDepartment.getPatientIds().remove(oldPatient.getId());
        clinicDepartmentDAO.updateClinicDepartment(oldPatientClinicDepartment);
        patientDAO.updatePatient(patient);
        Integer newPatientClinicDepartmentId = patient.getClinicDepartmentId();
        ClinicDepartment newPatientClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(newPatientClinicDepartmentId);
        newPatientClinicDepartment.getPatientIds().add(patient.getId());
        clinicDepartmentDAO.updateClinicDepartment(newPatientClinicDepartment);
    }

    private void createPatient(Patient patient) {
        patientDAO.createPatient(patient);
        Integer clinicDepartmentId = patient.getClinicDepartmentId();
        ClinicDepartment patientClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(clinicDepartmentId);
        patientClinicDepartment.getPatientIds().add(patient.getId());

        clinicDepartmentDAO.updateClinicDepartment(patientClinicDepartment);
    }

    public void removeDoctorById(Integer doctorId) {
        Doctor doctorById = doctorDAO.getDoctorById(doctorId);
        if (doctorById != null) {
            Integer clinicDepartmentId = doctorById.getClinicDepartmentId();
            ClinicDepartment doctorClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(clinicDepartmentId);
            doctorClinicDepartment.getDoctorIds().remove(doctorById.getId());
            clinicDepartmentDAO.updateClinicDepartment(doctorClinicDepartment);
            doctorDAO.deleteDoctorById(doctorById.getId());
        }
    }

    public void removePatientById(Integer patientId) {
        Patient patientById = patientDAO.getPatientById(patientId);
        if (patientById != null) {
            Integer clinicDepartmentId = patientById.getClinicDepartmentId();
            ClinicDepartment patientClinicDepartment = clinicDepartmentDAO.getClinicDepartmentById(clinicDepartmentId);
            patientClinicDepartment.getPatientIds().remove(patientById.getId());
            clinicDepartmentDAO.updateClinicDepartment(patientClinicDepartment);
            patientDAO.deletePatientById(patientById.getId());
        }
    }

    public void createAppointment(String doctorLogin, String patientLogin, Date appointmentDate, String commentToAppointment) {
        Doctor doctorByLogin = doctorDAO.getDoctorByLogin(doctorLogin);
        Patient patientByLogin = patientDAO.getPatientByLogin(patientLogin);
        Appointment newAppointment = new Appointment(doctorByLogin.getId(), patientByLogin.getId(), appointmentDate, commentToAppointment);
        appointmentDAO.createAppointment(newAppointment);
    }

    public User getUserByLogin(String userLogin) {
        Doctor doctorByLogin = doctorDAO.getDoctorByLogin(userLogin);
        if (doctorByLogin != null) {
            return doctorByLogin;
        } else {
            return patientDAO.getPatientByLogin(userLogin);
        }
    }

    public Collection<Appointment> getAppointmentsByUserLogin(String userLogin) {
        User userByLogin = getUserByLogin(userLogin);
        if (userByLogin == null) {
            return new ArrayList<>();
        }
        Integer userLoginId = userByLogin.getId();
        List<Appointment> resultAppointments = new ArrayList<>();
        for (Appointment appointment : appointmentDAO.getAllAppointments()) {
            if (userLoginId.equals(appointment.getDoctorId()) || userLoginId.equals(appointment.getPatientId())) {
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
