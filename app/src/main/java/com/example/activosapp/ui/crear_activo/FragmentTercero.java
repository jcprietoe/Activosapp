package com.example.activosapp.ui.crear_activo;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.activosapp.R;
import com.example.activosapp.VolleyRP;
import com.example.activosapp.ui.crear_activo.CrearActivo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.navigation.Navigation.findNavController;


public class FragmentTercero extends Fragment {


    public FragmentTercero() {
        // Required empty public constructor
    }


    private VolleyRP volley;
    private RequestQueue mRequest;
    // declaracion de variables
    View vista;
    Spinner spinnerdocu;
    Button btnGuardarTercero;
    EditText edtNombreTercero,edtNoDocTercero,edtEmailTercero,edtTelTercero;
    String tipoDocumento;
    public  static ArrayList<String> listPrueba;
    private static HashMap<String,String> hashTipoDocumento;
    private static final String TIPO_DOCUMENTO_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoDocumento.php";
    private static final String URL_REG_TERCERO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/registrar_tercero.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_fragment_tercero, container, false);

        spinnerdocu = vista.findViewById(R.id.spinnerdoc);
        btnGuardarTercero = vista.findViewById(R.id.btnGuardarTercero);
        edtNombreTercero = vista.findViewById(R.id.edtNombreTercero);
        edtNoDocTercero = vista.findViewById(R.id.edtNoDocTercero);
        edtEmailTercero = vista.findViewById(R.id.edtEmailTercero);
        edtTelTercero = vista.findViewById(R.id.edtTelTercero);





        btnGuardarTercero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtNombreTercero.getText().toString().trim().equals("")&&!edtNoDocTercero.getText().toString().trim().equals("")
                &&!edtTelTercero.getText().toString().trim().equals("")) {
                    volley = VolleyRP.getInstance(getContext());
                    mRequest = volley.getRequestQueue();
                    HashMap<String, String> hashDatos = new HashMap<>();
                    hashDatos.put("ter_nombre", edtNombreTercero.getText().toString().trim());
                    hashDatos.put("ter_tipid", tipoDocumento);
                    hashDatos.put("ter_iden", edtNoDocTercero.getText().toString().trim());
                    hashDatos.put("ter_email", edtEmailTercero.getText().toString().trim());
                    hashDatos.put("ter_telef", edtTelTercero.getText().toString().trim());
                    hashDatos.put("estado", "A");

                    JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_REG_TERCERO, new JSONObject(hashDatos), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject datos) {
                            Toast.makeText(vista.getContext(), "Guardado Exitosamente!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.println(Log.WARN, "JOANY:ERROR", error.toString());
                        }
                    });

                    VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
                    getFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getContext(), "LLenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //creacion de hilo para poblar spinner

        new GetTipoDocumento().execute();
        spinnerdocu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoDocumento = hashTipoDocumento.get(parent.getSelectedItem().toString());
                Toast.makeText(getContext(), tipoDocumento, Toast.LENGTH_SHORT).show();
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
            hashTipoDocumento = new HashMap<>();
            for(int i =0;i<datos.getJSONArray("datos").length();i++){
                JSONObject dato = (JSONObject) datos.getJSONArray("datos").get(i);
                listPrueba.add(dato.get("documento").toString());
                hashTipoDocumento.put(dato.getString("documento"),dato.getString("id"));
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
