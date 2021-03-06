package com.example.activosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MenuServicio extends AppCompatActivity{


    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_servicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView txtNombreUsuario = header.findViewById(R.id.recibe_nombre_usuario);
        TextView txtCargo = header.findViewById(R.id.txtCargoUsuario);
        TextView txtNombreEmpresa = header.findViewById(R.id.txtNombreEmpresa);
        SharedPreferences preferences= getSharedPreferences("preferencia_usuario",Context.MODE_PRIVATE);
        Log.println(Log.INFO,"DATOSJoany", preferences.toString());
        txtNombreUsuario.setText(preferences.getString(Login.KEY_PREFERENCES_NOMBRE,""));
        txtCargo.setText(preferences.getString(Login.KEY_PREFERENCES_CARGO,""));
        txtNombreEmpresa.setText("Custom Maintenance Software CMS");

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_crear_activo, R.id.nav_Modulo2,R.id.nav_sub_menu, R.id.nav_sitioweb, R.id.nav_VistaActivos, R.id.nav_RevPreOperacional, R.id.nav_Tercero)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater cerrarSesion;
        cerrarSesion=getMenuInflater();
        cerrarSesion.inflate(R.menu.menu_servicio, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SharedPreferences preferencesBoton = getSharedPreferences(Login.STRING_PREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences preferencesUsuario = getSharedPreferences(Login.PREFERENCES_USUARIO,Context.MODE_PRIVATE);
                preferencesBoton.edit().putBoolean(Login.PREFERENCE_ESTADO_BUTTON,false).apply();
                preferencesUsuario.edit().putString(Login.KEY_PREFERENCES_NOMBRE,"").apply();
                preferencesUsuario.edit().putString(Login.KEY_PREFERENCES_CARGO,"").apply();
                preferencesUsuario.edit().putString(Login.KEY_PREFERENCES_USUARIO,"").apply();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

}
