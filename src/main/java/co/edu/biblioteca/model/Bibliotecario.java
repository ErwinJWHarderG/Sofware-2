package co.edu.biblioteca.model;

import co.edu.biblioteca.enums.Turno;

public class Bibliotecario {

    private String identificacion;
    private String nombre;
    private String apellido;
    private String cargo;
    private Turno turno;

    public Bibliotecario() {
    }

    public Bibliotecario(String identificacion, String nombre, String apellido, String cargo, Turno turno) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
        this.turno = turno;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }
}
