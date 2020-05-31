package com.example.diznaoaoalcool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoProfile extends AppCompatActivity {
    private TextView msg_erro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_profile);
        Toast.makeText( this, "Não foi encontrado nenhum perfil, crie um perfil.", Toast.LENGTH_LONG ).show();
        msg_erro = findViewById(R.id.msg_erro_validacao);
        msg_erro.setVisibility(View.INVISIBLE);
        Spinner dropdown = findViewById(R.id.sexo_input);
        String[] items= new String[2];
        items[0] = "Masculino";
        items[1] = "Feminino";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        profissional();
    }

    private void profissional() {
        Spinner dropdown = findViewById(R.id.profissional_input);
        String[] items= new String[2];
        items[0] = "Não";
        items[1] = "Sim";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

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

    public void criaPerfil(View view) {
        TextView dataCarta = findViewById(R.id.data_input);
        TextView nome = findViewById(R.id.nome_input);
        TextView idade = findViewById(R.id.idade_input);
        TextView peso = findViewById(R.id.peso_input);
        Spinner dropdown = findViewById(R.id.profissional_input);
        int profissional = dropdown.getSelectedItemPosition();
        dropdown = findViewById(R.id.sexo_input);
        int sexo = dropdown.getSelectedItemPosition();
        boolean erro = false;

        if (!nome.getText().toString().isEmpty() && !idade.getText().toString().isEmpty() && !peso.getText().toString().isEmpty()) {
            try {
                //verificar se a data introduzida está no formato correcto
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                df.setLenient(false);
                Log.i("Log", "Data em string: " + dataCarta.getText().toString());
                Date dataInserida = df.parse(dataCarta.getText().toString());

                String dataActual = df.format(new Date());
                Date date1 = df.parse(dataActual);

                // se a data for mais recente
                if (dataInserida.compareTo(date1) > 0) {
                    colocaMsgErro("A data tem de ser menor ou igual ao dia de hoje.");
                    colocaDadosErro(dataCarta);
                } else {
                    colocaDadosCerto(dataCarta);
                    int idadeInt;
                    int pesoInt;
                    try {
                        idadeInt = Integer.parseInt(idade.getText().toString());
                        pesoInt = Integer.parseInt(peso.getText().toString());

                        if (idadeInt >= 18 && idadeInt<130) {
                            if (pesoInt >= 20 && pesoInt <= 499) {
                                //inserir perfil
                                boolean profissionalB;
                                if(profissional == 0) {
                                    profissionalB = false;
                                } else {
                                    profissionalB = true;
                                }
                                Pessoa p = new Pessoa(1, nome.getText().toString(), idadeInt, pesoInt, sexo, dataCarta.getText().toString(), profissionalB, true);

                                if (new DataBase(this).inserirPessoa(p)) {
                                    Toast.makeText( this, "Bem-vindo, "+p.getNome()+".", Toast.LENGTH_LONG ).show();
                                    startActivity(new Intent(NoProfile.this, Pag_inicial.class));
                                } else {
                                    colocaMsgErro("Ocorreu um erro a inserir o perfil, tente novamente.");
                                }
                            } else {
                                colocaMsgErro("Introduziu um peso inválido.");
                            }
                        } else {
                            colocaMsgErro("Introduziu uma idade inválida.");
                        }

                    } catch (NumberFormatException e) {
                        colocaMsgErro("Só pode introduzir inteiros no campo 'idade' e 'peso'.");
                    }
                }

            } catch (ParseException e) {
                colocaMsgErro("Colocou uma data inválida. Formato da data: dd/MM/aaaa");
                colocaDadosErro(dataCarta);
            }

        } else {
            colocaMsgErro("Tem de preencher todos os dados!");
        }


    }

    private void colocaDadosCerto(TextView textView) {
        textView.setBackgroundColor(Color.GREEN);
        textView.setTextColor(Color.WHITE);
    }

    private void colocaDadosErro(TextView textView) {
        textView.setBackgroundColor(Color.RED);
        textView.setTextColor(Color.WHITE);
    }

    private void colocaMsgErro(String msgErro) {
        msg_erro.setText(msgErro);
        msg_erro.setVisibility(View.VISIBLE);
    }


}
