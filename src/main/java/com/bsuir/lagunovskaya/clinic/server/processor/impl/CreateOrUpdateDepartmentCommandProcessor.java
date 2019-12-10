package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.CommonServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.command.CreateOrUpdateDepartmentClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Arrays;

public class CreateOrUpdateDepartmentCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();

    @Override
    public String getCommandNameToProcess() {
        return "createOrUpdateDepartment";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        CreateOrUpdateDepartmentClientCommand createOrUpdateDepartmentClientCommand =
                (CreateOrUpdateDepartmentClientCommand) clientCommand;
        ClinicDepartment clinicDepartment = createOrUpdateDepartmentClientCommand.getClinicDepartment();
        clinicService.createOrUpdateDepartment(clinicDepartment);
        return new CommonServerResponse("createOrUpdateDepartment", Arrays.asList("success"));
    }
}
