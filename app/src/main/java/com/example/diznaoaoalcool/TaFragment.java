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

public class TaFragment extends Fragment {

    private boolean passou;

    private TA ta;
    private int primeiro;
    private static View inf;

    public TaFragment() {
    }

    public TaFragment(TA ta, int primeiro) {
        this.ta = ta;
        this.primeiro = primeiro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inf = inflater.inflate(R.layout.fragment_historico, container, false);
        TextView data = inf.findViewById(R.id.data_teste);
        data.setText("Teste realizado na data: "+ta.getData());
        if (primeiro == -1) {
            TableRow eliminar = inf.findViewById(R.id.eliminar_11);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_22);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_33);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_44);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_55);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_66);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_77);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.eliminar_88);
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
                eliminar = inf.findViewById(R.id.eliminar_44);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_55);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_66);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_77);
                eliminar.setVisibility(View.GONE);
                eliminar = inf.findViewById(R.id.eliminar_88);
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

            if (primeiro == 0) {
                TextView msg = inf.findViewById(R.id.txt_Historico2);
                msg.setText("Histórico:");
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

        Button apagarBtn = (Button) inf.findViewById(R.id.apagart);
        apagarBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("Cliquei em apagar","ID TA:"+ta.getId());
            }
        });

        return inf;
    }

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

}