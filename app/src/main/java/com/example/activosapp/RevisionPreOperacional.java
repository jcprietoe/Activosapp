package com.example.activosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class RevisionPreOperacional extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_pre_operacional);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        NavigationView navigationView = drawer.findViewById(R.id.nav_view2);
        View header = navigationView.getHeaderView(0);
        TextView textUsuario = header.findViewById(R.id.recibe_nombre_usuario);
        SharedPreferences preferences= getSharedPreferences("preferencia_usuario", Context.MODE_PRIVATE);
        Log.println(Log.INFO,"DATOSJoany", preferences.toString());
        textUsuario.setText(preferences.getString("usuario",""));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_crear_activo, R.id.nav_slideshow,R.id.nav_sub_menu, R.id.nav_sitioweb, R.id.nav_VistaActivo, R.id.nav_Tercero)
                .setDrawerLayout(drawer)
                .build();
//        NavController navController = Navigation.findNavController(this, );
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
    }
}
