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
    //cria um novo utilizador
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_profile);
        Toast.makeText( this, "Não foi encontrado nenhum perfil, crie um perfil.", Toast.LENGTH_LONG ).show();
        msg_erro = findViewById(R.id.msg_erro_validacao);
        msg_erro.setVisibility(View.INVISIBLE);
        //coloca os sexos num spinner. Hoje em dia dizem que há mais que 2, mas nós somos antiquados :)
        Spinner dropdown = findViewById(R.id.sexo_input);
        String[] items= new String[2];
        items[0] = "Masculino";
        items[1] = "Feminino";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        profissional();
    }

    //preenche o spinner profissional com sim ou não
    private void profissional() {
        Spinner dropdown = findViewById(R.id.profissional_input);
        String[] items= new String[2];
        items[0] = "Não";
        items[1] = "Sim";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    // 'ecrã cheio'
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    //retirar as barras do sistema
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

    //cria um peril com os dados introduzidos
    public void criaPerfil(View view) {
        //obtem campos
        TextView dataCarta = findViewById(R.id.data_input);
        TextView nome = findViewById(R.id.nome_input);
        TextView idade = findViewById(R.id.idade_input);
        TextView peso = findViewById(R.id.peso_input);
        Spinner dropdown = findViewById(R.id.profissional_input);
        int profissional = dropdown.getSelectedItemPosition();
        dropdown = findViewById(R.id.sexo_input);
        int sexo = dropdown.getSelectedItemPosition();

        //se não houver campos vazios
        if (!nome.getText().toString().isEmpty() && !idade.getText().toString().isEmpty() && !peso.getText().toString().isEmpty()) {
            try {
                //verificar se a data introduzida está no formato correcto
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                df.setLenient(false);
                Log.i("Log", "Data em string: " + dataCarta.getText().toString());
                Date dataInserida = df.parse(dataCarta.getText().toString());

                String dataActual = df.format(new Date());
                Date date1 = df.parse(dataActual);

                // se a data for mais recente do que o dia actual (o utilizador não pode ser bruxo, certo?)
                if (dataInserida.compareTo(date1) > 0) {
                    colocaMsgErro("A data tem de ser menor ou igual ao dia de hoje.");
                    colocaDadosErro(dataCarta);
                } else {
                    colocaDadosCerto(dataCarta);
                    int idadeInt;
                    int pesoInt;
                    try {
                        //tenta converter a idade e peso para inteiro, em principio vai dar sempre, mas....mais vale prevenir que remediar
                        idadeInt = Integer.parseInt(idade.getText().toString());
                        pesoInt = Integer.parseInt(peso.getText().toString());

                        //tem de ter mais de 18 anos. Idade legal para beber em Portugal é 18 anos (não parece)
                        //tem de ter no máximo 129 anos
                        if (idadeInt >= 18 && idadeInt<130) {
                            //tem de ter mais de 20kg e menos de 500kg
                            if (pesoInt >= 20 && pesoInt <= 499) {
                                //verifica se é profissional
                                boolean profissionalB;
                                if(profissional == 0) {
                                    profissionalB = false;
                                } else {
                                    profissionalB = true;
                                }
                                //insere o utilizador na db, criando primeiro um objecto do Tipo pessoa. Linguagem orientada a objectos, não se deve passar os atributos isolados
                                Pessoa p = new Pessoa(1, nome.getText().toString(), idadeInt, pesoInt, sexo, dataCarta.getText().toString(), profissionalB, true);

                                //insere o objecto na db, se correr bem (return true = siga, return false = ocorreu um erro
                                if (new DataBase(this).inserirPessoa(p)) {
                                    //mensagem toda bonita a dar as boas vindas
                                    Toast.makeText( this, "Bem-vindo, "+p.getNome()+".", Toast.LENGTH_LONG ).show();
                                    //vai para a página inicial
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
            //caso ocorra um erro a fazer o Parse
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

    //coloca os dados de erro
    private void colocaDadosErro(TextView textView) {
        textView.setBackgroundColor(Color.RED);
        textView.setTextColor(Color.WHITE);
    }

    //coloca msg de erro
    private void colocaMsgErro(String msgErro) {
        msg_erro.setText(msgErro);
        msg_erro.setVisibility(View.VISIBLE);
    }


}
