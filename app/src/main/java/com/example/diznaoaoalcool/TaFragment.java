package com.example.diznaoaoalcool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaFragment extends Fragment {

    private boolean passou;

    private TA ta;
    private int primeiro;
    private int tipoOrdenacao;
    private static View inf;

    public TaFragment() {
    }

    //primeiro = saber quem é primeiro e último | tipoOrder = saber tipo de ordenação
    public TaFragment(TA ta, int primeiro, int tipoOrder) {
        this.ta = ta;
        this.primeiro = primeiro;
        this.tipoOrdenacao = tipoOrder;
        Log.i("mais um criado",":))))");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inf = inflater.inflate(R.layout.fragment_historico, container, false);
        TextView data = inf.findViewById(R.id.data_teste);
        data.setText("Teste realizado na data: "+ta.getData());
        //não há itens no histórico
        if (primeiro == -1) {
            //elimina o desnecessário ao fragmento. Deveria estar numa função (s/tempo)
            TableRow eliminar = inf.findViewById(R.id.eliminar_11);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_ordenar);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_limpar);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.row_eliminar1);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_22);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_33);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_55);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_77);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_99);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.e_1);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.e_2);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.e_3);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.e_4);
            eliminar.setVisibility(View.GONE);
            TextView msg = inf.findViewById(R.id.txt_Historico2);
            msg.setText("Ainda não tem dados no histórico.");
        } else {
            DecimalFormat f = new DecimalFormat("##.00");

            Pessoa p = new DataBase(getContext()).listaPerfilActivo();

            if (calculaPassouTeste(p)) {
                TextView resultado = inf.findViewById(R.id.resultado1);
                if (ta.getResultado() < 1) {
                    resultado.setText("0" + f.format(ta.getResultado()) + " g/l.");
                } else {
                    resultado.setText("" + f.format(ta.getResultado()) + " g/l.");
                }
                resultado.setTextColor(Color.rgb(0, 128, 0));
                TextView msg = inf.findViewById(R.id.passouNao1);
                msg.setText("Passou neste teste.");
                msg.setTextColor(Color.rgb(0, 128, 0));
                //eliminar o que não é preciso, pois passou
                TableRow eliminar = inf.findViewById(R.id.eliminar_11);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_22);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_33);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_55);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_77);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_99);
                eliminar.setVisibility(View.GONE);
            } else {
                TextView resultado = inf.findViewById(R.id.resultado1);
                if (ta.getResultado() < 1) {
                    resultado.setText("0" + f.format(ta.getResultado()) + " g/l.");
                } else {
                    resultado.setText("" + f.format(ta.getResultado()) + " g/l.");
                }
                resultado.setTextColor(Color.RED);
                TextView multa = inf.findViewById(R.id.multa_dinheiro1);
                TextView pontos = inf.findViewById(R.id.pontos1);
                TextView inibicao = inf.findViewById(R.id.inibicao1);
                TextView tipoContra = inf.findViewById(R.id.tipoContra1);
                multa.setText(ta.getCoima());
                pontos.setText(ta.getPontos());
                inibicao.setText(ta.getAno_carta());
                calculaTipo(tipoContra);
            }

            // se for o primeiro
            if (primeiro == 0) {
                TextView msg = inf.findViewById(R.id.txt_Historico2);
                msg.setText("Histórico:");

                Button apagarTudo = (Button) inf.findViewById(R.id.btn_clearall);
                apagarTudo.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        eliminarHistorico("Tem a certeza que deseja limpar o histórico?", -1);
                    }
                });

                // spinner para ordenar
                Spinner dropdown = inf.findViewById(R.id.spinner_ordenar);
                String[] items = new String [4];
                items[0] = "Ordenar por data ASC";
                items[1] = "Ordenar por data DESC";
                items[2] = "Ordenar por valor ASC";
                items[3] = "Ordenar por valor DESC";

                ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);

                dropdown.setSelection(tipoOrdenacao);

                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    //ao selecionar o item e se não for o mesmo do início, vai ordenar consoante o pedido
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if(position != tipoOrdenacao)
                            refreshActivity(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            } else {
                TableRow tr = inf.findViewById(R.id.eliminar_1111);
                tr.setVisibility(View.GONE);
                tr = inf.findViewById(R.id.eliminar_111);
                tr.setVisibility(View.GONE);
                tr = inf.findViewById(R.id.eliminar_ordenar);
                tr.setVisibility(View.GONE);
                tr = inf.findViewById(R.id.eliminar_limpar);
                tr.setVisibility(View.GONE);
            }
        }

        //evento no botão de apagar um teste
        Button btn = (Button) inf.findViewById(R.id.apagart);
        btn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Cliquei em apagar","ID TA:"+ta.getId());
                eliminarHistorico("Tem a certeza que deseja limpar este teste do histórico?", ta.getId());
            }
        });

        //evento no botão de mostrar mais detalhes
        btn = (Button) inf.findViewById(R.id.ver_mais1);
        btn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Cliquei em mostrar mais detalhes","ID TA:"+ta.getId());
                mostraMaisDetalhes();
            }
        });

        return inf;
    }

    //Calcula se o condutor passou no teste, se está a usar a aplicação é provável que não passe :)
    private boolean calculaPassouTeste(Pessoa p) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(p.getAno_carta());
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(dateFormat.format(today));
            long diffInMillis = date2.getTime() - date1.getTime();
            long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            Log.i("Dias de carta",""+dateDiffInDays);

            if (dateDiffInDays > 1095 && !p.isProfissional()) {
                if (ta.getResultado() >= 0.5) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (ta.getResultado() >= 0.2) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (ParseException e) {
            return false;
        }

    }

    //Vai calcular o tipo de contra ordenação consoante a idade da carta do condutor e o tipo
    private void calculaTipo(TextView tipoContra) {
        Pessoa p = new DataBase(getContext()).listaPerfilActivo();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(p.getAno_carta());
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(dateFormat.format(today));
            long diffInMillis = date2.getTime() - date1.getTime();
            long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

            if (dateDiffInDays > 1095 && !p.isProfissional()) {
                if (ta.getResultado() >= 0.5 && ta.getResultado() < 0.8 ) {
                    tipoContra.setText("Tipo: Contraordenação grave.");
                } else if (ta.getResultado() >= 0.8 && ta.getResultado() < 1.2 ) {
                    tipoContra.setText("Tipo: Contraordenação muito grave.");
                } else {
                    tipoContra.setText("Tipo: CRIME.");
                }
            } else {
                if (ta.getResultado() >= 0.2 && ta.getResultado() < 0.5 ) {
                    tipoContra.setText("Tipo: Contraordenação grave.");
                } else if (ta.getResultado() >= 0.5 && ta.getResultado() < 1.2 ) {
                    tipoContra.setText("Tipo: Contraordenação muito grave.");
                } else {
                    tipoContra.setText("Tipo: CRIME.");
                }
            }
        } catch (ParseException e) {}

    }

    //elimina um ou todo o histórico, tudo depende se a variável idTA é -1 ou diferente de -1
    private void eliminarHistorico(String msg, final int idTa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pim Pam Pum");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_history);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                if (idTa == -1) {
                    new DataBase(getContext()).deleteHistory();
                    Toast.makeText( getContext(), "O histórico foi limpo com sucesso.", Toast.LENGTH_LONG ).show();
                    refreshActivity(0);
                } else {
                    new DataBase(getContext()).delete1History(idTa);
                    Toast.makeText( getContext(), "O teste foi eliminado do histórico com sucesso.", Toast.LENGTH_LONG ).show();
                    refreshActivity(0);
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //'diz' à aplicação inicial para remover todos os fragmentos e voltar a fazer o método inicial (callback)
    public void refreshActivity(int posicao){
        Pag_inicial mDashboardActivity = (Pag_inicial) getActivity();
        if(mDashboardActivity != null){
            mDashboardActivity.refreshMyData(posicao);
        }
    }

    //'diz' à aplicação inicial para remover todos os fragmentos e voltar a fazer o método inicial (callback)
    public void mostraMaisDetalhes(){
        Pag_inicial mDashboardActivity = (Pag_inicial) getActivity();
        if(mDashboardActivity != null){
            mDashboardActivity.mostraDetalhes(ta.getId(), ta.getResultado());
        }
    }

}