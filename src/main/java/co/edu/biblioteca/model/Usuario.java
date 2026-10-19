package co.edu.biblioteca.model;

import co.edu.biblioteca.enums.EstadoUsuario;
import co.edu.biblioteca.enums.TipoUsuario;

public class Usuario {

    private String identificacion;
    private String nombre;
    private String apellido;
    private String correo;
    private TipoUsuario tipoUsuario;
    private EstadoUsuario estado;

    public Usuario() {
    }

    public Usuario(String identificacion, String nombre, String apellido, String correo,
                   TipoUsuario tipoUsuario, EstadoUsuario estado) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.tipoUsuario = tipoUsuario;
        this.estado = estado;
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
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public EstadoUsuario getEstado() { return estado; }
    public void setEstado(EstadoUsuario estado) { this.estado = estado; }
}
