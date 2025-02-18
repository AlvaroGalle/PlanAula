package com.example.planaula.Dto;

public class HorarioDTO {
    private int id;
    /*private String codigo;*/
    private String dia;
    private String hora;
    private String curso;
    private String asignatura1;
    private String profesor1;
    private String espacio1;

    private String asignatura2;
    private String profesor2;
    private String espacio2;

    private String asignatura3;
    private String profesor3;
    private String espacio3;

    private String observaciones;

    public HorarioDTO() {}

    /*String codigo,*/

    public HorarioDTO(int id, String dia, String hora, String curso, String asignatura1, String profesor1, String espacio1, String asignatura2, String profesor2, String espacio2, String asignatura3, String profesor3, String espacio3, String observaciones) {
        this.id = id;
        this.dia = dia;
        this.hora = hora;
        this.curso = curso;
        this.asignatura1 = asignatura1;
        this.profesor1 = profesor1;
        this.espacio1 = espacio1;
        this.asignatura2 = asignatura2;
        this.profesor2 = profesor2;
        this.espacio2 = espacio2;
        this.asignatura3 = asignatura3;
        this.profesor3 = profesor3;
        this.espacio3 = espacio3;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

/*    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }*/

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

    public String getAsignatura1() {
        return asignatura1;
    }

    public void setAsignatura1(String asignatura1) {
        this.asignatura1 = asignatura1;
    }

    public String getProfesor1() {
        return profesor1;
    }

    public void setProfesor1(String profesor1) {
        this.profesor1 = profesor1;
    }

    public String getEspacio1() {
        return espacio1;
    }

    public void setEspacio1(String espacio1) {
        this.espacio1 = espacio1;
    }

    public String getAsignatura2() {
        return asignatura2;
    }

    public void setAsignatura2(String asignatura2) {
        this.asignatura2 = asignatura2;
    }

    public String getProfesor2() {
        return profesor2;
    }

    public void setProfesor2(String profesor2) {
        this.profesor2 = profesor2;
    }

    public String getEspacio2() {
        return espacio2;
    }

    public void setEspacio2(String espacio2) {
        this.espacio2 = espacio2;
    }

    public String getAsignatura3() {
        return asignatura3;
    }

    public void setAsignatura3(String asignatura3) {
        this.asignatura3 = asignatura3;
    }

    public String getProfesor3() {
        return profesor3;
    }

    public void setProfesor3(String profesor3) {
        this.profesor3 = profesor3;
    }

    public String getEspacio3() {
        return espacio3;
    }

    public void setEspacio3(String espacio3) {
        this.espacio3 = espacio3;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
