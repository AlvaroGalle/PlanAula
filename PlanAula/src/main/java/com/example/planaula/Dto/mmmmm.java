package com.example.planaula.Dto;

public class mmmmm {
    private int id;
    private String codigo;
    private String dia;
    private String hora;
    private String asignatura;
    private String curso;
    private String espacio;
    private String profesor;
    private String sustituto;
    private String observaciones;

    public mmmmm() {}

    public mmmmm(int id, String codigo, String dia, String hora, String asignatura,
                   String curso, String espacio, String profesor,
                   String sustituto, String observaciones) {
        this.id = id;
        this.codigo = codigo;
        this.dia = dia;
        this.hora = hora;
        this.asignatura = asignatura;
        this.curso = curso;
        this.espacio = espacio;
        this.profesor = profesor;
        this.sustituto = sustituto;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEspacio() {
        return espacio;
    }

    public void setEspacio(String espacio) {
        this.espacio = espacio;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getSustituto() {
        return sustituto;
    }

    public void setSustituto(String sustituto) {
        this.sustituto = sustituto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
