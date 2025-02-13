package com.example.planaula.Dto;

public class CursoDTO {
    private int id;
    private String nombre;

    public CursoDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public CursoDTO() {}

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
