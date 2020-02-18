package com.example.activosapp;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.activosapp.ui.crear_activo.CrearActivo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentTercero extends Fragment {


    public FragmentTercero() {
        // Required empty public constructor
    }


    private VolleyRP volley;
    private RequestQueue mRequest;
    // declaracion de variables
    View vista;
    Spinner spinnerdocu;
    EditText edtNombreTercero,edtNoDocTercero,edtEmailTercero,edtTelTercero;
    public  static ArrayList<String> listPrueba;
    private static final String TIPO_DOCUMENTO_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoDocumento.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_fragment_tercero, container, false);

        spinnerdocu = vista.findViewById(R.id.spinnerdoc);


        //creacion de hilo para poblar spinner

        new GetTipoDocumento().execute();
        spinnerdocu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        return vista;
    }



    public void poblarSpinnerTipoDocumento(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            listPrueba = new ArrayList<String>();
            for(int i =0;i<datos.getJSONArray("datos").length();i++){
                JSONObject dato = (JSONObject) datos.getJSONArray("datos").get(i);
                listPrueba.add(dato.get("documento").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listPrueba);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdocu.setAdapter(spinnerAdapter);
    }

    private class GetTipoDocumento extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            volley = VolleyRP.getInstance(getContext());
            mRequest = volley.getRequestQueue();

            JsonObjectRequest solicitud = new JsonObjectRequest(TIPO_DOCUMENTO_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    poblarSpinnerTipoDocumento(datos);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.println(Log.WARN, "JOANYDERROR", error.toString());
                }
            });

            VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //populateSpinner();
        }
    }

}
