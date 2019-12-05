package com.bsuir.lagunovskaya.clinic.server.dao;

import com.bsuir.lagunovskaya.clinic.communication.entity.Clinic;

public interface ClinicDAO {

    Clinic createClinic(Clinic clinic);

    Clinic getClinicById(Integer id);

    Clinic updateClinic(Clinic clinic);
}
