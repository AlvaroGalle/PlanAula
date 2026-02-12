package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfesorDTO {
    private int id;
    private String nombre;
    private String apellidos;
    private boolean tutor;

    public ProfesorDTO() {}
    public ProfesorDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public ProfesorDTO(int id, String nombre, String apellidos, boolean tutor) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tutor = tutor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter y Setter para nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter para apellidos
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    // Getter y Setter para tutor
    public boolean isTutor() {
        return tutor;
    }

    public void setTutor(boolean tutor) {
        this.tutor = tutor;
    }
}
