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

    public static HashMap<String, String> hashCiudad;
    public static HashMap<String, String> hashTipoActivo2;
    public static HashMap<String, String> hashDepartamento;
    public static HashMap<String, String> hashSelectActivo;
    public static ArrayList<String> listCiudad;
    public static ArrayList<String> listSelectActivo;
    public static ArrayList<String> listTipoAtivo2;
    public static ArrayList<String> listDepartamento;

    private static final String URL_TIPO_ACTIVO2 = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoActivo.php";
    private static final String URL_SELECT_ACTIVO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerSelectActivo.php?act_tipoid=";
    private static final String URL_DEPARTAMENTO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerDepartamento.php";
    private static final String URL_CIUDAD = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerCiudad.php?ciu_depid=";
public Enviar envia;

    public RevPreOperacional() {
        // Required empty public constructor
    }

    public interface Enviar{

        void comunicar(String mensaje);
    }
    String SeleccionarActivo;
    String idSeleccionarActivo;
    String tipoActivo2;
    String idTipoActivo2;
    String idDepartamento;
    String departamento1;
    String SeleccionarCiudad;
    String idSeleccionarCiudad;
    View vista;

    Spinner spTipoActivo2,spSelectActivo, spDepartamento, spCiudad;

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
         spSelectActivo=vista.findViewById(R.id.spSelectActivo);
         spDepartamento=vista.findViewById(R.id.spDepartamento);
         spCiudad=vista.findViewById(R.id.spCiudad);

         new GetTipoActivo().execute();
         new  GetDepartamento().execute();


         boton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //pasar dato

                 FragmentRevision fragmentRevision = new FragmentRevision();
                 Bundle args = new Bundle();
                 args.putString("enviaTipoActivo", idTipoActivo2);
                 fragmentRevision.setArguments(args);


                 findNavController(v).navigate(R.id.action_nav_RevPreOperacional_to_fragmentRevision);


             }
         });


        spTipoActivo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoActivo2 = parent.getSelectedItem().toString();
                idTipoActivo2 = hashTipoActivo2.get(tipoActivo2);
                new GetSelectActivo().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSelectActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SeleccionarActivo = parent.getSelectedItem().toString();
                idSeleccionarActivo = hashSelectActivo.get(SeleccionarActivo);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departamento1 = parent.getSelectedItem().toString();
               idDepartamento = hashDepartamento.get(departamento1);
                new GetCiudad().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SeleccionarCiudad = parent.getSelectedItem().toString();
                idSeleccionarCiudad = hashCiudad.get(SeleccionarCiudad);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



            return vista;
        }

        //Spinner Ciudad

    public void poblarSpinnerCiudad(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            hashCiudad = new HashMap<>();
            listCiudad = new ArrayList<>();
            listCiudad.add("Seleccione");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashCiudad.put(dato.getString("ciu_nombre"), dato.getString("ciu_id"));
                listCiudad.add(dato.getString("ciu_nombre"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listCiudad);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spCiudad .setAdapter(spinnerAdapter);
    }


    private class GetCiudad extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            if (null != idDepartamento) {
                JsonObjectRequest solicitud = new JsonObjectRequest(URL_CIUDAD + idDepartamento, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject datos) {
                        poblarSpinnerCiudad(datos);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.WARN, "JOANYERROR", error.toString());
                    }
                });

                VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //populateSpinner();
        }
    }


        //Spinner Departamento

    public void poblarSpinnerDepartamento(JSONObject datos) {

        try {
            hashDepartamento = new HashMap<>();
            listDepartamento = new ArrayList<>();
            listDepartamento.add("Seleccione");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashDepartamento.put(dato.getString("dep_nombre"), dato.getString("dep_id"));
                listDepartamento.add(dato.getString("dep_nombre"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listDepartamento);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(spinnerAdapter);
    }

    private class GetDepartamento extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

                JsonObjectRequest solicitud = new JsonObjectRequest(URL_DEPARTAMENTO , null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject datos) {
                        poblarSpinnerDepartamento(datos);
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
            //populateSpinner();
        }
    }


        //Spinner SeletActivo

    public void poblarSpinnerSelectActivo(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            hashSelectActivo = new HashMap<>();
            listSelectActivo = new ArrayList<>();
            listSelectActivo.add("Seleccione");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashSelectActivo.put(dato.getString("act_nombre"), dato.getString("act_id"));
                listSelectActivo.add(dato.getString("act_nombre"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listSelectActivo);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectActivo.setAdapter(spinnerAdapter);
    }


    private class GetSelectActivo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            if (null != idTipoActivo2) {
                JsonObjectRequest solicitud = new JsonObjectRequest(URL_SELECT_ACTIVO + idTipoActivo2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject datos) {
                        poblarSpinnerSelectActivo(datos);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.WARN, "JOANYERROR", error.toString());
                    }
                });

                VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //populateSpinner();
        }
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




