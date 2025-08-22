package com.example.planaula.Dto;

public class TurnoDTO {
    private int id;
    private String tipo;
    private String dia;
    private String hora;
    private String nombre;
    private String apellidos;

    public TurnoDTO() {
    }

    public TurnoDTO(String tipo, int id, String dia, String hora, String nombre, String apellidos) {
        this.id = id;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
