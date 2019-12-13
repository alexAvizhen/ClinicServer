package com.bsuir.lagunovskaya.clinic.server;

import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
import com.bsuir.lagunovskaya.clinic.server.dao.ClinicDepartmentDAO;
import com.bsuir.lagunovskaya.clinic.server.dao.DAOProvider;
import com.bsuir.lagunovskaya.clinic.server.db.JDBCTemplate;
import com.bsuir.lagunovskaya.clinic.server.handler.ClinicClientHandler;
import com.bsuir.lagunovskaya.clinic.server.service.ClinicService;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClinicServer {

    public static Properties properties = new Properties();

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
        if (DAOProvider.usedDb) {
            if (Boolean.valueOf(properties.getProperty("init.db.on.server.start"))) {
                JDBCTemplate jdbcTemplate = new JDBCTemplate();
                jdbcTemplate.runScript("src/main/resources/initSqlScript.sql");
            }
        } else {
            Clinic clinic = clinicService.createClinic();
            ClinicDepartment clinicDepartmentOne = clinicService.createClinicDepartment(clinic, "Терапевтическое", new ArrayList<String>());

            Doctor rootDoctor = new Doctor("root", "root", clinicDepartmentOne.getId());
            clinicService.createOrUpdateDoctor(rootDoctor);
        }

    }

    private static Date generateDateByOld(int old) {
        return DateUtils.addYears(DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DATE), -old);
    }
}
