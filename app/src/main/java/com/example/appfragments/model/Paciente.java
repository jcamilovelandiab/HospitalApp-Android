package com.example.appfragments.model;

public class Paciente {

    Long id;
    String nombre;
    String sexo;
    String edad;
    String estatura;
    String peso;

    String area;
    String doctor;
    String fecha_ingreso;

    @Override
    public String toString() {
        return "Paciente" + "\r\n"+
                "id: " + id + "\r\n" +
                "nombre: " + nombre + "\r\n"+
                "sexo: " + sexo + "\r\n"+
                "edad: " + edad + "\r\n"+
                "estatura: " + estatura + "\r\n"+
                "peso: " + peso + "\r\n"+
                "area: " + area + "\r\n"+
                "doctor: " + doctor + "\r\n"+
                "fecha de ingreso: " + fecha_ingreso + "\r\n"
                ;
    }

    public Paciente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getEstatura() {
        return estatura;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }
}
