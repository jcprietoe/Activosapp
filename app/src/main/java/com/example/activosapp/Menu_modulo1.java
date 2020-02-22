package com.example.activosapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static androidx.navigation.Navigation.createNavigateOnClickListener;
import static androidx.navigation.Navigation.findNavController;


public class Menu_modulo1 extends Fragment {


    public Menu_modulo1() {
        // Required empty public constructor
    }
    View vista;
    Button btncrearactivo, btnrevpreoperacional;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_menu_modulo1, container, false);

        btncrearactivo=vista.findViewById(R.id.btn_crear_activo);
        btncrearactivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(R.id.boton_crear_activo_to_crear_activo);
                Toast.makeText(getContext(),"Crear activo",Toast.LENGTH_LONG).show();
            }
        });
        btnrevpreoperacional=vista.findViewById(R.id.btn_revision_preoperacional);
        btnrevpreoperacional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(R.id.action_nav_sub_menu_to_revisionPreOperacional);
                Toast.makeText(getContext(),"Revision Preoperacional",Toast.LENGTH_LONG).show();
            }
        });










        return vista;
    }

}
