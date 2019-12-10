package com.bsuir.lagunovskaya.clinic.server.processor;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;

public interface CommandProcessor {

    String getCommandNameToProcess();

    ServerResponse processCommand(ClientCommand clientCommand);
}
