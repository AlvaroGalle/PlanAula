package com.example.planaula.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JornadaDTO {
	private int idCentro;
    private String[] dias;
    private String[] horas;

    public JornadaDTO(int idCentro, String[] dias, String[] horas) {
        this.idCentro = idCentro;
        this.dias = dias;
        this.horas = horas;
    }
    public JornadaDTO() {}

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public String[] getDias() {
        return dias;
    }

    public void setDias(String[] dias) {
        this.dias = dias;
    }
    
    public String[] getHoras() {
        return horas;
    }

    public void setHoras(String[] horas) {
        this.horas = horas;
    }
}
