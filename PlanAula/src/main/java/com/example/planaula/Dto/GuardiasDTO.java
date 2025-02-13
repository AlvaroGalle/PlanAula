package com.example.planaula.Dto;

public class GuardiasDTO {
    private int id;
    private String codigo;
    private String dia;
    private String hora;
    private String guardia1;
    private String guardia2;
    private String guardia3;
    private String libranza1;
    private String libranza2;
    private String libranza3;
    private String recreo1;
    private String recreo2;
    private String recreo3;

    public GuardiasDTO() {
    }

    public GuardiasDTO(int id, String codigo, String dia, String hora, String guardia1, String guardia2, String guardia3, String libranza1, String libranza2, String libranza3, String recreo1, String recreo2, String recreo3) {
        this.id = id;
        this.codigo = codigo;
        this.dia = dia;
        this.hora = hora;
        this.guardia1 = guardia1;
        this.guardia2 = guardia2;
        this.guardia3 = guardia3;
        this.libranza1 = libranza1;
        this.libranza2 = libranza2;
        this.libranza3 = libranza3;
        this.recreo1 = recreo1;
        this.recreo2 = recreo2;
        this.recreo3 = recreo3;
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

    public String getGuardia1() {
        return guardia1;
    }

    public void setGuardia1(String guardia1) {
        this.guardia1 = guardia1;
    }

    public String getGuardia2() {
        return guardia2;
    }

    public void setGuardia2(String guardia2) {
        this.guardia2 = guardia2;
    }

    public String getGuardia3() {
        return guardia3;
    }

    public void setGuardia3(String guardia3) {
        this.guardia3 = guardia3;
    }

    public String getLibranza1() {
        return libranza1;
    }

    public void setLibranza1(String libranza1) {
        this.libranza1 = libranza1;
    }

    public String getLibranza2() {
        return libranza2;
    }

    public void setLibranza2(String libranza2) {
        this.libranza2 = libranza2;
    }

    public String getLibranza3() {
        return libranza3;
    }

    public void setLibranza3(String libranza3) {
        this.libranza3 = libranza3;
    }

    public String getRecreo1() {
        return recreo1;
    }

    public void setRecreo1(String recreo1) {
        this.recreo1 = recreo1;
    }

    public String getRecreo2() {
        return recreo2;
    }

    public void setRecreo2(String recreo2) {
        this.recreo2 = recreo2;
    }

    public String getRecreo3() {
        return recreo3;
    }

    public void setRecreo3(String recreo3) {
        this.recreo3 = recreo3;
    }
}
