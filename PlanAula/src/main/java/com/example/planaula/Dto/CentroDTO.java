package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

 @Setter
 @Getter
public class CentroDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private MultipartFile logo;
    private boolean favorito;

    public CentroDTO() {
    }

    public CentroDTO(Integer id, String nombre, String descripcion, boolean favorito) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.favorito = favorito;
    }
    
    public CentroDTO(Integer id, String nombre, String descripcion, MultipartFile logo, boolean favorito) {
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
    
    public MultipartFile getLogo() {
        return logo;
    }

    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }
    
    public boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
