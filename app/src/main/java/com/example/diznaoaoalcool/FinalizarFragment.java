package com.example.diznaoaoalcool;

import android.content.Context;
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

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinalizarFragment extends Fragment {

    private boolean passou;


    private static View inf;

    public FinalizarFragment() {
    }

    public FinalizarFragment(boolean passou) {
        this.passou = passou;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inf = inflater.inflate(R.layout.fragment_finalizar, container, false);
        DecimalFormat f = new DecimalFormat("##.00");

        if (passou) {
            TextView resultado = inf.findViewById(R.id.resultado);
            if (Pag_inicial.taxaTotal < 1) {
                resultado.setText("0"+f.format(Pag_inicial.taxaTotal)+ " g/l.");
            } else {
                resultado.setText(""+f.format(Pag_inicial.taxaTotal)+ " g/l.");
            }
            resultado.setTextColor(Color.rgb(0,128,0));
            TextView msg = inf.findViewById(R.id.passouNao);
            msg.setText("Passou no teste! Pode conduzir (com juízo).");
            msg.setTextColor(Color.rgb(0,128,0));
            //eliminar o que não é preciso, pois passou
            TableRow eliminar = inf.findViewById(R.id.eliminar_1);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_2);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_3);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_4);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_5);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_6);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_7);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_9);
            eliminar.setVisibility(View.GONE);
        } else {
            TextView resultado = inf.findViewById(R.id.resultado);
            if (Pag_inicial.taxaTotal < 1) {
                resultado.setText("0"+f.format(Pag_inicial.taxaTotal)+ " g/l.");
            } else {
                resultado.setText(""+f.format(Pag_inicial.taxaTotal)+ " g/l.");
            }
            resultado.setTextColor(Color.RED);
            TextView multa = inf.findViewById(R.id.multa_dinheiro);
            TextView pontos = inf.findViewById(R.id.pontos);
            TextView inibicao = inf.findViewById(R.id.inibicao);
            TextView tipoContra = inf.findViewById(R.id.tipoContra);
            calculaCoimas(multa, pontos, inibicao, tipoContra);
        }

        return inf;
    }

    public void save(View view) {

    }

    private void calculaCoimas(TextView multa, TextView pontos, TextView inibicao, TextView tipoContra) {
        Pessoa p = new DataBase(getContext()).listaPerfilActivo();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(p.getAno_carta());
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(dateFormat.format(today));
            long diffInMillis = date2.getTime() - date1.getTime();
            long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            Log.i("Dias de carta",""+dateDiffInDays);

            if (dateDiffInDays > 1095 && !p.isProfissional()) {
                if (Pag_inicial.taxaTotal >= 0.5 && Pag_inicial.taxaTotal < 0.8 ) {
                    multa.setText("Multa: 250€ até 1250€.");
                    pontos.setText("Perda de 3 pontos.");
                    inibicao.setText("Até 1 ano s/conduzir.");
                    tipoContra.setText("Tipo: Contraordenação grave.");
                } else if (Pag_inicial.taxaTotal >= 0.8 && Pag_inicial.taxaTotal < 1.2 ) {
                    multa.setText("Multa 500€ até 2500€.");
                    pontos.setText("Perda de 5 pontos.");
                    inibicao.setText("Até 2 anos s/conduzir.");
                    tipoContra.setText("Tipo: Contraordenação muito grave.");
                } else {
                    multa.setText("Pena: Prisão até 1 ano.");
                    pontos.setText("Perda de 6 pontos.");
                    inibicao.setText("3 meses a 3 anos s/conduzir.");
                    tipoContra.setText("Tipo: CRIME.");
                }
            } else {
                if (Pag_inicial.taxaTotal >= 0.2 && Pag_inicial.taxaTotal < 0.5 ) {
                    multa.setText("Multa: 250€ até 1250€.");
                    pontos.setText("Perda de 3 pontos.");
                    inibicao.setText("Até 1 ano s/conduzir.");
                    tipoContra.setText("Tipo: Contraordenação grave.");
                } else if (Pag_inicial.taxaTotal >= 0.5 && Pag_inicial.taxaTotal < 1.2 ) {
                    multa.setText("Multa: 500€ até 2500€.");
                    pontos.setText("Perda de 5 pontos.");
                    inibicao.setText("Até 2 anos s/conduzir.");
                    tipoContra.setText("Tipo: Contraordenação muito grave.");
                } else {
                    multa.setText("Pena: Prisão até 1 ano.");
                    pontos.setText("Perda de 6 pontos.");;
                    inibicao.setText("Até 3 anos s/conduzir.");
                    tipoContra.setText("Tipo: CRIME.");
                }
            }
        } catch (ParseException e) {}

    }

}