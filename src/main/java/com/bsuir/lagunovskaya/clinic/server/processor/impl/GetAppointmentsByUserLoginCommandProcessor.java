package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.entity.Appointment;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.UserAppointmentsServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Collection;

public class GetAppointmentsByUserLoginCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();
    @Override
    public String getCommandNameToProcess() {
        return "getAppointmentsByUserLogin";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        Collection<Appointment> appointmentsByUserLogin = clinicService.getAppointmentsByUserLogin(clientCommand.getCommandParams().get(0));
        return new UserAppointmentsServerResponse(appointmentsByUserLogin);
    }

}
