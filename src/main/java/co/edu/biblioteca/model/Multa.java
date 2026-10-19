package co.edu.biblioteca.model;

import co.edu.biblioteca.enums.EstadoMulta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Multa {

    private int idMulta;
    private BigDecimal valor;
    private LocalDate fechaGeneracion;
    private EstadoMulta estado;
    private String motivo;
    private Usuario usuario;

    public Multa() {
    }

    public Multa(int idMulta, BigDecimal valor, LocalDate fechaGeneracion, EstadoMulta estado,
                 String motivo, Usuario usuario) {
        this.idMulta = idMulta;
        this.valor = valor;
        this.fechaGeneracion = fechaGeneracion;
        this.estado = estado;
        this.motivo = motivo;
        this.usuario = usuario;
    }

    public boolean estaPendiente() {
        return estado == EstadoMulta.PENDIENTE;
    }

    public int getIdMulta() { return idMulta; }
    public void setIdMulta(int idMulta) { this.idMulta = idMulta; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDate fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public EstadoMulta getEstado() { return estado; }
    public void setEstado(EstadoMulta estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
