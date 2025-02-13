package com.example.planaula.Dto;

public class TutorDTO{
    private int id;
    private String curso;
    private String tutor2425;
    private String tutor2324;

    public TutorDTO(int id, String curso, String tutor2425, String tutor2324) {
        this.id = id;
        this.curso = curso;
        this.tutor2425 = tutor2425;
        this.tutor2324 = tutor2324;
    }
    public TutorDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTutor2425() {
        return tutor2425;
    }

    public void setTutor2425(String tutor2425) {
        this.tutor2425 = tutor2425;
    }

    public String getTutor2324() {
        return tutor2324;
    }

    public void setTutor2324(String tutor2324) {
        this.tutor2324 = tutor2324;
    }
}
