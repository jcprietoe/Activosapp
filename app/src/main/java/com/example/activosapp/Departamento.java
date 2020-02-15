package com.example.activosapp;

public class Departamento {private int id;
    private String nombre;


    public Departamento(int id, String nombre){
        this.setIdDepartamento(id);
        this.setNombreDepartamento(nombre);
    }

    public int getIdMunicipio(){
        return id;
    }
    public void setIdDepartamento(int id){
        this.id = id;
    }
    public String getNombreDepartamento(){
        return nombre;
    }
    public void setNombreDepartamento(String nombre){
        this.nombre = nombre;
    }
}