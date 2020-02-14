package com.example.activosapp.ui;

public class TipoDocumento {
    private int id;

    public TipoDocumento(int id, String nombre) {
        this.id=id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;
}
