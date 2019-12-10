package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.communication.entity.Appointment;

import java.util.Collection;

public interface AppointmentDAO {
    Appointment getAppointmentById(Integer id);

    Appointment createAppointment(Appointment appointment);

    Collection<Appointment> getAllAppointments();
}
