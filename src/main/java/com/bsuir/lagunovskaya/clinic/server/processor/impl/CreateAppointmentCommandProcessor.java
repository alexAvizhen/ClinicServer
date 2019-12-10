package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.command.CreateAppointmentClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.CommonServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Arrays;

public class CreateAppointmentCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();
    @Override
    public String getCommandNameToProcess() {
        return "createAppointment";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        CreateAppointmentClientCommand createAppointmentCommand = (CreateAppointmentClientCommand) clientCommand;
        clinicService.createAppointment(createAppointmentCommand.getDoctorLogin(), createAppointmentCommand.getPatientLogin(),
                createAppointmentCommand.getAppointmentDate(), createAppointmentCommand.getCommentToAppointment());
        return new CommonServerResponse("createAppointment", Arrays.asList("success"));
    }

}
