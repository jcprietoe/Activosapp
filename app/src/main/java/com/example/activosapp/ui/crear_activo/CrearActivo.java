package com.example.activosapp.ui.crear_activo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.activosapp.Login;
import com.example.activosapp.R;
import com.example.activosapp.VolleyRP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.navigation.Navigation.findNavController;


public class CrearActivo extends Fragment {

    private VolleyRP volley;
    private RequestQueue mRequest;
    public static ArrayList<String> listTercero;
    public static ArrayList<String> listTipoAtivo;
    public static ArrayList<String> listDepartamentoEmpresa;
    public static ArrayList<String> listAreaEmpresa;
    public static HashMap<String, String> hashDepartamentoEmpresa;
    public static HashMap<String, String> hashTipoActivo;
    public static HashMap<String, String> hashAreaEmpresa;
    public static HashMap<String, String> hashTercero;
    public static final String DATOS = "datos";
    private static final String URL_TIPO_ACTIVO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoActivo.php";
    private static final String URL_DEPARTAMENTO_EMPRESA = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerDepartamentoEmpresa.php";
    private static final String TERCERO_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTercero.php";
    private static final String UPLOAD_IMAGE_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/upload_image.php";
    private static final String URL_AREA_EMPRESA = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerAreaEmpresa.php?are_demid=";
    private static final String URL_REG_ACTIVO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/registrar_activo.php";
    Button btnBuscarImagen;
    ImageView ivMostrarImagen;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    String id_departamento = "";
    String idAreaDependencia;
    String idEstadoActivo;
    String idTipoActivo;
    String tipoActivo;
    String idSiNo;
    String idTercero;
    String nomTercero;
    String departamento;
    String areaDependencia;

    String nombreActivo, noPlaca, modelo, referencia, anofabricacion, linea, fechaMatricula, serial1, serial2, serial3, variableControl, descripActivo, fechaRegistro;

    CustomDialogFragment customDialogFragment;

    View vista;
    Spinner spSiNo, spTercero, spTipoActivo, spEstadoActivo, spDepartamentoEmpresa, spAreaDependencia;
    String[] tpodocumento;
    EditText fechamatricula, fechafabricacion, edtnoplaca, edtNombreImagen, edtNombreActivo;
    EditText edtmodelo, edtreferencia, edtlinea, edtserial, edtserialmotor, edtserialpartes, edtdescripcion, edtVariableControl;
    CheckBox cbSi, cbNo;

    Button btn_guardar_registro;

    public CrearActivo() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_crear_activo, container, false);

        //Solicitudes de web service
        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();

        //asociacion variable a vista
        spTercero = vista.findViewById(R.id.spTercero);
        spSiNo = vista.findViewById(R.id.spPregunta);
        spTipoActivo = vista.findViewById(R.id.spTipoActivo);
        spEstadoActivo = vista.findViewById(R.id.spEstadoActivo);
        spDepartamentoEmpresa = vista.findViewById(R.id.spDepartamentoEmpresa);
        spAreaDependencia = vista.findViewById(R.id.spAreaDependencia);

        tpodocumento = getResources().getStringArray(R.array.tipo_documentos);
        cbNo = vista.findViewById(R.id.cbNo);
        cbSi = vista.findViewById(R.id.cbSi);
        fechamatricula = vista.findViewById(R.id.edtfmatricula);
        fechafabricacion = vista.findViewById(R.id.edtfabricacion);
        btnBuscarImagen = vista.findViewById(R.id.btnBuscarImagen);
        edtnoplaca = vista.findViewById(R.id.edtnoplaca);
        edtNombreActivo = vista.findViewById(R.id.edtNombreActivo);
        edtreferencia = vista.findViewById(R.id.edtreferencia);
        edtmodelo = vista.findViewById(R.id.edtmodelo);
        edtlinea = vista.findViewById(R.id.edtlinea);
        edtserial = vista.findViewById(R.id.edtserial);
        edtserialmotor = vista.findViewById(R.id.edtserialmotor);
        edtserialpartes = vista.findViewById(R.id.edtserialpartes);
        edtdescripcion = vista.findViewById(R.id.edtdescripcion);
        edtVariableControl = vista.findViewById(R.id.edtVariableControl);
        ivMostrarImagen = vista.findViewById(R.id.ivMostrarImagen);
        //edtNombreImagen = vista.findViewById(R.id.edtNombreImagen);
        btn_guardar_registro = vista.findViewById(R.id.btn_guardar_registro);

        //Carga el adapter de los spinner
        new GetTipoActivo().execute();
        new GetTercero().execute();
        new GetDepartamentoEmpresa().execute();

        //carga adapter de estado activo
        ArrayList<String> listEstadoActivo = new ArrayList<>();
        listEstadoActivo.add("Seleccione");
        listEstadoActivo.add("ACTIVO");
        listEstadoActivo.add("INACTIVO");
        listEstadoActivo.add("DAÑADO");
        ArrayAdapter arrayAdapter1;
        arrayAdapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listEstadoActivo);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstadoActivo.setAdapter(arrayAdapter1);

        //Spiner Pregunta
        final List<String> list = new ArrayList<String>();
        list.add("Seleccionar");
        list.add("No");
        list.add("Si");
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSiNo.setAdapter(arrayAdapter);

        cbNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbNo.isChecked()) {
                    cbSi.setChecked(false);
                }
            }
        });
        cbSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbSi.isChecked()) {
                    cbNo.setChecked(false);
                }
            }
        });

        spAreaDependencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaDependencia = parent.getSelectedItem().toString();
                idAreaDependencia = hashAreaEmpresa.get(areaDependencia);
                Toast.makeText(getContext(), areaDependencia, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDepartamentoEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departamento = parent.getSelectedItem().toString();
                id_departamento = hashDepartamentoEmpresa.get(departamento);
                new GetAreaEmpresa().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spEstadoActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (parent.getSelectedItem().toString()) {
                    case "Seleccione":
                        idEstadoActivo = "Seleccione";
                    case "ACTIVO":
                        idEstadoActivo = "A";
                        break;
                    case "INACTIVO":
                        idEstadoActivo = "I";
                        break;
                    case "DAÑADO":
                        idEstadoActivo = "D";
                        break;
                }
//                Toast.makeText(getContext(), idEstadoActivo, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTipoActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoActivo = parent.getSelectedItem().toString();
//                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                idTipoActivo = hashTipoActivo.get(tipoActivo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTercero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Otro")) {
                    findNavController(view).navigate(R.id.action_nav_crear_tercero_to_nav_Tercero);
                } else {
                    nomTercero = parent.getSelectedItem().toString();
                    idTercero = hashTercero.get(nomTercero);
                    Toast.makeText(getContext(), idTercero + "  : " + nomTercero, Toast.LENGTH_SHORT).show();
                }

                // Toast.makeText(getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spSiNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        idSiNo = "0";
                        break;
                    case 1:
                        spTercero.setVisibility(View.INVISIBLE);
                        idSiNo = "N";
                        idTercero = "NULL";
                        nomTercero = "Ninguno";
                        Toast.makeText(getContext(), idTercero + "  : " + nomTercero, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        new GetTercero().execute();
                        spTercero.setVisibility(View.VISIBLE);
                        idSiNo = "S";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnBuscarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        //boton guardar
        btn_guardar_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashDatos = new HashMap<>();

                noPlaca = edtnoplaca.getText().toString().trim();
                nombreActivo = edtNombreActivo.getText().toString().trim();
                modelo = edtmodelo.getText().toString().trim();
                referencia = edtreferencia.getText().toString().trim();
                anofabricacion = fechafabricacion.getText().toString().trim();
                linea = edtlinea.getText().toString().trim();
                fechaMatricula = fechamatricula.getText().toString().trim();
                serial1 = edtserial.getText().toString().trim();
                serial2 = edtserialmotor.getText().toString().trim();
                serial3 = edtserialpartes.getText().toString().trim();
                descripActivo = edtdescripcion.getText().toString().trim();
                variableControl = edtVariableControl.getText().toString().trim();

                SharedPreferences preferences = getContext().getSharedPreferences(Login.PREFERENCES_USUARIO, Context.MODE_PRIVATE);
                String idEmpresa = preferences.getString(Login.KEY_PREFERENCES_IDEMPRESA, "");
                hashDatos.put("act_empresaid", idEmpresa);
                String usuario = preferences.getString(Login.KEY_PREFERENCES_USUARIO, "");
                hashDatos.put("act_usureg", usuario);
                Calendar fecha = Calendar.getInstance();
                String fechaString = fecha.get(Calendar.YEAR) + "-" + fecha.get(Calendar.MONTH)
                        + "-" + fecha.get(Calendar.DATE) + " " + fecha.get(Calendar.HOUR)
                        +":"+fecha.get(Calendar.MINUTE)+":"+fecha.get(Calendar.SECOND);
                hashDatos.put("act_fecreg", fechaString);
                customDialogFragment = new CustomDialogFragment();
                Log.println(Log.WARN, "JOANY:REGISTRO", usuario);
                if (!tipoActivo.equals("Seleccione")) {
                    hashDatos.put("act_tipoid", idTipoActivo);
                } else {
                    customDialogFragment.setMessage("Ingrese el tipo de activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!nombreActivo.trim().equals("")) {
                    hashDatos.put("act_nombre", nombreActivo);
                } else {
                    customDialogFragment.setMessage("Ingrese el nombre del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!noPlaca.trim().equals("")) {
                    hashDatos.put("act_placa", noPlaca);
                } else {
                    customDialogFragment.setMessage("Ingrese el número de placa");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!modelo.trim().equals("")) {
                    hashDatos.put("act_modelo", modelo);
                } else {
                    customDialogFragment.setMessage("Ingrese el modelo del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!referencia.trim().equals("")) {
                    hashDatos.put("act_referencia", referencia);
                } else {
                    customDialogFragment.setMessage("Ingrese la referencia");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!anofabricacion.trim().equals("")) {
                    hashDatos.put("act_anofab", anofabricacion);
                } else {
                    customDialogFragment.setMessage("Ingrese el año de fabricación");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!linea.trim().equals("")) {
                    hashDatos.put("act_linea", linea);
                } else {
                    customDialogFragment.setMessage("Ingrese la línea del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!fechaMatricula.trim().equals("")) {
                    hashDatos.put("act_fecmat", fechaMatricula);
                } else {
                    customDialogFragment.setMessage("Ingrese fecha de matrícula");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!serial1.trim().equals("")) {
                    hashDatos.put("act_serialvin", serial1);
                } else {
                    customDialogFragment.setMessage("Ingrese serial del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!serial2.trim().equals("")) {
                    hashDatos.put("act_serial2", serial2);
                } else {
                    customDialogFragment.setMessage("Ingrese serial del motor");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!serial3.trim().equals("")) {
                    hashDatos.put("act_serial3", serial3);
                } else {
                    customDialogFragment.setMessage("Ingrese serial de subpartes");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!idSiNo.trim().equals("0")) {
                    hashDatos.put("act_tercero", idSiNo);
                    hashDatos.put("act_terceroid", idTercero);
                } else {
                    customDialogFragment.setMessage("Seleccione si es de un tercero el activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!descripActivo.trim().equals("")) {
                    hashDatos.put("act_descrip", descripActivo);
                } else {
                    customDialogFragment.setMessage("Ingrese las funciones del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!departamento.trim().equals("Seleccione")) {
//                    hashDatos.put("ter_nombre", id_departamento);
                    if (!areaDependencia.trim().equals("Seleccione")) {
                        hashDatos.put("act_aremprid", idAreaDependencia);
                    } else {
                        customDialogFragment.setMessage("Ingrese el área al que pertenece el activo");
                        customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                        return;
                    }
                } else {
                    customDialogFragment.setMessage("Seleccione el departamento al que pertenece el activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                if (!variableControl.trim().equals("")) {
                    hashDatos.put("act_varcontrol", variableControl);
                } else {
                    customDialogFragment.setMessage("Ingrese variable de control del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                //activo padre
                if (cbSi.isChecked()) {
                    hashDatos.put("act_padre", null);
                } else {
                    //si no es activo padre debe generarme un spinner con los activos que pueden ser padre
                    hashDatos.put("act_padre", "0");
                }
                if (!idEstadoActivo.trim().equals("Seleccione")) {
                    hashDatos.put("estado", idEstadoActivo);
                } else {
                    customDialogFragment.setMessage("Seleccione el estado del activo");
                    customDialogFragment.show(getActivity().getFragmentManager(), "customPicker");
                    return;
                }
                volley = VolleyRP.getInstance(getContext());
                mRequest = volley.getRequestQueue();
                JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_REG_ACTIVO, new JSONObject(hashDatos), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject datos) {
                        Toast.makeText(vista.getContext(), datos.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.WARN, "JOANY:ERROR", error.getMessage().toString());
                    }
                });

                VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);
                getFragmentManager().popBackStack();
//                uploadImage();
//                findNavController(view).navigate(R.id.action_nav_crear_activo_to_nav_VistaActivos);
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
                        final String selectedDate = year + "-" + (month + 1) + "-" + day;

                        fechamatricula.setText(selectedDate);
                    }
                });

                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
//        metodo fecha año de fabricacion
//        fechafabricacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    case R.id.edtfabricacion:
//                        showDatePickerDialog();
//                        break;
//                }
//            }
//
//            //calendario
//
//            private void showDatePickerDialog() {
//                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                        // +1 because January is zero
//                        final String selectedDate = day + " / " + (month + 1) + " / " + year;
//                        fechafabricacion.setText(selectedDate);
//                    }
//                });
//
//                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
//            }
//        });
        return vista;


    }


    public void poblarSpinnerTerceros(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            hashTercero = new HashMap<>();
            listTercero = new ArrayList<String>();
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                listTercero.add(dato.get("ter_nombre").toString());
                hashTercero.put(dato.get("ter_nombre").toString(), dato.get("ter_id").toString());
            }
            listTercero.add("Otro");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listTercero);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTercero.setAdapter(spinnerAdapter);
    }

    public void poblarSpinnerTipoActivo(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            hashTipoActivo = new HashMap<>();
            listTipoAtivo = new ArrayList<>();
            listTipoAtivo.add("Seleccione");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashTipoActivo.put(dato.getString("tip_tipo"), dato.getString("tip_id"));
                listTipoAtivo.add(dato.getString("tip_tipo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listTipoAtivo);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoActivo.setAdapter(spinnerAdapter);
    }

    public void poblarSpinnerDepartamentoEmpresa(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            hashDepartamentoEmpresa = new HashMap<>();
            listDepartamentoEmpresa = new ArrayList<>();
            listDepartamentoEmpresa.add("Seleccione");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashDepartamentoEmpresa.put(dato.getString("dem_descrip"), dato.getString("dem_id"));
                listDepartamentoEmpresa.add(dato.getString("dem_descrip"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listDepartamentoEmpresa);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartamentoEmpresa.setAdapter(spinnerAdapter);
    }

    public void poblarSpinnerAreaEmpresa(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            hashAreaEmpresa = new HashMap<>();
            listAreaEmpresa = new ArrayList<>();
            listAreaEmpresa.add("Seleccione");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashAreaEmpresa.put(dato.getString("are_descrip"), dato.getString("are_id"));
                listAreaEmpresa.add(dato.getString("are_descrip"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listAreaEmpresa);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAreaDependencia.setAdapter(spinnerAdapter);
    }

    private class GetTipoActivo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
//            listTercero.clear();
            JsonObjectRequest solicitud = new JsonObjectRequest(URL_TIPO_ACTIVO, null, new Response.Listener<JSONObject>() {
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
            //populateSpinner();
        }
    }

    private class GetDepartamentoEmpresa extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonObjectRequest solicitud = new JsonObjectRequest(URL_DEPARTAMENTO_EMPRESA, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    poblarSpinnerDepartamentoEmpresa(datos);
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

    private class GetAreaEmpresa extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            if (null != id_departamento) {
                JsonObjectRequest solicitud = new JsonObjectRequest(URL_AREA_EMPRESA + id_departamento, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject datos) {
                        poblarSpinnerAreaEmpresa(datos);
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

    private class GetTercero extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
//            listTercero.clear();
            JsonObjectRequest solicitud = new JsonObjectRequest(TERCERO_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    poblarSpinnerTerceros(datos);
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


    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = edtNombreImagen.getText().toString().trim();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                ivMostrarImagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
