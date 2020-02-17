package com.example.activosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    EditText edtusuario, edtpassword;
    Button btnLogin;
    RadioButton rbtnsesion;
    public static String nombreUsuario;
    private VolleyRP volley;
    private RequestQueue mRequest;
    private boolean is_activado_rbt;
    public static final String STRING_PREFERENCES = "mi_paquete_preferences";
    public static final String PREFERENCES_USUARIO = "preferencia_usuario";
    public static final String KEY_PREFERENCES_USUARIO = "usuario";
    public static final String PREFERENCE_ESTADO_BUTTON = "estado.button.sesion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (obtenerEstadoButton()){
            Intent intent = new Intent(getApplicationContext(),MenuServicio.class);
            startActivity(intent);
            finish();

        }

        edtusuario=findViewById(R.id.txtusuario);
        edtpassword=findViewById(R.id.txtpassword);
        btnLogin=findViewById(R.id.btnentrar);
        rbtnsesion=findViewById(R.id.rbtnsesion);

        is_activado_rbt =rbtnsesion.isChecked();// desactivado
        rbtnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_activado_rbt){
                    rbtnsesion.setChecked(false);
                }
                is_activado_rbt=rbtnsesion.isChecked();

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarUsuario("https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/Login_GETID.php?usuario=",edtusuario.getText().toString());
            }
        });

    }

    private void validarUsuario(String URL, String usuario) {
        URL += usuario;
        solicitudJson(URL);
    }

    public  void solicitudJson(String URL){
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        JsonObjectRequest solicitud = new JsonObjectRequest(URL,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                verificarPassword(datos);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }
    public  void verificarPassword(JSONObject datos){
        try {
            String resultado = datos.getString("resultado");
            if(existeUsuario(resultado)){
                JSONObject resultJson=  datos.getJSONObject("datos");
                if(resultJson.getString("password").equals(edtpassword.getText().toString())){


                    nombreUsuario = edtusuario.getText().toString();
                    guardarPreferenciasString();
                    guardarPreferencias();

                    Toast.makeText(Login.this, "Logueo existoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MenuServicio.class);
                    /*Bundle pasardato=new Bundle();
                    pasardato.putString("nombre_usuario", String.valueOf(edtusuario));
                    intent.putExtras(pasardato);*/

                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(Login.this, "contraseña errada", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Login.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
            }
        }catch(JSONException e){
        }
    }

    public boolean existeUsuario(String resultado){
        return resultado.equals("CC");
    }


    private void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON,rbtnsesion.isChecked()).apply();

    }
    private void guardarPreferenciasString(){
        SharedPreferences preferences=getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_PREFERENCES_USUARIO,nombreUsuario).apply();

    }

    private String obtenerStringUsuario(){
        SharedPreferences preferences=getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
        return preferences.getString(KEY_PREFERENCES_USUARIO,nombreUsuario);
    }
    private boolean obtenerEstadoButton(){
        SharedPreferences preferences=getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
       return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON,false);
    }







}
