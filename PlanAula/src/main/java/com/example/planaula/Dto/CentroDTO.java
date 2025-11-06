package com.example.planaula.Dto;

public class CentroDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private byte[] logo;
    private boolean favorito;

    public CentroDTO() {
    }

    public CentroDTO(Integer id, String nombre, String descripcion, byte[] logo, boolean favorito) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.logo = logo;
        this.favorito = favorito;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    
    public boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
