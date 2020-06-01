package com.example.diznaoaoalcool;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class BA_TA {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id bebida")
    private int id_b;

    @ColumnInfo(name = "id copo")
    private int id_c;

    @ColumnInfo(name = "b_tipo")
    private String b_tipo;

    @ColumnInfo(name = "c_tipo")
    private String c_tipo;

    @ColumnInfo(name = "graduacao")
    private int graduacao;

    @ColumnInfo(name = "volume")
    private int volume;

    public BA_TA(int id_b, String b_tipo, int graduacao, int id_c, String c_tipo, int volume) {
        this.id_b = id_b;
        this.id_c = id_c;
        this.b_tipo = b_tipo;
        this.c_tipo = c_tipo;
        this.graduacao = graduacao;
        this.volume = volume;
    }

    public int getId_b() {
        return id_b;
    }

    public int getId_c() {
        return id_c;
    }

    public String getB_tipo() {
        return b_tipo;
    }

    public String getC_tipo() {
        return c_tipo;
    }

    public int getGraduacao() {
        return graduacao;
    }

    public int getVolume() {
        return volume;
    }
}
