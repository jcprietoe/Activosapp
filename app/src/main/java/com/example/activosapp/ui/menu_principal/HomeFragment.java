package com.example.activosapp.ui.menu_principal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.activosapp.R;

import static androidx.navigation.Navigation.findNavController;

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
                findNavController(v).navigate(R.id.action_nav_home_to_nav_sub_menu);
                Toast.makeText(getContext(),"modulo 1.",Toast.LENGTH_LONG).show();
            }
        });
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //modulo1.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_sub_menu));





    }
}