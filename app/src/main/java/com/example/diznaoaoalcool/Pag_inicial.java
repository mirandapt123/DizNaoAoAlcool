package com.example.diznaoaoalcool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Pag_inicial extends AppCompatActivity {
    Pessoa perfilActivo;
    List <BA_TA> listaBebidaCopo;
    List <Bebida> listaBebida;
    List <Copo> listaCopo;
    List <TA> listaTA;
    List<Fragment> listaBebidaFrag;
    public static int bebidas [][];
    public static double taxaTotal;

    @Override
    //ao criar verificamos se as bebidas/copos estão adicionados, caso não, adicionamos
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_inicial);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //colocar o menu
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Bundle extras = getIntent().getExtras();
        // é diferente de null apenas quando se faz edição de dados de um perfil
        if(extras != null){
            //começar no framento definições
            if(extras.getInt("definicoes") == 1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            }
        }else{
            //começar no fragmento da home
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        //obter o perfil activo
        perfilActivo = perfilActivo();

        insereBebida();
        listaBebida = new DataBase(this).listaBebidas();

        insereCopo();
        listaCopo = new DataBase(this).listaCopos();
    }

    // se não existir, adiciona
    private void insereBebida() {
        if (new DataBase(this).verificaBebida()) {
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Cerveja Light", 4));
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Cerveja Normal", 5));
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Vinho", 12));
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Cocktail Genérico", 13));
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Shot", 40));
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Whisky", 43));
            new DataBase(this).insereBebidaGenerica(new Bebida(-1, "Outra bebida", 10));
            Log.i("Bebidas genéricas", "Foram inseridas na db.");
        } else {
            Log.i("Bebidas genéricas", "Não foram inseridas, já existem na db.");
        }
    }

    //se não existir, adiciona
    private void insereCopo() {
        if (new DataBase(this).verificaCopo()) {
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Copo de Shot", 30));
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Copo Pony", 60));
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Copo de Cocktail", 120));
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Copo de Whisky", 240));
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Copo de Vinho", 340));
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Caneca de Cerveja", 600));
            new DataBase(this).insereCopoGenerico(new Copo(-1, "Outro copo", 700));
            Log.i("Copos genéricos", "Foram inseridas na db.");
        } else {
            Log.i("Copos genéricos", "Não foram inseridos, já existem na db.");
        }
    }

    //configuração do menu, estar à escuta de eventos no mesmo
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            removeFrag();
                            selectedFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_settings:
                            removeFrag();
                            selectedFragment = new SettingsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_history:
                            refreshMyData(0);
                            break;
                        case R.id.nav_fines:
                            removeFrag();
                            selectedFragment = new FinesFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                    }
                    return true;
                }
            };

    //voltar ao menu das coimas
    public void voltaMenu() {
        //remove todos os fragmentos
        removeFrag();
        //coloca o menu das coimas
        Fragment selectedFragment = new FinesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    //remove todos os fragmentos para não haver erros
    private void removeFrag() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BebidaFragment && fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof HistoricoDetalhadoFragment && fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof FinalizarFragment && fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof TaFragment && fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
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

    //obtem o perfil activo
    private Pessoa perfilActivo () {
        return new DataBase(this).listaPerfilActivo();
    }

    //verifica os dados inseridos para calcular o teste
    public void vamosAIsso(View view) {
        TextView quantidade = findViewById(R.id.input_quantidade);
        TextView msg_erro = findViewById(R.id.erro_home);

        if (quantidade.getText().length() > 0) {
            int quantidadeInt = Integer.parseInt(quantidade.getText().toString());

            //não pode adicionar mais de 20 bebidas
            if (quantidadeInt > 20) {
                msg_erro.setText("Não sejas aldrabão, introduz uma quantia correcta.");
                msg_erro.setVisibility(View.VISIBLE);
            //tem de ser uma ou mais bebidas
            } else if (quantidadeInt <= 0) {
                msg_erro.setText("Então para que é que queres fazer o teste?");
                msg_erro.setVisibility(View.VISIBLE);
            } else {
                msg_erro.setVisibility(View.INVISIBLE);
                colocaFragmentos(quantidadeInt);
            }
        } else {
            msg_erro.setText("Tem de digitar alguma coisa.");
            msg_erro.setVisibility(View.VISIBLE);
        }

    }

    //coloca as bebidas, consoante a quantidade colocada pelo utilizador
    private void colocaFragmentos(int quantidade) {
        TableRow quantidadeLinha = findViewById(R.id.linhaQuant);
        quantidadeLinha.setVisibility(View.GONE);


        for (int i = 0; i < quantidade; i++) {
            //Se for a última bebida
            if (i < (quantidade - 1)) {
                BebidaFragment aFragment = new BebidaFragment(1);
                getSupportFragmentManager().beginTransaction().add(R.id.tabelaBebidas, aFragment, ""+i).commit();
            } else {
                BebidaFragment aFragment = new BebidaFragment(0);
                getSupportFragmentManager().beginTransaction().add(R.id.tabelaBebidas, aFragment, ""+i).commit();
            }
        }
    }

    public void refreshMyData(int ordenar){
        removeFrag();
        Fragment selectedFragment = new HistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, "historico").commit();
        //verifica o tipo de ordenação desejada
        switch (ordenar) {
            case 0:
                mostraHistorico("TA_DATA asc", 0);
                break;
            case 1:
                mostraHistorico("TA_DATA desc", 1);
                break;
            case 2:
                mostraHistorico("TA_RESULTADO asc", 2);
                break;
            case 3:
                mostraHistorico("TA_RESULTADO desc", 3);
                break;
        }
    }

    //função que recebe a ordem para mostrar os detalhes de um teste
    public void mostraDetalhes(int ta_id, double resultado){
        Fragment selectedFragment = new HistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, "historico").commit();
        mostraDetalhesFrag(ta_id, resultado);
    }

    private void mostraDetalhesFrag(int ta_id, double resultado) {
        // remove os fragmentos
        removeFrag();
        // obtem bebidas, copos, resultado, jejum e quantidade da db
        listaBebidaCopo = new DataBase(this).listaBebidaCopo(ta_id);
        Log.i("Detalhes:", "TA_ID: "+ta_id);
        if (listaBebidaCopo != null) {
            for (int i = 0; i < listaBebidaCopo.size(); i++) {
                int quantidade = new DataBase(this).obtemQuantidade(ta_id, listaBebidaCopo.get(i).getId_b(), listaBebidaCopo.get(i).getId_c());
                //se for o último
                if (i == listaBebidaCopo.size() -1) {
                    HistoricoDetalhadoFragment aFragment = new HistoricoDetalhadoFragment(ta_id, resultado, listaBebidaCopo.get(i), 0, quantidade);
                    getSupportFragmentManager().beginTransaction().add(R.id.tabelaHistorico, aFragment).commit();
                //se for o primeiro
                } else if (i == 0) {
                    HistoricoDetalhadoFragment aFragment = new HistoricoDetalhadoFragment(ta_id, resultado, listaBebidaCopo.get(i), 1, quantidade);
                    getSupportFragmentManager().beginTransaction().add(R.id.tabelaHistorico, aFragment).commit();
                } else {
                    HistoricoDetalhadoFragment aFragment = new HistoricoDetalhadoFragment(ta_id, resultado, listaBebidaCopo.get(i), -1, quantidade);
                    getSupportFragmentManager().beginTransaction().add(R.id.tabelaHistorico, aFragment).commit();
                }
            }
        } else {
            //se ocorrer um erro, acaba com a actividade e volta para a página inicial
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            Toast.makeText( this, "Ocorreu um erro, a voltar à página inicial.", Toast.LENGTH_SHORT ).show();
        }
    }

    //mostra o histórico
    private void mostraHistorico(String order, int tipoOrder) {
        // remove os fragmentos
        removeFrag();
        // adiciona o fragmento do historico
        listaTA = new DataBase(this).obtemTA(perfilActivo.getId(), order);

        if (listaTA != null) {
            for (int i = 0; i < listaTA.size(); i++) {
                TaFragment aFragment = new TaFragment(listaTA.get(i), i, tipoOrder);
                getSupportFragmentManager().beginTransaction().add(R.id.tabelaHistorico, aFragment).commit();
            }
        } else {
            TaFragment aFragment = new TaFragment(new TA(), -1, -1);
            getSupportFragmentManager().beginTransaction().add(R.id.tabelaHistorico, aFragment).commit();
        }
    }

    //calcula a taxa de alcoolemia
    public void calculaTaxa(View view) {
        listaBebidaFrag = getSupportFragmentManager().getFragments();
        taxaTotal = 0;
        bebidas = new int[listaBebidaFrag.size()][6];
        //0->id bebida, 1-> id copo, 2-> volume, 3-> quantidade, 4-> graduacao, 5-> jejum (-1 não, 1 sim)
        if (listaBebidaFrag != null) {
            Pessoa p = new DataBase(this).listaPerfilActivo();
            int j = 0;
            for (int i = 1; i < listaBebidaFrag.size(); i++) {
                //obtem dados introduzidos em cada bebida
                SeekBar volumeSeek = listaBebidaFrag.get(i).getView().findViewById(R.id.volume_consumido);
                SeekBar quantSeek = listaBebidaFrag.get(i).getView().findViewById(R.id.quantidade);
                SeekBar graduacaoSeek = listaBebidaFrag.get(i).getView().findViewById(R.id.graduacao);
                Spinner dropdown = listaBebidaFrag.get(i).getView().findViewById(R.id.spinner_bebida);
                bebidas [j][0] = dropdown.getSelectedItemPosition();
                dropdown = listaBebidaFrag.get(i).getView().findViewById(R.id.spinner_copo);
                bebidas [j][1] = dropdown.getSelectedItemPosition();
                int volume = volumeSeek.getProgress();
                int quantidade = quantSeek.getProgress();
                int graduacao = graduacaoSeek.getProgress();
                bebidas [j][2] = volume;
                bebidas [j][3] = quantidade;
                bebidas [j][4] = graduacao;
                //se a graduação for maior que 0
                if (graduacao != 0) {
                    //verifica as gramas que tem a bebida (alcool)
                    double gramasAlcool =  (graduacao * 0.01) * volume * quantidade;
                    double coeficiente = 0;
                    CheckBox jejum = listaBebidaFrag.get(i).getView().findViewById(R.id.jej);
                    Log.i("Fragmento nº"+i,""+jejum.isChecked());
                    //verifica o sexo da pessoa, assim como se bebeu em jejum
                    if (p.getSexo() == 0) {
                        if (jejum.isChecked()) {
                            bebidas [j][5] = 1;
                            coeficiente =  0.7;
                        } else {
                            bebidas [j][5] = -1;
                            coeficiente =  1.1;
                        }
                    } else {
                        if (jejum.isChecked()) {
                            bebidas [j][5] = 1;
                            coeficiente = 0.6;
                        } else {
                            bebidas [j][5] = -1;
                            coeficiente =  1.1;
                        }
                    }
                    //calcula a taxa e adiciona à taxa total
                    double taxa = gramasAlcool / (p.getPeso() * coeficiente);
                    Log.i("Fragmento nº"+i,"Taxa de alcool: "+ taxa + " gr/l");
                    taxaTotal += taxa;
                    Log.i("Taxa total","Até agora: "+ taxaTotal + "gr/l");
                }
                j++;
            }
            // remove os fragmentos
            removeFrag();
            // adiciona o fragmento do resultado
            FinalizarFragment aFragment = new FinalizarFragment(calculaPassouTeste(), 0);
            getSupportFragmentManager().beginTransaction().add(R.id.tabelaBebidas, aFragment).commit();
        } else {
            Log.i("Lista de fragmentos","É nula... Que se passa? :(");
            Toast.makeText( this, "Ocorreu um erro, tente novamente.", Toast.LENGTH_SHORT ).show();
        }
    }

    //retira os componentes do fragmento das coimas e coloca o fragmento finalizar com o resultado pedido
    public void verCoima(View view) {
        TextView taxa = findViewById(R.id.input_alcool);
        taxaTotal = Double.parseDouble(taxa.getText().toString());

        TableRow quantidadeLinha = findViewById(R.id.linhaQuant1);
        quantidadeLinha.setVisibility(View.GONE);

        FinalizarFragment aFragment = new FinalizarFragment(calculaPassouTeste(), 1);
        getSupportFragmentManager().beginTransaction().add(R.id.tabelaCoimas, aFragment).commit();
    }

    //salva o teste
    public void save(View view) {
        listaBebidaFrag = getSupportFragmentManager().getFragments();

        //se a lista for diferente de nula
        if (listaBebidaFrag != null) {
            CheckBox salvar = listaBebidaFrag.get(1).getView().findViewById(R.id.save_ceck);
            //se quiser salvar
            if (salvar.isChecked()) {
                //obtem os dados necessários
                TextView coimaMax = listaBebidaFrag.get(1).getView().findViewById(R.id.multa_dinheiro);
                TextView pontos = listaBebidaFrag.get(1).getView().findViewById(R.id.pontos);
                TextView inib = listaBebidaFrag.get(1).getView().findViewById(R.id.inibicao);
                if (salvarDados(coimaMax.getText().toString(), pontos.getText().toString(), inib.getText().toString())) {
                    Toast.makeText( this, "O resultado foi guardado com sucesso.", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( this, "Ocorreu um erro a guardar o resultado.", Toast.LENGTH_SHORT ).show();
                }
                Log.i("Guardar", "sim");
                //volta para a página inicial
                retornaHome();
            } else {
                Log.i("Guardar", "não");
                Toast.makeText( this, "A voltar à página principal.", Toast.LENGTH_SHORT ).show();
                //volta para a página inicial
                retornaHome();
            }
        } else {
            Toast.makeText( this, "Ocorreu um erro, tente novamente.", Toast.LENGTH_SHORT ).show();
        }
    }

    //salva os dados em si
    private boolean salvarDados(String coima, String pontos, String inib) {
        boolean contador = true; int idTa = -1;
        for (int i = 0; i < bebidas.length-1; i++) {
            //obtem o id da bebida e do copo
            int idBebida = new DataBase(this).obtemIDBebida(listaBebida.get(bebidas[i][0]).getTipo(), bebidas[i][4]);
            int idCopo = new DataBase(this).obtemIDCopo(listaCopo.get(bebidas[i][1]).getTipo(), bebidas[i][2]);

            //se o id da bebida for diferente de -1
            if (idBebida == -1) {
                idBebida = new DataBase(this).adicionaBebidaCalc(listaBebida.get(bebidas[i][0]).getTipo(), bebidas[i][4]);
            }

            //se o id do copo for diferente de -1
            if (idCopo == -1) {
                idCopo = new DataBase(this).adicionaCopoCalc(listaCopo.get(bebidas[i][1]).getTipo(), bebidas[i][2]);
            }

            Log.i("id bebida", ""+idBebida);
            Log.i("id copo", ""+idCopo);

            //se não houver erro
            if (contador) {
                contador = false;
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                idTa = new DataBase(this).adicionaTA(perfilActivo.getId(), formatter.format(date), taxaTotal, coima, pontos, inib);
            }

            //adiciona o teste à base de dados
            if (idCopo != -1 && idBebida != -1 && idTa != -1) {
                if (new DataBase(this).adicionaTABC(idTa, idBebida, bebidas[i][5], idCopo, bebidas[i][3]) == -1) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    //volta a casa!
    private void retornaHome() {
        removeFrag();
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    //vai calcular o teste consoante os dados do utilizador
    private boolean calculaPassouTeste() {
        Pessoa p = new DataBase(this).listaPerfilActivo();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(p.getAno_carta());
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(dateFormat.format(today));
            long diffInMillis = date2.getTime() - date1.getTime();
            long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

            if (dateDiffInDays > 1095 && !p.isProfissional()) {
                if (taxaTotal >= 0.5) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (taxaTotal >= 0.2) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (ParseException e) {
            return false;
        }

    }
}
