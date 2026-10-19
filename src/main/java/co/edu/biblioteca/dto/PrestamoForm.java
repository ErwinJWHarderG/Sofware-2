package co.edu.biblioteca.dto;

public class PrestamoForm {

    private String identificacionUsuario;
    private String isbnLibro;
    private String identificacionBibliotecario;
    private String observaciones;

    public PrestamoForm() {
    }

    public String getIdentificacionUsuario() { return identificacionUsuario; }
    public void setIdentificacionUsuario(String identificacionUsuario) { this.identificacionUsuario = identificacionUsuario; }
    public String getIsbnLibro() { return isbnLibro; }
    public void setIsbnLibro(String isbnLibro) { this.isbnLibro = isbnLibro; }
    public String getIdentificacionBibliotecario() { return identificacionBibliotecario; }
    public void setIdentificacionBibliotecario(String identificacionBibliotecario) { this.identificacionBibliotecario = identificacionBibliotecario; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
