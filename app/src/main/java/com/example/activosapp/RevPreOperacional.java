package com.example.activosapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.navigation.Navigation.findNavController;
import static com.example.activosapp.ui.crear_activo.CrearActivo.DATOS;



public class RevPreOperacional extends Fragment {

    private VolleyRP volley;
    private RequestQueue mRequest;

    public static ArrayList<String> listTipoAtivo2;
    public static HashMap<String, String> hashTipoActivo2;
    private static final String URL_TIPO_ACTIVO2 = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoActivo.php";

public Enviar envia;

    public RevPreOperacional() {
        // Required empty public constructor
    }

    public interface Enviar{

        void comunicar(String mensaje);
    }

    String tipoActivo2;
    String idTipoActivo2;
    View vista;

    Spinner spTipoActivo2;
    Button boton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         vista=inflater.inflate(R.layout.fragment_rev_pre_operacional, container, false);
         boton = vista.findViewById(R.id.button2);

         //Solicitudes de web service
        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();

         spTipoActivo2=vista.findViewById(R.id.spSelecTipotActivo);

         new GetTipoActivo().execute();

         boton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 findNavController(v).navigate(R.id.action_nav_RevPreOperacional_to_fragmentRevision);

             }
         });
        spTipoActivo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoActivo2 = parent.getSelectedItem().toString();
//                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                idTipoActivo2 = hashTipoActivo2.get(tipoActivo2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


            return vista;
        }

        //Spinner Tipo Activo
        public void poblarSpinnerTipoActivo(JSONObject datos) {

            try {
                hashTipoActivo2 = new HashMap<>();
                listTipoAtivo2 = new ArrayList<>();
                listTipoAtivo2.add("Seleccione");
                for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                    JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                    hashTipoActivo2.put(dato.getString("tip_tipo"), dato.getString("tip_id"));
                    listTipoAtivo2.add(dato.getString("tip_tipo"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, listTipoAtivo2);
            spinnerAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTipoActivo2.setAdapter(spinnerAdapter);
        }


    private class GetTipoActivo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
//            listTercero.clear();
            JsonObjectRequest solicitud = new JsonObjectRequest(URL_TIPO_ACTIVO2, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    poblarSpinnerTipoActivo(datos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.println(Log.WARN, "JOANYERROR", error.toString());
                }
            });

            VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }



}




