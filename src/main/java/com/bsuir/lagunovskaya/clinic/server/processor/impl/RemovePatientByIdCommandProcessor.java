package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.CommonServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Arrays;

public class RemovePatientByIdCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();

    @Override
    public String getCommandNameToProcess() {
        return "removePatientById";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        String patientIdAsStr = clientCommand.getCommandParams().get(0);
        clinicService.removePatientById(Integer.valueOf(patientIdAsStr));
        return new CommonServerResponse("removePatientById", Arrays.asList("success"));
    }
}
