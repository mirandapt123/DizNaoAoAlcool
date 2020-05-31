package com.example.diznaoaoalcool;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class Bebida {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tipo")
    private String tipo;

    @ColumnInfo(name = "graduacao")
    private int graduacao;

    public Bebida(int id, String tipo, int graduacao) {
        this.id = id;
        this.tipo = tipo;
        this.graduacao = graduacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getGraduacao() {
        return graduacao;
    }

    public void setGraduacao(int graduacao) {
        this.graduacao = graduacao;
    }
}
