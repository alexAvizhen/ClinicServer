package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.response.ClinicDepartmentServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

public class GetClinicDepartmentByIdCommandProcessor implements CommandProcessor {

    private ClinicDepartmentDAO clinicDepartmentDAO = DAOProvider.getClinicDepartmentDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getClinicDepartmentById";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        Integer id = Integer.valueOf(clientCommand.getCommandParams().get(0));
        ClinicDepartment clinicDepartmentById = clinicDepartmentDAO.getClinicDepartmentById(id);
        return new ClinicDepartmentServerResponse(clinicDepartmentById);
    }
}
