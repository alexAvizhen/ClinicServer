package com.bsuir.lagunovskaya.clinic.server;

import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.handler.ClinicClientHandler;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClinicServer {

    private static Properties properties = new Properties();

    public static void main(String[] args) {
        initProperties();
        initDataBase();
        ExecutorService clintExecutorService = Executors.newFixedThreadPool(Integer.valueOf(properties.getProperty("amount.of.request.handlers")));
        ServerSocket server = null;
        BufferedReader br = null;
// стартуем сервер на порту 3345 и инициализируем переменную для обработки консольных команд с самого сервера
        try {
            server = new ServerSocket(Integer.valueOf(properties.getProperty("server.port")));
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

    private static void initProperties() {
        FileInputStream fis;

        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);

        } catch (IOException e) {
            System.err.println("Error during initializing property file");
        }
    }

    private static void initDataBase() {
        ClinicService clinicService = new ClinicService();
        Clinic clinic = clinicService.createClinic();
        ClinicDepartment clinicDepartmentOne = clinicService.createClinicDepartment(clinic, "Терапевтическое 1", Arrays.asList("Лидская", "Неманская"));
        ClinicDepartment clinicDepartmentTwo = clinicService.createClinicDepartment(clinic, "Терапевтическое 2", Arrays.asList("Лидская2", "Неманская2"));
        for (int i = 0; i < 15; i++) {
            clinicService.createOrUpdateDoctor(generateDoctor(clinicDepartmentOne));
            clinicService.createOrUpdateDoctor(generateDoctor(clinicDepartmentTwo));
            clinicService.createOrUpdatePatient(generatePatient(clinicDepartmentOne));
            clinicService.createOrUpdatePatient(generatePatient(clinicDepartmentTwo));
        }

        Doctor doctor = new Doctor("t", "t", clinicDepartmentOne);
        clinicService.createOrUpdateDoctor(doctor);
    }

    private static Integer doctorsCounter = 1;
    private static Doctor generateDoctor(ClinicDepartment clinicDepartment) {
        String testStr = "Доктор" + doctorsCounter;
        doctorsCounter++;
        Doctor doctor = new Doctor(testStr, testStr, clinicDepartment);
        doctor.setPhoneNumber("1234567");
        doctor.setName(testStr);
        doctor.setSurname(testStr);
        doctor.setBirthDate(new Date());
        return doctor;
    }

    private static Integer patientsCounter = 1;
    private static Patient generatePatient(ClinicDepartment clinicDepartment) {
        String testStr = "Пациент" + patientsCounter;
        patientsCounter++;
        Patient patient = new Patient(testStr, testStr, clinicDepartment);
        patient.setPhoneNumber("1234567");
        patient.setName(testStr);
        patient.setSurname(testStr);
        patient.setBirthDate(new Date());
        patient.setAddress(testStr);
        return patient;
    }
}
