package com.example.diznaoaoalcool;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Pessoa implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "idade")
    private int idade;

    @ColumnInfo(name = "peso")
    private int peso;

    @ColumnInfo(name = "sexo")
    private int sexo;

    @ColumnInfo(name = "ano carta")
    private String ano_carta;

    @ColumnInfo(name="profissional")
    private boolean profissional;

    @ColumnInfo(name="activo")
    private boolean activo;

    public Pessoa(int id, String nome, int idade, int peso, int sexo,
                  String ano_carta, boolean profissional, boolean activo) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.sexo = sexo;
        this.ano_carta = ano_carta;
        this.profissional = profissional;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String getAno_carta() {
        return ano_carta;
    }

    public void setAno_carta(String ano_carta) {
        this.ano_carta = ano_carta;
    }

    public boolean isProfissional() {
        return profissional;
    }

    public void setProfissional(boolean profissional) {
        this.profissional = profissional;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
