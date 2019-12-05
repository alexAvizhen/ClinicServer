package com.bsuir.lagunovskaya.clinic.server.handler;

import com.bsuir.lagunovskaya.clinic.communication.ClientCommand;
import com.bsuir.lagunovskaya.clinic.communication.ServerResponse;
import com.bsuir.lagunovskaya.clinic.server.service.ClientCommandsManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClinicClientHandler implements Runnable {


    private Socket clientSocket;
    private ClientCommandsManager clientCommandsManager;

    private ObjectInputStream in = null; // поток чтения из сокета
    private ObjectOutputStream out = null; // поток записи в сокет

    public ClinicClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.clientCommandsManager = new ClientCommandsManager();
    }

    public void run() {
        try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
            // к созданию потоков ввода/вывода.

            // отправлять
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            //принимать сообщения
            in = new ObjectInputStream(clientSocket.getInputStream());

            ServerResponse serverResponse = null;

            ClientCommand clientCommand = (ClientCommand) in.readObject(); // ждём пока клиент что-нибудь нам напишет
            System.out.println(clientCommand);
            serverResponse = clientCommandsManager.processCommand(clientCommand);
            System.out.println(serverResponse);
            out.writeObject(serverResponse);
            out.flush(); // выталкиваем все из буфера

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally { // в любом случае сокет будет закрыт
            try {
                clientSocket.close();
                // потоки тоже хорошо бы закрыть
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
