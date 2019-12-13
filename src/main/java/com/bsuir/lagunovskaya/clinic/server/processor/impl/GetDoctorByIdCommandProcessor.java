package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.UserServerResponse;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

public class GetDoctorByIdCommandProcessor implements CommandProcessor {

    private DoctorDAO doctorDAO = DAOProvider.getDoctorDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getDoctorById";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        Integer id = Integer.valueOf(clientCommand.getCommandParams().get(0));
        Doctor doctorById = doctorDAO.getDoctorById(id);
        return new UserServerResponse(doctorById);
    }
}
