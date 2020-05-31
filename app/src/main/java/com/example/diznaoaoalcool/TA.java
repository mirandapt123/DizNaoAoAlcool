package com.example.diznaoaoalcool;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class TA {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "P_ID")
    private int p_id;

    @ColumnInfo(name = "TA_DATA")
    private String data;

    @ColumnInfo(name = "TA_RESULTADO")
    private double resultado;

    @ColumnInfo(name = "TA_COIMAMAX")
    private String coima;

    @ColumnInfo(name = "TA_INIB")
    private String ano_carta;

    @ColumnInfo(name = "TA_PONTOS")
    private String pontos;

    public TA() {
    }

    public TA(int id, int p_id, String data, double resultado, String coima, String ano_carta, String pontos) {
        this.id = id;
        this.p_id = p_id;
        this.data = data;
        this.resultado = resultado;
        this.coima = coima;
        this.ano_carta = ano_carta;
        this.pontos = pontos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public String getCoima() {
        return coima;
    }

    public void setCoima(String coima) {
        this.coima = coima;
    }

    public String getAno_carta() {
        return ano_carta;
    }

    public void setAno_carta(String ano_carta) {
        this.ano_carta = ano_carta;
    }

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }
}
