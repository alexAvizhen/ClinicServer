package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.AllClinicDepartmentsServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;

import java.util.ArrayList;
import java.util.Collection;

public class GetAllClinicDepartmentsCommandProcessor implements CommandProcessor {

    private ClinicDepartmentDAO clinicDepartmentDAO = DAOProvider.getClinicDepartmentDAO();

    @Override
    public String getCommandNameToProcess() {
        return "getAllClinicDepartments";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        Collection<ClinicDepartment> allClinicDepartments = clinicDepartmentDAO.getAllClinicDepartments();
        return new AllClinicDepartmentsServerResponse(new ArrayList<>(allClinicDepartments));
    }
}
