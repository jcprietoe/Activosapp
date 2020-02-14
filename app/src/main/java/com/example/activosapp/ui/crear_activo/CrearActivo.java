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

import com.example.activosapp.Login;
import com.example.activosapp.R;
import com.example.activosapp.ui.ServiceHandler;
import com.example.activosapp.ui.TipoDocumento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CrearActivo extends Fragment {

    public CrearActivo(){

    }

// declaracion de variables
    TipoDocumento tipoDocumento;
    ArrayList <TipoDocumento> tipoDocumentoList;
    private String URL_LISTA_Documento = "http://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoDocumento.php?id=";
    ArrayAdapter<String> aaTipoDocumento;
    View vista;
    Spinner spinnerdocu;
    String[] tpodocumento;
    EditText fechamatricula, fechafabricacion,edtvalorreal,edtcodigointerno,edtnoplaca,edtnodoctercero,edtemailtercero,edtteltercero;
    EditText edtmodelo,edtreferencia, edtlinea,edtserial,edtserialmotor,edtserialpartes,edtnombretercero,edtdescripcion,edtubicacion;
    RadioButton rbtnsi, rbtnno;
    Button btn_guardar_registro;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        vista = inflater.inflate(R.layout.fragment_crear_activo, container, false);

         //llama de variable
        spinnerdocu=vista.findViewById(R.id.spinnerdoc);
        tpodocumento= getResources().getStringArray(R.array.tipo_documentos);
        fechamatricula=vista.findViewById(R.id.edtfmatricula);
        fechafabricacion=vista.findViewById(R.id.edtfabricacion);
        edtcodigointerno=vista.findViewById(R.id.edtcodigointerno);
        edtnoplaca=vista.findViewById(R.id.edtnoplaca);
        edtnodoctercero=vista.findViewById(R.id.edtnodoctercero);
        edtemailtercero=vista.findViewById(R.id.edtemailtercero);
        edtteltercero=vista.findViewById(R.id.edtteltercero);
        edtmodelo=vista.findViewById(R.id.edtreferencia);
        edtlinea=vista.findViewById(R.id.edtlinea);
        edtlinea=vista.findViewById(R.id.edtlinea);
        edtserial=vista.findViewById(R.id.edtserial);
        edtserialmotor=vista.findViewById(R.id.edtserialmotor);
        edtserialpartes=vista.findViewById(R.id.edtserialpartes);
        edtnombretercero=vista.findViewById(R.id.edtnombretercero);
        edtdescripcion=vista.findViewById(R.id.edtdescripcion);
        edtubicacion=vista.findViewById(R.id.edtubicacion);
        rbtnsi=vista.findViewById(R.id.rbtnsi);
        rbtnno=vista.findViewById(R.id.rbtnno);
        btn_guardar_registro=vista.findViewById(R.id.btn_guardar_registro);



        //spinner

       //ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),
                //R.array.tipo_documentos, android.R.layout.simple_spinner_item);
 //       aaTipoDocumento = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, opcTipoDocumento);
 //       aaTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 //       spinnerdocu.setAdapter(aaTipoDocumento);

 //metodo de accion para el spinner crea conflicto
        //new getTipoDocumento().execute();
        //populateSpinner();
        init();
       spinnerdocu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
           }
       });

        //boton guardar
        btn_guardar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"entro aqui",Toast.LENGTH_SHORT).show();

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

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < tipoDocumentoList.size(); i++) {
            lables.add(tipoDocumentoList.get(i).getNombre());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, lables);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerdocu.setAdapter(spinnerAdapter);


    }

    public void init(){

        Toast.makeText(getContext(),"entro al metodo init",Toast.LENGTH_SHORT).show();
        ServiceHandler jsonParser = new ServiceHandler();
        //String json = jsonParser.makeServiceCall(URL_LISTA_Documento, ServiceHandler.GET);
    }

    private class getTipoDocumento extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_LISTA_Documento, ServiceHandler.GET);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {

                        JSONArray documento = jsonObj.getJSONArray("documento");

                        for (int i = 0; i < documento.length(); i++) {
                            JSONObject catObj = (JSONObject) documento.get(i);
                            TipoDocumento cat = new TipoDocumento(catObj.getInt("id"),
                                    catObj.getString("nombre_departamento"));
                            tipoDocumentoList.add(cat);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateSpinner();
        }
    }
    }
