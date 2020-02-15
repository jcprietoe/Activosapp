package com.example.activosapp.ui.crear_activo;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.activosapp.R;
import com.example.activosapp.VolleyRP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CrearActivo extends Fragment {

    public CrearActivo() {

    }
    private VolleyRP volley;
    private RequestQueue mRequest;
    // declaracion de variables

    ArrayList<String>listPrueba;
    private String URL_LISTA_Documento = "http://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoDocumento.php";
    ArrayAdapter<String> aaTipoDocumento;
    View vista;
    Spinner spinnerdocu;
    String[] tpodocumento;
    EditText fechamatricula, fechafabricacion, edtvalorreal, edtcodigointerno, edtnoplaca, edtnodoctercero, edtemailtercero, edtteltercero;
    EditText edtmodelo, edtreferencia, edtlinea, edtserial, edtserialmotor, edtserialpartes, edtnombretercero, edtdescripcion, edtubicacion;
    RadioButton rbtnsi, rbtnno;
    Button btn_guardar_registro;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_crear_activo, container, false);

        //llama de variable
        spinnerdocu = vista.findViewById(R.id.spinnerdoc);
        tpodocumento = getResources().getStringArray(R.array.tipo_documentos);
        fechamatricula = vista.findViewById(R.id.edtfmatricula);
        fechafabricacion = vista.findViewById(R.id.edtfabricacion);

        edtnoplaca = vista.findViewById(R.id.edtnoplaca);
        edtnodoctercero = vista.findViewById(R.id.edtnodoctercero);
        edtemailtercero = vista.findViewById(R.id.edtemailtercero);
        edtteltercero = vista.findViewById(R.id.edtteltercero);
        edtmodelo = vista.findViewById(R.id.edtreferencia);
        edtlinea = vista.findViewById(R.id.edtlinea);
        edtlinea = vista.findViewById(R.id.edtlinea);
        edtserial = vista.findViewById(R.id.edtserial);
        edtserialmotor = vista.findViewById(R.id.edtserialmotor);
        edtserialpartes = vista.findViewById(R.id.edtserialpartes);
        edtnombretercero = vista.findViewById(R.id.edtnombretercero);
        edtdescripcion = vista.findViewById(R.id.edtdescripcion);
        edtubicacion = vista.findViewById(R.id.edtubicacion);

        btn_guardar_registro = vista.findViewById(R.id.btn_guardar_registro);


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

        //boton guardar
        btn_guardar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "entro aqui", Toast.LENGTH_SHORT).show();

            }
        });


        //metodo para textview fecha de matricula
        fechamatricula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.edtfmatricula:
                        showDatePickerDialog();
                        break;


                }
            }

            private void showDatePickerDialog() {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + " / " + (month + 1) + " / " + year;
                        fechamatricula.setText(selectedDate);
                    }
                });

                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        //metodo fecha año de fabricacion
        fechafabricacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.edtfabricacion:
                        showDatePickerDialog();
                        break;


                }
            }

            //calendario

            private void showDatePickerDialog() {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + " / " + (month + 1) + " / " + year;
                        fechafabricacion.setText(selectedDate);
                    }
                });

                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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
                listPrueba.add(dato.get("usuario").toString());
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

            JsonObjectRequest solicitud = new JsonObjectRequest(URL_LISTA_Documento, null, new Response.Listener<JSONObject>() {
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
