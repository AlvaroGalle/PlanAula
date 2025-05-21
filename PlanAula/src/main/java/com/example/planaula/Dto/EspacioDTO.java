package com.example.planaula.Dto;

public class EspacioDTO {
    private int id;
    private String dia;
    private String hora;
    private String curso;
    private String asignatura;
    private String nombre;
    private String aula;
    private String observaciones;


    public EspacioDTO() {}

    public EspacioDTO(int id, String dia, String hora, String curso, String asignatura, String nombre, String aula, String observaciones) {
        this.id = id;
        this.dia = dia;
        this.hora = hora;
        this.curso = curso;
        this.asignatura = asignatura;
        this.nombre = nombre;
        this.aula = aula;
        this.observaciones = observaciones;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
