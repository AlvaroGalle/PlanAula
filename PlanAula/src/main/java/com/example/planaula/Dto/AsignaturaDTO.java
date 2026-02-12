package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AsignaturaDTO {
    private int id;
    private String nombre;

    public AsignaturaDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public AsignaturaDTO() {}

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
