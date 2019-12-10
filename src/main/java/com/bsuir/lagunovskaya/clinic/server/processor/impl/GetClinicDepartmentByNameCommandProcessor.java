package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.ClinicDepartmentServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

public class GetClinicDepartmentByNameCommandProcessor implements CommandProcessor {

    private ClinicDepartmentDAO clinicDepartmentDAO = DAOProvider.getClinicDepartmentDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getClinicDepartmentByName";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        String clinicDepartmentName = clientCommand.getCommandParams().iterator().next();
        ClinicDepartment clinicDepartment = clinicDepartmentDAO.getClinicDepartmentByName(clinicDepartmentName);
        return new ClinicDepartmentServerResponse(clinicDepartment);
    }
}
