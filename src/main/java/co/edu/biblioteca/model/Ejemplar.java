package co.edu.biblioteca.model;

import co.edu.biblioteca.enums.EstadoEjemplar;
import co.edu.biblioteca.enums.TipoEjemplar;

public class Ejemplar {

    private String codigoEjemplar;
    private EstadoEjemplar estado;
    private String ubicacion;
    private TipoEjemplar tipo;

    public Ejemplar() {
    }

    public Ejemplar(String codigoEjemplar, EstadoEjemplar estado, String ubicacion, TipoEjemplar tipo) {
        this.codigoEjemplar = codigoEjemplar;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
    }

    public boolean estaDisponible() {
        return estado == EstadoEjemplar.DISPONIBLE;
    }

    public void marcarPrestado() {
        this.estado = EstadoEjemplar.PRESTADO;
    }

    public void marcarDevuelto() {
        this.estado = EstadoEjemplar.DISPONIBLE;
    }

    public String getCodigoEjemplar() { return codigoEjemplar; }
    public void setCodigoEjemplar(String codigoEjemplar) { this.codigoEjemplar = codigoEjemplar; }
    public EstadoEjemplar getEstado() { return estado; }
    public void setEstado(EstadoEjemplar estado) { this.estado = estado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public TipoEjemplar getTipo() { return tipo; }
    public void setTipo(TipoEjemplar tipo) { this.tipo = tipo; }
}
