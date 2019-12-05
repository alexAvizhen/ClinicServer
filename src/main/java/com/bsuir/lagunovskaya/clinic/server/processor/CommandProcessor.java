package com.bsuir.lagunovskaya.clinic.server.processor;

import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;

public interface CommandProcessor {

    String getCommandNameToProcess();

    ServerResponse processCommand(ClientCommand clientCommand);
}
