package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AulaDTO {
    private int id;
    private String nombre;

    public AulaDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public AulaDTO() {}

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
