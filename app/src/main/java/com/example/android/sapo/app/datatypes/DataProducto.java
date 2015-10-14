package com.example.android.sapo.app.datatypes;

/**
 * Created by Alejandro on 14/10/2015.
 */
public class DataProducto {
    private int idProducto;
    private String nombre;
    private String descripcion;

    public DataProducto() {
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
