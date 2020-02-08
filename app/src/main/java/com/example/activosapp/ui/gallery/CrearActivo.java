package com.example.activosapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.activosapp.R;

public class CrearActivo extends Fragment {

    public CrearActivo(){

    }

    View view;
    Spinner spinnerdocu;
    TextView txtdocument;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crear_activo, container, false);













        return view;
    }
}