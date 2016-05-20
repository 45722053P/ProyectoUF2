package com.proyecto.projectmap;

/**
 * Created by alex on 20/05/2016.
 */
public class Nota {

    private String titulo;
    private String nota;
    private double latitud;
    private double longitud;
    private String imagePath;
    private String videoPath;

    public Nota() {

    }
    public Nota(String titulo, String nota, double latitud, double longitud, String imagePath, String videoPath) {
        this.titulo = titulo;
        this.nota = nota;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
    }
    //GETTERS
    public String getTitulo() {
        return titulo;
    }
    public String getNota() {
        return nota;
    }
    public double getLatitud() {
        return latitud;
    }
    public double getLongitud() {
        return longitud;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getVideoPath() {
        return videoPath;
    }


    //SETTERS
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setNota(String nota) {
        this.nota = nota;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}