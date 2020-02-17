package com.example.appballoon.Class;


public class Passenger {
    private int id;
    private String name;
    private String surname;
    private String dni;
    private String terms;

    public Passenger(int id, String name, String surname, String dni, String terms) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dni = dni;
        this.terms= terms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }


    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
