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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BebidaFragment extends Fragment {

    private String quantidade;
    private String graduacao;
    private int calcula;


    private static View inf;

    public BebidaFragment() {
    }

    public BebidaFragment(int calcula) {
        this.calcula = calcula;
    }

    public BebidaFragment BebidaFragment(String aQuantidade, String aGraduacao) {
        BebidaFragment fragment = new BebidaFragment();
        Bundle args = new Bundle();
        args.putString(quantidade, aQuantidade);
        args.putString(graduacao, aGraduacao);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quantidade = getArguments().getString(quantidade);
            graduacao = getArguments().getString(graduacao);
        }
    }

    public String getQuantidade() {
        return quantidade;
    }

    public String getGraduacao() {
        return graduacao;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            quantidade = getArguments().getString(quantidade);
            graduacao = getArguments().getString(graduacao);
        }
        final View inf = inflater.inflate(R.layout.fragment_bebida, container, false);

        Spinner dropdown = inf.findViewById(R.id.spinner_bebida);
        final List<Bebida> listaBebidas = new DataBase(getContext()).listaBebidas();
        final List<Copo> listaCopos = new DataBase(getContext()).listaCopos();
        String[] items= new String[listaBebidas.size()];
        for (int i=0; i < listaBebidas.size(); i++) {
            items[i] = listaBebidas.get(i).getTipo();
        }

        Integer [] ClipCodesImage = new Integer[] {R.drawable.cerceja_light, R.drawable.cervejasagres, R.drawable.vinho, R.drawable.cocktail, R.drawable.shot, R.drawable.whisky, R.drawable.pontointerr};
        MyAdapterSpinner adapter = new MyAdapterSpinner(getContext(), R.layout.item_custom, items, ClipCodesImage);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SeekBar simpleSeekBar = inf.findViewById(R.id.graduacao);
                simpleSeekBar.setProgress(listaBebidas.get(position).getGraduacao());
                TextView txtV_graduacao = inf.findViewById(R.id.textView11);
                txtV_graduacao.setText(""+listaBebidas.get(position).getGraduacao()+" %");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        dropdown = inf.findViewById(R.id.spinner_copo);
        items= new String[listaCopos.size()];
        for (int i=0; i < listaCopos.size(); i++) {
            items[i] = listaCopos.get(i).getTipo();
        }

        ClipCodesImage = new Integer[] {R.drawable.coposhot, R.drawable.tacapony, R.drawable.copocock, R.drawable.copowhisky, R.drawable.copovinho, R.drawable.canecacerveja, R.drawable.pontointerr};
        adapter = new MyAdapterSpinner(getContext(), R.layout.item_custom, items, ClipCodesImage);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SeekBar volumeSeek = inf.findViewById(R.id.volume_consumido);
                volumeSeek.setProgress(listaCopos.get(position).getVolume());
                TextView txtV_volume = inf.findViewById(R.id.textView9);
                txtV_volume.setText(""+listaCopos.get(position).getVolume()+" ml");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        if (calcula == 1) {
            Button btn = inf.findViewById(R.id.calcula);
            btn.setVisibility(View.GONE);
        }

        SeekBar graduacaoSeek = inf.findViewById(R.id.graduacao);
        graduacaoSeek.setProgress(listaBebidas.get(0).getGraduacao());
        TextView txtV_graduacao = inf.findViewById(R.id.textView11);
        txtV_graduacao.setText(""+listaBebidas.get(0).getGraduacao()+" %");

        SeekBar volumeSeek = inf.findViewById(R.id.volume_consumido);
        volumeSeek.setProgress(listaCopos.get(0).getVolume());
        TextView txtV_volume = inf.findViewById(R.id.textView9);
        txtV_volume.setText(""+listaCopos.get(0).getVolume()+" ml");

        SeekBar quantSeek = inf.findViewById(R.id.quantidade);
        TextView txtV_quantidade = inf.findViewById(R.id.volume_12);
        txtV_quantidade.setText("1 copo(s)");
        quantSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                TextView txtV_quantidade = inf.findViewById(R.id.volume_12);
                txtV_quantidade.setText(""+progress+" copo(s)");
            }
        });

        volumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                TextView txtV_volume = inf.findViewById(R.id.textView9);
                txtV_volume.setText(""+progress+" ml");
            }
        });

        graduacaoSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                TextView txtV_graduacao = inf.findViewById(R.id.textView11);
                txtV_graduacao.setText(""+progress+" %");
            }
        });

        return inf;
    }

    public void calculaTaxa(View view) {
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