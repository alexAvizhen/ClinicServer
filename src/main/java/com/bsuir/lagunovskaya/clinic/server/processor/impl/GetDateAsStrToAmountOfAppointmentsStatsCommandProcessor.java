package com.bsuir.lagunovskaya.clinic.server.processor.impl;

import com.bsuir.lagunovskaya.clinic.communication.command.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.command.GetDateAsStrToAmountOfAppointmentsStatsClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.response.DateAsStrToAmountOfAppointmentsStatsServerResponse;
import com.bsuir.lagunovskaya.clinic.communication.response.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.processor.CommandProcessor;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.util.Map;

public class GetDateAsStrToAmountOfAppointmentsStatsCommandProcessor implements CommandProcessor {

    private ClinicService clinicService = new ClinicService();

    @Override
    public String getCommandNameToProcess() {
        return "getDateAsStrToAmountOfAppointmentsStats";
    }

    @Override
    public ServerResponse processCommand(ClientCommand clientCommand) {
        GetDateAsStrToAmountOfAppointmentsStatsClientCommand getGroupedAppointmentsStatsCommand =
                ((GetDateAsStrToAmountOfAppointmentsStatsClientCommand) clientCommand);

        Map<String, Integer> dateAsStrToAmountOfAppointmentsStats = clinicService.getDateAsStrToAmountOfAppointmentsStats(
                getGroupedAppointmentsStatsCommand.getStartDate(), getGroupedAppointmentsStatsCommand.getEndDate());

        return new DateAsStrToAmountOfAppointmentsStatsServerResponse(dateAsStrToAmountOfAppointmentsStats);
    }
}
