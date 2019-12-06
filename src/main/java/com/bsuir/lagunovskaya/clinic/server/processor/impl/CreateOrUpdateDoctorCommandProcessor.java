package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.CommonServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.CreateOrUpdateDoctorClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Arrays;

public class CreateOrUpdateDoctorCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();

    @Override
    public String getCommandNameToProcess() {
        return "createOrUpdateDoctor";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        CreateOrUpdateDoctorClientCommand createOrUpdateDoctorClientCommand =
                (CreateOrUpdateDoctorClientCommand) clientCommand;
        Doctor doctor = createOrUpdateDoctorClientCommand.getDoctor();
        clinicService.createOrUpdateDoctor(doctor);
        return new CommonServerResponse("createOrUpdateDoctor", Arrays.asList("success"));
    }
}
