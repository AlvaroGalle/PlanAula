package com.example.planaula.Dto;

public class ProfesorDTO {
    private int id;
    private String nombre;

    public ProfesorDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public ProfesorDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
