package com.example.planaula.Dto;

public class TutorDTO {
    private int id;
    private int idCurso;
    private int idTutor2425;
    private int idTutor2324;

    public TutorDTO(int id, int idCurso, int idTutor2425, int idTutor2324) {
        this.id = id;
        this.idCurso = idCurso;
        this.idTutor2425 = idTutor2425;
        this.idTutor2324 = idTutor2324;
    }
    public TutorDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdTutor2425() {
        return idTutor2425;
    }

    public void setIdTutor2425(int idTutor2425) {
        this.idTutor2425 = idTutor2425;
    }

    public int getIdTutor2324() {
        return idTutor2324;
    }

    public void setIdTutor2324(int idTutor2324) {}
}
