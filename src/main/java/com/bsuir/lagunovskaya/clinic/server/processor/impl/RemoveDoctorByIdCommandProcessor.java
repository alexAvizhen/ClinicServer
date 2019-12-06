package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.CommonServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Arrays;

public class RemoveDoctorByIdCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();

    @Override
    public String getCommandNameToProcess() {
        return "removeDoctorById";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        String doctorIdAsStr = clientCommand.getCommandParams().get(0);
        clinicService.removeDoctorById(Integer.valueOf(doctorIdAsStr));
        return new CommonServerResponse("removeDoctorById", Arrays.asList("success"));
    }
}
