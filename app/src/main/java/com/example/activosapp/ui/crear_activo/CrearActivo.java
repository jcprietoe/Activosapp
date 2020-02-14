package com.example.activosapp.ui.crear_activo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.activosapp.R;


public class CrearActivo extends Fragment {

    public CrearActivo(){

    }

// declaracion de variables
    String[] opcTipoDocumento = new String[]{"Selecione", "Cedula extranjera", "Cedula ciudadania"};
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
        aaTipoDocumento = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, opcTipoDocumento);
        aaTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdocu.setAdapter(aaTipoDocumento);
/* metodo de accion para el spinner crea conflicto
        spinnerdocu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
 */
        //boton guardar
        btn_guardar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
    }