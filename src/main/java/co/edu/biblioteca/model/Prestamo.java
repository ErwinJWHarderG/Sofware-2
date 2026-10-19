package co.edu.biblioteca.model;

import co.edu.biblioteca.enums.EstadoPrestamo;

import java.time.LocalDate;

public class Prestamo {

    private int idPrestamo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;
    private String observaciones;
    private Usuario usuario;
    private Bibliotecario bibliotecario;
    private Libro libro;
    private Ejemplar ejemplar;

    public Prestamo() {
    }

    public Prestamo(int idPrestamo, LocalDate fechaPrestamo, LocalDate fechaDevolucion,
                    EstadoPrestamo estado, String observaciones, Usuario usuario,
                    Bibliotecario bibliotecario, Libro libro, Ejemplar ejemplar) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.usuario = usuario;
        this.bibliotecario = bibliotecario;
        this.libro = libro;
        this.ejemplar = ejemplar;
    }

    public boolean estaActivo() {
        return estado == EstadoPrestamo.ACTIVO;
    }

    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaDevolucion) && estado == EstadoPrestamo.ACTIVO;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Bibliotecario getBibliotecario() { return bibliotecario; }
    public void setBibliotecario(Bibliotecario bibliotecario) { this.bibliotecario = bibliotecario; }
    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
    public Ejemplar getEjemplar() { return ejemplar; }
    public void setEjemplar(Ejemplar ejemplar) { this.ejemplar = ejemplar; }
}
