package com.example.diznaoaoalcool;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.List;

public class HistoricoDetalhadoFragment extends Fragment {

    private int ta_id;
    private int primeiro;
    private int quantidade;
    private double resultado;
    private boolean jejum;
    private Bebida bebida;
    private Copo copo;



    private static View inf;

    public HistoricoDetalhadoFragment() {
    }

    public HistoricoDetalhadoFragment(int ta_id, double resultado, Bebida ba, Copo cp, int primeiro, int quantidade, boolean jejum) {
        this.ta_id = ta_id;
        this.resultado = resultado;
        this.bebida = ba;
        this.copo = cp;
        this.primeiro = primeiro;
        this.quantidade = quantidade;
        this.jejum = jejum;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inf = inflater.inflate(R.layout.fragment_historicodetalhado, container, false);

        Spinner dropdown = inf.findViewById(R.id.spinner_bebida1);
        final List<Bebida> listaBebidas = new DataBase(getContext()).listaBebidas();
        final List<Copo> listaCopos = new DataBase(getContext()).listaCopos();
        String[] items= new String[listaBebidas.size()];
        for (int i=0; i < listaBebidas.size(); i++) {
            items[i] = listaBebidas.get(i).getTipo();
        }

        Integer [] ClipCodesImage = new Integer[] {R.drawable.cerceja_light, R.drawable.cervejasagres, R.drawable.vinho, R.drawable.cocktail, R.drawable.shot, R.drawable.whisky, R.drawable.pontointerr};
        MyAdapterSpinner adapter = new MyAdapterSpinner(getContext(), R.layout.item_custom, items, ClipCodesImage);
        dropdown.setAdapter(adapter);

        int posicao = 0;
        for (int i=0 ; i < listaBebidas.size(); i++) {
            if (items[i].equalsIgnoreCase(bebida.getTipo())) {
                posicao = i;
            }
        }

        dropdown.setSelection(posicao);
        dropdown.setEnabled(false);

        dropdown = inf.findViewById(R.id.spinner_copo1);
        items= new String[listaCopos.size()];
        for (int i=0; i < listaCopos.size(); i++) {
            items[i] = listaCopos.get(i).getTipo();
        }

        ClipCodesImage = new Integer[] {R.drawable.coposhot, R.drawable.tacapony, R.drawable.copocock, R.drawable.copowhisky, R.drawable.copovinho, R.drawable.canecacerveja, R.drawable.pontointerr};
        adapter = new MyAdapterSpinner(getContext(), R.layout.item_custom, items, ClipCodesImage);
        dropdown.setAdapter(adapter);

        for (int i=0 ; i < listaCopos.size(); i++) {
            if (items[i].equalsIgnoreCase(copo.getTipo())) {
                posicao = i;
            }
        }

        dropdown.setSelection(posicao);
        dropdown.setEnabled(false);

        SeekBar graduacaoSeek = inf.findViewById(R.id.graduacao1);
        graduacaoSeek.setProgress(bebida.getGraduacao());
        graduacaoSeek.setEnabled(false);
        TextView txtV_graduacao = inf.findViewById(R.id.grad_int);
        txtV_graduacao.setText(""+bebida.getGraduacao()+" %");

        SeekBar volumeSeek = inf.findViewById(R.id.volume_consumido1);
        volumeSeek.setProgress(copo.getVolume());
        volumeSeek.setEnabled(false);
        TextView txtV_volume = inf.findViewById(R.id.volume_int);
        txtV_volume.setText(""+copo.getVolume()+" ml");

        SeekBar quantSeek = inf.findViewById(R.id.quantidade1);
        quantSeek.setProgress(quantidade);
        quantSeek.setEnabled(false);
        TextView txtV_quantidade = inf.findViewById(R.id.quant_int);
        txtV_quantidade.setText(quantidade + " copo(s)");

        CheckBox jejum_check = inf.findViewById(R.id.jejum1);

        if (jejum)
            jejum_check.setChecked(true);

        jejum_check.setEnabled(false);

        if (primeiro == 1) {
            TableRow eliminar = inf.findViewById(R.id.apag_2);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.apag_3);
            eliminar.setVisibility(View.GONE);
        } else if (primeiro == 0){
            TableRow eliminar = inf.findViewById(R.id.apag_1);
            eliminar.setVisibility(View.GONE);
            TextView resultado_txt = inf.findViewById(R.id.resultado_tested);
            DecimalFormat f = new DecimalFormat("##.00");

            if (resultado < 1) {
                resultado_txt.setText("Resultado: 0"+f.format(resultado)+ " g/l.");
            } else {
                resultado_txt.setText("Resultado: "+f.format(resultado)+ " g/l.");
            }

            Button apagarTeste = (Button) inf.findViewById(R.id.btn_eliminar);
            apagarTeste.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                }
            });
        } else {
            TableRow eliminar = inf.findViewById(R.id.apag_1);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.apag_2);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.apag_3);
            eliminar.setVisibility(View.GONE);
        }

        return inf;
    }

    protected class MyAdapterSpinner extends ArrayAdapter {

        Integer[] Image;
        String[] Text;

        public MyAdapterSpinner(Context context, int resource, String[] text, Integer[] image) {
            super(context, resource, text);
            Image = image;
            Text = text;
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.item_custom, parent, false);

            //Set Custom View
            TextView tv = (TextView)view.findViewById(R.id.textView);
            ImageView img = (ImageView) view.findViewById(R.id.imageView);

            tv.setText(Text[position]);
            img.setImageResource(Image[position]);

            return view;
        }

        @Override
        public View getDropDownView(int position,View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

}