package com.example.diznaoaoalcool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //verifica se existe um perfil activo na aplicação
    private boolean existeUserLigado() {
        if (new DataBase(this).quantidadePerfil()) {
            return true;
        } else {
            return false;
        }
    }

    //colocar app em ecrã 'cheio'
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    // ao clicar no ecrã, vai verificar se existe um utilizador activo, se não, manda criar um user, se sim, vai para a pagina inicial
    public boolean onTouchEvent(MotionEvent event){
        if(!existeUserLigado()) {
            startActivity(new Intent(MainActivity.this, NoProfile.class));
        } else {
            startActivity(new Intent(MainActivity.this, Pag_inicial.class));
            Toast.makeText( this, "Bem-vindo, "+new DataBase(this).listaPerfilActivo().getNome()+".", Toast.LENGTH_SHORT ).show();
        }
        return true;
    }
}
