package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.UserServerResponse;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

public class GetPatientByIdCommandProcessor implements CommandProcessor {

    private PatientDAO patientDAO = DAOProvider.getPatientDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getPatientById";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        Integer id = Integer.valueOf(clientCommand.getCommandParams().get(0));
        Patient patientById = patientDAO.getPatientById(id);
        return new UserServerResponse(patientById);
    }
}
