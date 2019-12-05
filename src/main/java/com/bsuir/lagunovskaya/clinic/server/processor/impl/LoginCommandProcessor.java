package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.LoginServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

import java.util.List;

public class LoginCommandProcessor implements CommandProcessor {

    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;

    public LoginCommandProcessor() {
        patientDAO = DAOProvider.getPatientDAO();
        doctorDAO = DAOProvider.getDoctorDAO();
    }

    @Override
    public String getCommandNameToProcess() {
        return "login";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        List<String> commandParams = clientCommand.getCommandParams();
        String login = commandParams.get(0);
        String password = commandParams.get(1);

        Doctor doctorByLogin = doctorDAO.getDoctorByLogin(login);
        LoginServerResponse failedLoginServerResponse = new LoginServerResponse("failed");
        if (doctorByLogin != null) {
            if (password.equals(doctorByLogin.getPassword())) {
                return new LoginServerResponse("success", doctorByLogin);
            } else {
                return failedLoginServerResponse;
            }

        }
        Patient patientByLogin = patientDAO.getPatientByLogin(login);
        if (patientByLogin != null) {
            if (password.equals(patientByLogin.getPassword())) {
                return new LoginServerResponse("success", patientByLogin);
            } else {
                return failedLoginServerResponse;
            }
        }
        return failedLoginServerResponse;
    }
}
