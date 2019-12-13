package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.response.ClinicServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

public class GetClinicByIdCommandProcessor implements CommandProcessor {

    private ClinicDAO clinicDAO = DAOProvider.getClinicDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getClinicById";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        Integer id = Integer.valueOf(clientCommand.getCommandParams().get(0));
        Clinic clinicById = clinicDAO.getClinicById(id);
        return new ClinicServerResponse(clinicById);
    }
}
