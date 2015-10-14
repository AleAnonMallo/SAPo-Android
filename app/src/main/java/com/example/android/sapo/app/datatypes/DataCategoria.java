package com.example.android.sapo.app.datatypes;

/**
 * Created by Alejandro on 14-Oct-15.
 */
public class DataCategoria {
    private int idCategoria;
    private String nombre;

    public DataCategoria() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
