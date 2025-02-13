package com.example.planaula.Dto;

public class CodigoHorasDTO {
    private int id;
    private String codigo;
    private String dia;
    private String hora;

    public CodigoHorasDTO() {
    }

    public CodigoHorasDTO(int id, String codigo, String dia, String hora) {
        this.id = id;
        this.codigo = codigo;
        this.dia = dia;
        this.hora = hora;
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
}
