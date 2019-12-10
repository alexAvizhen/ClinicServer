package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.CommonServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.command.CreateOrUpdatePatientClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Arrays;

public class CreateOrUpdatePatientCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();

    @Override
    public String getCommandNameToProcess() {
        return "createOrUpdatePatient";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        CreateOrUpdatePatientClientCommand createOrUpdatePatientClientCommand =
                (CreateOrUpdatePatientClientCommand) clientCommand;
        Patient patient = createOrUpdatePatientClientCommand.getPatient();
        clinicService.createOrUpdatePatient(patient);
        return new CommonServerResponse("createOrUpdatePatient", Arrays.asList("success"));
    }
}
