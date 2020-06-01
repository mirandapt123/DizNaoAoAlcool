package com.example.diznaoaoalcool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoProfile extends AppCompatActivity {
    private TextView msg_erro;
    private boolean editar = false;
    private Pessoa pessoa;
    TextView dataCarta;
    TextView nome;
    TextView idade;
    TextView peso;
    Spinner dropdownProfissional;
    Spinner dropdownSexo;

    @Override
    //cria um novo utilizador
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_profile);

        //carregar para o programa os elementos da página
        dataCarta = findViewById(R.id.data_input);
        nome = findViewById(R.id.nome_input);
        idade = findViewById(R.id.idade_input);
        peso = findViewById(R.id.peso_input);
        dropdownProfissional = findViewById(R.id.profissional_input);
        dropdownSexo = findViewById(R.id.sexo_input);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int criar = extras.getInt("criar");
            Pessoa temp = (Pessoa) extras.getSerializable("pessoa");

            if(temp != null){
                editar = true;
                pessoa = temp;
                TextView titulo = findViewById(R.id.textView2);
                titulo.setText("Editar perfil:");
            }

            if(criar == 1){
                TextView titulo = findViewById(R.id.textView2);
                titulo.setText("Criação de novo perfil:");
            }
        }else{
            Toast.makeText( this, "Não foi encontrado nenhum perfil, crie um perfil.", Toast.LENGTH_LONG ).show();
            TextView titulo = findViewById(R.id.textView2);
            titulo.setText("Não foi encontrado um perfil. Crie um:");
        }
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
        Button botaoAdicionar = (Button) findViewById(R.id.btn_validar);
        if(editar){
            botaoAdicionar.setText("Editar Perfil");
            botaoAdicionar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    editaPerfil();
                }
            });

            nome.setText(pessoa.getNome());
            idade.setText("" + pessoa.getIdade());
            peso.setText("" +pessoa.getPeso());
            dataCarta.setText(pessoa.getAno_carta());
            if(pessoa.isProfissional()) dropdownProfissional.setSelection(1);
            else dropdownProfissional.setSelection(0);
            dropdownSexo.setSelection(pessoa.getSexo());

        }else{
            botaoAdicionar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    criaPerfil();
                }
            });
        }
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
    public void editaPerfil() {
        //obtem campos
        int profissional = dropdownProfissional.getSelectedItemPosition();
        int sexo = dropdownSexo.getSelectedItemPosition();

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
                                if(validaNome(nome.getText().toString())) {
                                    //limpa cores de erro do campo
                                    colocaDadosCerto(nome);
                                    //verifica se é profissional
                                    boolean profissionalB;
                                    if (profissional == 0) {
                                        profissionalB = false;
                                    } else {
                                        profissionalB = true;
                                    }
                                    //novo objeto a utilizar para alterar os dados presentes na db
                                    Pessoa p = new Pessoa(pessoa.getId(), nome.getText().toString(), idadeInt, pesoInt, sexo, dataCarta.getText().toString(), profissionalB, pessoa.isActivo());

                                    //altera o objecto na db, se correr bem (return true = siga, return false = ocorreu um erro)
                                    if (new DataBase(this).alterarPessoa(p)) {
                                        //mensagem toda bonita a dar as boas vindas
                                        Toast.makeText(this, "Dados de perfil alterados com sucesso", Toast.LENGTH_LONG).show();

                                        //vai para as definições
                                        Intent intent = new Intent(NoProfile.this, Pag_inicial.class);
                                        intent.putExtra("definicoes", 1);
                                        startActivity(intent);
                                    } else {
                                        colocaMsgErro("Ocorreu um erro a editar o perfil, tente novamente.");
                                    }
                                }else{
                                    colocaDadosErro(nome);
                                    colocaMsgErro("Introduziu um nome que já existe");
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

    public void criaPerfil() {
        //obtem campos
        int profissional = dropdownProfissional.getSelectedItemPosition();
        int sexo = dropdownSexo.getSelectedItemPosition();

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
                                if(validaNome(nome.getText().toString())) {
                                    //verifica se é profissional
                                    colocaDadosCerto(nome);
                                    boolean profissionalB;
                                    if (profissional == 0) {
                                        profissionalB = false;
                                    } else {
                                        profissionalB = true;
                                    }
                                    //insere o utilizador na db, criando primeiro um objecto do Tipo pessoa. Linguagem orientada a objectos, não se deve passar os atributos isolados
                                    int id = new DataBase(this).quantidadePerfil();
                                    Pessoa p = new Pessoa(id + 1, nome.getText().toString(), idadeInt, pesoInt, sexo, dataCarta.getText().toString(), profissionalB, true);

                                    //insere o objecto na db, se correr bem (return true = siga, return false = ocorreu um erro
                                    if (new DataBase(this).inserirPessoa(p)) {

                                        //altera o perfil activo para o novo criado
                                        Pessoa perfilAtivo = new DataBase(this).listaPerfilActivo();
                                        if (perfilAtivo != null) {
                                            new DataBase(this).alteraPerfilActivo(p);
                                        }

                                        //mensagem toda bonita a dar as boas vindas
                                        Toast.makeText(this, "Bem-vindo, " + p.getNome() + ".", Toast.LENGTH_LONG).show();
                                        //vai para a página inicial
                                        startActivity(new Intent(NoProfile.this, Pag_inicial.class));
                                    } else {
                                        colocaMsgErro("Ocorreu um erro a inserir o perfil, tente novamente.");
                                    }
                                }else{
                                    colocaDadosErro(nome);
                                    colocaMsgErro("Introduziu um nome que já existe");
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

    private boolean validaNome(String nome){
        if(editar){
            if(nome.equals(pessoa.getNome())){
                return true;
            }
        }
        boolean resultado = new DataBase(this).verificaNomePessoa(nome);
        return resultado;
    }

    //coloca a aparencia normal no campo
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
