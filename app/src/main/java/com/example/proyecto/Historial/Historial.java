package com.example.proyecto.Historial;

import com.google.firebase.Timestamp;

public class Historial {

    private String imagen, tipoReporte, estatus, comentarios, ubicacion, damage;
    private Timestamp timestamp;

    public Historial(String imagen, String tipoReporte, String estatus, String comentarios, String ubicacion, String damage, Timestamp timestamp) {
        this.imagen = imagen;
        this.tipoReporte = tipoReporte;
        this.estatus = estatus;
        this.comentarios = comentarios;
        this.ubicacion = ubicacion;
        this.damage = damage;
        this.timestamp = timestamp;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
