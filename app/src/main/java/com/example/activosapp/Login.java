package com.example.activosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
    TextView  txtTerminosCondiciones;
    Button btnLogin;
    RadioButton rbtnsesion;
    public static String usuario;
    public static String nombreUsuario;
    public static String cargoUsuario;
    public static String idEmpresa;
    private VolleyRP volley;
    private RequestQueue mRequest;
    private boolean is_activado_rbt;
    public static final String STRING_PREFERENCES = "mi_paquete_preferences";
    public static final String PREFERENCES_USUARIO = "preferencia_usuario";
    public static final String KEY_PREFERENCES_NOMBRE = "nombre";
    public static final String KEY_PREFERENCES_CARGO = "cargo";
    public static final String KEY_PREFERENCES_IDEMPRESA = "empresaId";
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
        txtTerminosCondiciones = findViewById(R.id.txtTerminosCondiciones);

        SpannableString content = new SpannableString(txtTerminosCondiciones.getText());
        content.setSpan(new UnderlineSpan(), 0, txtTerminosCondiciones.length(), 0);
        txtTerminosCondiciones.setText(content);
        txtTerminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.gerenciandomantenimiento.com"));
                startActivity(i);
            }
        });

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
                    usuario = resultJson.getString("usuario");
                    nombreUsuario = resultJson.getString("nombre");
                    cargoUsuario= resultJson.getString("cargo");
                    idEmpresa = resultJson.getString("emp_id");
                    guardarPreferenciasString();
                    guardarPreferencias();

                    Intent intent = new Intent(getApplicationContext(),MenuServicio.class);

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
        preferences.edit().putString(KEY_PREFERENCES_USUARIO,usuario).apply();
        preferences.edit().putString(KEY_PREFERENCES_NOMBRE,nombreUsuario).apply();
        preferences.edit().putString(KEY_PREFERENCES_CARGO,cargoUsuario).apply();
        preferences.edit().putString(KEY_PREFERENCES_IDEMPRESA,idEmpresa).apply();
    }

//    private String obtenerStringUsuario(){
//        SharedPreferences preferences=getSharedPreferences(PREFERENCES_USUARIO, Context.MODE_PRIVATE);
//        return preferences.getString(KEY_PREFERENCES_USUARIO,nombreUsuario);
//    }
    private boolean obtenerEstadoButton(){
        SharedPreferences preferences=getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
       return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON,false);
    }







}
