package com.bsuir.lagunovskaya.clinic.server.dao.impl;

import com.bsuir.lagunovskaya.clinic.communication.entity.Appointment;
import com.bsuir.lagunovskaya.clinic.server.dao.AppointmentDAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryAppointmentDAO implements AppointmentDAO {

    private Map<Integer, Appointment> idToAppointmentMap = new HashMap<>();
    private Integer appointmentIdCounter = 1;

    private static InMemoryAppointmentDAO INSTANCE = null;

    private InMemoryAppointmentDAO() {
    }

    public static InMemoryAppointmentDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryAppointmentDAO();
        }
        return INSTANCE;
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        return idToAppointmentMap.get(id);
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        appointment.setId(appointmentIdCounter);
        idToAppointmentMap.put(appointment.getId(), appointment);
        appointmentIdCounter++;
        return appointment;
    }

    @Override
    public Collection<Appointment> getAllAppointments() {
        return idToAppointmentMap.values();
    }
}
