package com.example.activosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private VolleyRP volley;
    private RequestQueue mRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusuario=findViewById(R.id.txtusuario);
        edtpassword=findViewById(R.id.txtpassword);
        btnLogin=findViewById(R.id.btnentrar);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario("http://www.gerenciandomantenimiento.com/activos/mantenimientoapp/Login_GETID.php?nombre=",edtusuario.getText().toString());
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
                    Toast.makeText(Login.this, "Logueo existoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MenuServicio.class);
                    startActivity(intent);

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
}
