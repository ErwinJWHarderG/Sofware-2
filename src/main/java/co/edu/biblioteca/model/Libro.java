package co.edu.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

public class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private int anioPublicacion;
    private String categoria;
    private List<Ejemplar> ejemplares = new ArrayList<>();

    public Libro() {
    }

    public Libro(String isbn, String titulo, String autor, String editorial, int anioPublicacion, String categoria) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
    }

    public void agregarEjemplar(Ejemplar ejemplar) {
        ejemplares.add(ejemplar);
    }

    public Ejemplar buscarPrimerEjemplarDisponible() {
        for (Ejemplar ejemplar : ejemplares) {
            if (ejemplar.estaDisponible()) {
                return ejemplar;
            }
        }
        return null;
    }

    public int contarEjemplaresDisponibles() {
        int contador = 0;
        for (Ejemplar ejemplar : ejemplares) {
            if (ejemplar.estaDisponible()) {
                contador++;
            }
        }
        return contador;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public List<Ejemplar> getEjemplares() { return ejemplares; }
    public void setEjemplares(List<Ejemplar> ejemplares) { this.ejemplares = ejemplares; }
}
