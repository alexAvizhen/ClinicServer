package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.AllClinicDepartmentsServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.UserServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.communication.entity.User;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.dao.DoctorDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.PatientDAO;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

import java.util.ArrayList;
import java.util.Collection;

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
