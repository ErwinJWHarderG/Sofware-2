package co.edu.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

public class Biblioteca {

    private int idBiblioteca;
    private String nombre;
    private String direccion;
    private String telefono;
    private String horarioAtencion;
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Bibliotecario> bibliotecarios = new ArrayList<>();
    private List<Libro> libros = new ArrayList<>();
    private List<Prestamo> prestamos = new ArrayList<>();
    private List<Multa> multas = new ArrayList<>();

    public Biblioteca() {
    }

    public Biblioteca(int idBiblioteca, String nombre, String direccion, String telefono, String horarioAtencion) {
        this.idBiblioteca = idBiblioteca;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarioAtencion = horarioAtencion;
    }

    public int getIdBiblioteca() { return idBiblioteca; }
    public void setIdBiblioteca(int idBiblioteca) { this.idBiblioteca = idBiblioteca; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getHorarioAtencion() { return horarioAtencion; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
    public List<Bibliotecario> getBibliotecarios() { return bibliotecarios; }
    public void setBibliotecarios(List<Bibliotecario> bibliotecarios) { this.bibliotecarios = bibliotecarios; }
    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
    public List<Prestamo> getPrestamos() { return prestamos; }
    public void setPrestamos(List<Prestamo> prestamos) { this.prestamos = prestamos; }
    public List<Multa> getMultas() { return multas; }
    public void setMultas(List<Multa> multas) { this.multas = multas; }
}
