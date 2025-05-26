package com.example.planaula.Dto;

public class GuardiasDTO {
    private int id;
    private String codigo;
    private String dia;
    private String hora;
    private String nombre;
    private String apellidos;

    public GuardiasDTO() {
    }

    public GuardiasDTO(int id, String codigo, String dia, String hora, String nombre, String apellidos) {
        this.id = id;
        this.dia = dia;
        this.hora = hora;
        this.nombre = nombre;
        this.apellidos = apellidos;
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

    public void setCodigo(String id) {
        this.codigo = codigo;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {return hora;}

    public void setHora(String hora) {this.hora = hora;}

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
}
