package com.bsuir.lagunovskaya.clinic.server.service;

import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.CreateOrUpdateDepartmentCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.CreateOrUpdateDoctorCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.CreateOrUpdatePatientCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.GetAllClinicDepartmentsCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.GetClinicDepartmentByNameCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.GetUserByLoginCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.LoginCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.RemoveDoctorByIdCommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.processor.impl.RemovePatientByIdCommandProcessor;

import java.util.ArrayList;
import java.util.List;

public class ClientCommandsManager {

    private List<CommandProcessor> commandProcessors;

    public ClientCommandsManager() {
        commandProcessors = new ArrayList<>();
        commandProcessors.add(new LoginCommandProcessor());
        commandProcessors.add(new GetAllClinicDepartmentsCommandProcessor());
        commandProcessors.add(new GetClinicDepartmentByNameCommandProcessor());
        commandProcessors.add(new GetUserByLoginCommandProcessor());
        commandProcessors.add(new CreateOrUpdateDepartmentCommandProcessor());
        commandProcessors.add(new CreateOrUpdateDoctorCommandProcessor());
        commandProcessors.add(new CreateOrUpdatePatientCommandProcessor());
        commandProcessors.add(new RemovePatientByIdCommandProcessor());
        commandProcessors.add(new RemoveDoctorByIdCommandProcessor());
    }

    public ServerResponse processCommand(ClientCommand clientCommand) {
        for (CommandProcessor commandProcessor : commandProcessors) {
            if (commandProcessor.getCommandNameToProcess().equals(clientCommand.getCommandName())) {
                return commandProcessor.processCommand(clientCommand);
            }
        }
        //don't detect command processor. Shouldn't go here
        throw new UnsupportedOperationException("Не нашёл обработчик команды от клиента");
    }
}
