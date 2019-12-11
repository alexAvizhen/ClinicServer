package com.bsuir.lagunovskaya.clinic.server;

import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;
import com.bsuir.lagunovskaya.clinic.communication.entity.ClinicDepartment;
import com.bsuir.lagunovskaya.clinic.communication.entity.Doctor;
import com.bsuir.lagunovskaya.clinic.communication.entity.Patient;
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
        Boolean shouldInitDbOnServerStart = Boolean.valueOf(properties.getProperty("init.db.on.server.start"));
        if (shouldInitDbOnServerStart) {
            JDBCTemplate jdbcTemplate = new JDBCTemplate();
            jdbcTemplate.runScript("src/main/resources/initSqlScript.sql");
        }
        ClinicService clinicService = new ClinicService();
        Clinic clinic = clinicService.createClinic();
        ClinicDepartment clinicDepartmentOne = clinicService.createClinicDepartment(clinic, "Терапевтическое", Arrays.asList("Лидская", "Неманская", "Колесникова", "Кунцевщина"));
        ClinicDepartment clinicDepartmentTwo = clinicService.createClinicDepartment(clinic, "Неврологическое", new ArrayList<String>());
        ClinicDepartment clinicDepartmentThree = clinicService.createClinicDepartment(clinic, "Стоматологическое", new ArrayList<String>());
        ClinicDepartment clinicDepartmentFour = clinicService.createClinicDepartment(clinic, "Травмато-хирургическое", new ArrayList<String>());
        ClinicDepartment clinicDepartmentFive = clinicService.createClinicDepartment(clinic, "Регистратура", new ArrayList<String>());

        Doctor doctor = new Doctor("фрол", "фрол", clinicDepartmentOne);
        doctor.setPhoneNumber("3337922");
        doctor.setName("Альфред");
        doctor.setSurname("Фролов");
        doctor.setBirthDate(generateDateByOld(45));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("кудр", "кудр", clinicDepartmentOne);
        doctor.setPhoneNumber("3447922");
        doctor.setName("Артём");
        doctor.setSurname("Кудрявцев");
        doctor.setBirthDate(generateDateByOld(35));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("комисс", "комисс", clinicDepartmentTwo);
        doctor.setPhoneNumber("5647922");
        doctor.setName("Злата");
        doctor.setSurname("Комиссарова");
        doctor.setBirthDate(generateDateByOld(38));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("мурав", "мурав", clinicDepartmentTwo);
        doctor.setPhoneNumber("8647952");
        doctor.setName("Александр");
        doctor.setSurname("Муравьёв");
        doctor.setBirthDate(generateDateByOld(38));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("сухан", "сухан", clinicDepartmentThree);
        doctor.setPhoneNumber("8547352");
        doctor.setName("Валерий");
        doctor.setSurname("Суханов");
        doctor.setBirthDate(generateDateByOld(38));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("исак", "исак", clinicDepartmentThree);
        doctor.setPhoneNumber("1647452");
        doctor.setName("Влада");
        doctor.setSurname("Исакова");
        doctor.setBirthDate(generateDateByOld(48));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("каныг", "каныг", clinicDepartmentFour);
        doctor.setPhoneNumber("3747452");
        doctor.setName("Игнатий");
        doctor.setSurname("Каныгин");
        doctor.setBirthDate(generateDateByOld(58));
        clinicService.createOrUpdateDoctor(doctor);

        doctor = new Doctor("петух", "петух", clinicDepartmentFive);
        doctor.setPhoneNumber("3723452");
        doctor.setName("Мария");
        doctor.setSurname("Петухова");
        doctor.setBirthDate(generateDateByOld(28));
        clinicService.createOrUpdateDoctor(doctor);

        Patient patient = new Patient("кулик", "кулик", clinicDepartmentOne);
        patient.setPhoneNumber("33234567");
        patient.setName("Василий");
        patient.setSurname("Куликов");
        patient.setBirthDate(generateDateByOld(34));
        patient.setAddress("Лещинского, 16");
        clinicService.createOrUpdatePatient(patient);

        patient = new Patient("русак", "русак", clinicDepartmentOne);
        patient.setPhoneNumber("22234567");
        patient.setName("Давид");
        patient.setSurname("Русаков");
        patient.setBirthDate(generateDateByOld(37));
        patient.setAddress("Неманская, 91");
        clinicService.createOrUpdatePatient(patient);

        Doctor rootDoctor = new Doctor("root", "root", clinicDepartmentOne);
        clinicService.createOrUpdateDoctor(rootDoctor);
    }

    private static Date generateDateByOld(int old) {
        return DateUtils.addYears(DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DATE), -old);
    }
}
