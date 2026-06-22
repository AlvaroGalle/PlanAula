package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaDTO {
    private int id;
    private String nombre;

    public DiaDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public DiaDTO() {}

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
