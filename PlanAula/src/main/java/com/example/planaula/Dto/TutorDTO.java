package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorDTO{
    private int id;
    private String nombre;
    private String apellidos;
    private String curso;
    private String anio;

    public TutorDTO(int id, String nombre, String apellidos, String curso, String anio) {
        this.id = id;
        this.curso = curso;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.anio = anio;
    }
    public TutorDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
}
