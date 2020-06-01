package com.example.diznaoaoalcool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inf = inflater.inflate(R.layout.fragment_settings, container, false);
        final List<Pessoa> listaPessoas = new DataBase(getContext()).listaPerfis();
        final Pessoa pessoa = new DataBase(getContext()).listaPerfilActivo();

        loadActiveProfile(inf, pessoa);
        loadProfiles(inf, listaPessoas);

        Button botaoEditar = (Button) inf.findViewById(R.id.btn_edit);
        botaoEditar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editProfile(pessoa);
            }
        });

        Button botaoMudar = (Button) inf.findViewById(R.id.btn_change_profile);
        botaoMudar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeProfile(inf, listaPessoas);
            }
        });

        Button botaoCriar = (Button) inf.findViewById(R.id.btn_add_profile);
        botaoCriar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addProfile();
            }
        });
        return inf;
    }



    private void loadActiveProfile(View inf, Pessoa pessoa){
        TextView texto = inf.findViewById(R.id.textView16);
        texto.setText("  " + pessoa.getNome() + " (Peso: " + pessoa.getPeso() + " | " + "Idade: " + pessoa.getIdade() + ")");
    }

    private void loadProfiles(View inf, List<Pessoa> listaPessoas){
        Spinner profiles = inf.findViewById(R.id.spinner);
        String[] profileList = new String[listaPessoas.size()];

        for (int i = 0; i < listaPessoas.size(); i++) {
            Pessoa temp = listaPessoas.get(i);
            profileList[i] = temp.getNome() + " (Peso: " + temp.getPeso() + " | " + "Idade: " + temp.getIdade() + ")";
        }
        ArrayAdapter <String> adapter = new ArrayAdapter <> (getContext(), R.layout.support_simple_spinner_dropdown_item, profileList);
        profiles.setAdapter(adapter);
    }

    private void editProfile(Pessoa pessoa){
        Intent intent = new Intent(getContext(), NoProfile.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void addProfile(){
        startActivity(new Intent(getContext(), NoProfile.class));
    }

    private void changeProfile(View inf, List<Pessoa> pessoas){
        Spinner profiles = inf.findViewById(R.id.spinner);
    }
}
