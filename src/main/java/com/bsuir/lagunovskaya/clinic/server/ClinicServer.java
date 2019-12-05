package com.bsuir.lagunovskaya.clinic.server;

import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.server.handler.ClinicClientHandler;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClinicServer {

    private static ExecutorService clintExecutorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        initDataBase();
        ServerSocket server = null;
        BufferedReader br = null;
// стартуем сервер на порту 3345 и инициализируем переменную для обработки консольных команд с самого сервера
        try {
            server = new ServerSocket(3345);
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Сервер запущен. Ожидание клиентов");
            System.out.println("Для остановки сервера введите quit. " +
                    "После обработки последнеего клиента сервер будет остановлен");

            // стартуем цикл при условии что серверный сокет не закрыт
            while (!server.isClosed()) {

                // проверяем поступившие комманды из консоли сервера если такие
                // были
                if (br.ready()) {
                    // если команда - quit то инициализируем закрытие сервера и
                    // выход из цикла раздачии нитей монопоточных серверов
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Остановка сервера...");
                        server.close();
                        break;
                    }
                }
                // если комманд от сервера нет то становимся в ожидание
                // подключения к сокету общения под именем - "clientDialog" на
                // серверной стороне
                Socket client = server.accept();

                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                // монопоточную нить = сервер - MonoThreadClientHandler и тот
                // продолжает общение от лица сервера
                clintExecutorService.execute(new ClinicClientHandler(client));
                System.out.print("Клиент принят в обработку.\n");
            }

            // закрытие пула нитей после завершения работы всех нитей
            clintExecutorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void initDataBase() {
        ClinicService clinicService = new ClinicService();
        Clinic clinic = clinicService.createClinic();
        ClinicDepartment clinicDepartmentOne = clinicService.createClinicDepartment(clinic, "Терапевтическое 1", Arrays.asList("Лидская", "Неманская"));
        ClinicDepartment clinicDepartmentTwo = clinicService.createClinicDepartment(clinic, "Терапевтическое 2", Arrays.asList("Лидская2", "Неманская2"));
        clinicService.createDoctor(1, clinicDepartmentOne);
        clinicService.createDoctor(2, clinicDepartmentOne);
        clinicService.createDoctor(3, clinicDepartmentOne);
        clinicService.createPatient(1, clinicDepartmentOne);
        clinicService.createPatient(2, clinicDepartmentOne);
        clinicService.createPatient(3, clinicDepartmentOne);

        clinicService.createDoctor(4, clinicDepartmentTwo);
        clinicService.createDoctor(5, clinicDepartmentTwo);
        clinicService.createDoctor(6, clinicDepartmentTwo);
        clinicService.createPatient(4, clinicDepartmentTwo);
        clinicService.createPatient(5, clinicDepartmentTwo);
        clinicService.createPatient(6, clinicDepartmentTwo);

        clinicService.createDoctor(clinicDepartmentOne);
    }
}
