package com.example.activosapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.pm.PermissionInfoCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.activosapp.Menu_modulo1;
import com.example.activosapp.R;
import com.example.activosapp.ui.sitioweb.WebFragment;

public class HomeFragment extends Fragment {

    public HomeFragment(){

    }

    View vista;  //puente para reconocer el metodo
    Button modulo1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_home, container, false);


        modulo1=vista.findViewById(R.id.btnmodulo1);//accion modulo 1
        modulo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"hola mundo",Toast.LENGTH_LONG).show();


            }


        });






        return vista;
    }
}