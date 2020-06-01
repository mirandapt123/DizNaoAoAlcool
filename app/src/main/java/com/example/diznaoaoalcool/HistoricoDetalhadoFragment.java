package com.example.diznaoaoalcool;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.List;

public class HistoricoDetalhadoFragment extends Fragment {

    private int ta_id;
    private int primeiro;
    private int quantidade;
    private double resultado;
    private BA_TA bc;



    private static View inf;

    public HistoricoDetalhadoFragment() {
    }

    //saber se é a primeira ou ultima bebida | primeiro = 1, ultimo = 0
    public HistoricoDetalhadoFragment(int ta_id, double resultado, BA_TA bc, int primeiro, int quantidade) {
        this.ta_id = ta_id;
        this.resultado = resultado;
        this.bc = bc;
        this.primeiro = primeiro;
        this.quantidade = quantidade;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inf = inflater.inflate(R.layout.fragment_historicodetalhado, container, false);

        Spinner dropdown = inf.findViewById(R.id.spinner_bebida1);
        //obtem a lista de bebidas na base de dados (genéricas)
        final List<Bebida> listaBebidas = new DataBase(getContext()).listaBebidas();
        //obtem a lista de copos na base de dados (genéricas)
        final List<Copo> listaCopos = new DataBase(getContext()).listaCopos();
        //coloca as bebidas (genéricas) no spinner
        String[] items= new String[listaBebidas.size()];
        for (int i=0; i < listaBebidas.size(); i++) {
            items[i] = listaBebidas.get(i).getTipo();
        }

        //coloca as imagens para depois associar ao spinner
        Integer [] ClipCodesImage = new Integer[] {R.drawable.cerceja_light, R.drawable.cervejasagres, R.drawable.vinho, R.drawable.cocktail, R.drawable.shot, R.drawable.whisky, R.drawable.pontointerr};
        MyAdapterSpinner adapter = new MyAdapterSpinner(getContext(), R.layout.item_custom, items, ClipCodesImage);
        dropdown.setAdapter(adapter);

        //verifica em qual posição está a bebida do teste
        int posicao = 0;
        for (int i=0 ; i < listaBebidas.size(); i++) {
            if (items[i].equalsIgnoreCase(bc.getB_tipo())) {
                posicao = i;
            }
        }

        //coloca o spinner com a bebida do teste seleciona e retira os eventos ao mesmo (disable)
        dropdown.setSelection(posicao);
        dropdown.setEnabled(false);

        //faz exactamente o mesmo, mas para os copos
        dropdown = inf.findViewById(R.id.spinner_copo1);
        items= new String[listaCopos.size()];
        for (int i=0; i < listaCopos.size(); i++) {
            items[i] = listaCopos.get(i).getTipo();
        }

        ClipCodesImage = new Integer[] {R.drawable.coposhot, R.drawable.tacapony, R.drawable.copocock, R.drawable.copowhisky, R.drawable.copovinho, R.drawable.canecacerveja, R.drawable.pontointerr};
        adapter = new MyAdapterSpinner(getContext(), R.layout.item_custom, items, ClipCodesImage);
        dropdown.setAdapter(adapter);

        for (int i=0 ; i < listaCopos.size(); i++) {
            if (items[i].equalsIgnoreCase(bc.getC_tipo())) {
                posicao = i;
            }
        }

        dropdown.setSelection(posicao);
        dropdown.setEnabled(false);

        // coloca a graduação da bebida do teste e coloca disabled
        SeekBar graduacaoSeek = inf.findViewById(R.id.graduacao1);
        graduacaoSeek.setProgress(bc.getGraduacao());
        graduacaoSeek.setEnabled(false);
        TextView txtV_graduacao = inf.findViewById(R.id.grad_int);
        txtV_graduacao.setText(""+bc.getGraduacao()+" %");

        // coloca o volume que do copo do teste e coloca disabled
        SeekBar volumeSeek = inf.findViewById(R.id.volume_consumido1);
        volumeSeek.setProgress(bc.getVolume());
        volumeSeek.setEnabled(false);
        TextView txtV_volume = inf.findViewById(R.id.volume_int);
        txtV_volume.setText(""+bc.getVolume()+" ml");

        // coloca a quantidade e coloca disabled
        SeekBar quantSeek = inf.findViewById(R.id.quantidade1);
        quantSeek.setProgress(quantidade);
        quantSeek.setEnabled(false);
        TextView txtV_quantidade = inf.findViewById(R.id.quant_int);
        txtV_quantidade.setText(quantidade + " copo(s)");

        //Se for primeiro
        if (primeiro == 1) {
            TableRow eliminar = inf.findViewById(R.id.apag_2);
            eliminar.setVisibility(View.GONE);
            eliminar = inf.findViewById(R.id.apag_3);
            eliminar.setVisibility(View.GONE);
        //Se for último
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

            //evento para apagar o teste
            Button apagarTeste = (Button) inf.findViewById(R.id.btn_eliminar);
            apagarTeste.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    eliminarHistorico("Tem a certeza que deseja apagar este teste?", ta_id);
                }
            });
        // Se não for nenhum
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

    //elimina o teste selecionado
    private void eliminarHistorico(String msg, final int idTa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pim Pam Pum");
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_history);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                new DataBase(getContext()).delete1History(idTa);
                Toast.makeText( getContext(), "O teste foi eliminado do histórico com sucesso.", Toast.LENGTH_LONG ).show();
                refreshActivity(0);

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

    //coloca o spinner costumizado com imagem ao lado | classe tirada da internet :)
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

            //Coloca a custom view no spiiner
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