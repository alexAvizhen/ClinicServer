package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.UserServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.User;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

public class GetUserByLoginCommandProcessor implements CommandProcessor {

    private DoctorDAO doctorDAO = DAOProvider.getDoctorDAO();
    private PatientDAO patientDAO = DAOProvider.getPatientDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getUserByLogin";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        User resultUser = null;
        String login = clientCommand.getCommandParams().get(0);
        Doctor doctorByLogin = doctorDAO.getDoctorByLogin(login);
        if (doctorByLogin != null) {
            resultUser = doctorByLogin;
        } else {
            resultUser = patientDAO.getPatientByLogin(login);
        }
        return new UserServerResponse(resultUser);
    }
}
