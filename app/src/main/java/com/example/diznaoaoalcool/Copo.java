package com.example.diznaoaoalcool;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class Copo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tipo")
    private String tipo;

    @ColumnInfo(name = "volume")
    private int volume;

    public Copo(int id, String tipo, int volume) {
        this.id = id;
        this.tipo = tipo;
        this.volume = volume;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
