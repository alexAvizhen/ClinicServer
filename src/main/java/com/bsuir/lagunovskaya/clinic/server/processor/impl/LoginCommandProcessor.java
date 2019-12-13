package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.entity.User;
import com.bsuir.lagunovskaya.clinic.communication.response.LoginServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.List;

public class LoginCommandProcessor implements CommandProcessor {

    private ClinicService clinicService;

    public LoginCommandProcessor() {
        clinicService = new ClinicService();
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

        User userByLogin = clinicService.getUserByLogin(login);
        if (userByLogin != null && password.equals(userByLogin.getPassword())) {
            return new LoginServerResponse("success", userByLogin);
        }
        return new LoginServerResponse("failed");
    }
}
