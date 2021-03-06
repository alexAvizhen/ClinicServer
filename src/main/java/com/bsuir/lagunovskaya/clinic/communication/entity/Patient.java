package com.bsuir.lagunovskaya.clinic.communication.entity;

import java.util.Objects;

public class Patient extends User {

    private Integer clinicDepartmentId;
    private String address;

    public Patient(String login, String password, Integer clinicDepartmentId) {
        super(null, login, password);
        this.clinicDepartmentId = clinicDepartmentId;
    }

    public Integer getClinicDepartmentId() {
        return clinicDepartmentId;
    }

    public void setClinicDepartmentId(Integer clinicDepartmentId) {
        this.clinicDepartmentId = clinicDepartmentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getClinicDepartmentId(), getAddress());
    }
}
