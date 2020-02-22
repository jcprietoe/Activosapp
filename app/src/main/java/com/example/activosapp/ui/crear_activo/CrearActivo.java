package com.example.activosapp.ui.crear_activo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.activosapp.R;
import com.example.activosapp.VolleyRP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.navigation.Navigation.findNavController;


public class CrearActivo extends Fragment {

    public CrearActivo() {

    }

    private VolleyRP volley;
    private RequestQueue mRequest;
    public static ArrayList<String> listTercero;
    public static ArrayList<String> listTipoAtivo;
    public static ArrayList<String> listDepartamentoEmpresa;
    public static ArrayList<String> listAreaEmpresa;
    public static HashMap<String,String> hashDepartamentoEmpresa;
    public static HashMap<String,String> hashTipoActivo;
    public static HashMap<String,String> hashAreaEmpresa;

    public static final String DATOS="datos";

    private static final String URL_TIPO_ACTIVO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTipoActivo.php";
    private static final String URL_DEPARTAMENTO_EMPRESA = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerDepartamentoEmpresa.php";
    private static final String TERCERO_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTercero.php";
    private static final String UPLOAD_IMAGE_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/upload_image.php";
    private static final String URL_AREA_EMPRESA ="https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerAreaEmpresa.php?are_demid=";

    Button btnBuscarImagen;
    ImageView ivMostrarImagen;


    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;


    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    String id_departamento ="";


    View vista;
    Spinner spSiNo, spTercero,spTipoActivo,spEstadoActivo, spDepartamentoEmpresa, spAreaDependencia;
    String[] tpodocumento;
    EditText fechamatricula, fechafabricacion, edtnoplaca, edtNombreImagen;
    EditText edtmodelo, edtreferencia, edtlinea, edtserial, edtserialmotor, edtserialpartes, edtdescripcion;

    Button btn_guardar_registro;

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
        fechamatricula = vista.findViewById(R.id.edtfmatricula);
        fechafabricacion = vista.findViewById(R.id.edtfabricacion);
        btnBuscarImagen = vista.findViewById(R.id.btnBuscarImagen);
        edtnoplaca = vista.findViewById(R.id.edtnoplaca);

        edtmodelo = vista.findViewById(R.id.edtreferencia);
        edtlinea = vista.findViewById(R.id.edtlinea);
        edtlinea = vista.findViewById(R.id.edtlinea);
        edtserial = vista.findViewById(R.id.edtserial);
        edtserialmotor = vista.findViewById(R.id.edtserialmotor);
        edtserialpartes = vista.findViewById(R.id.edtserialpartes);

        edtdescripcion = vista.findViewById(R.id.edtdescripcion);
        ivMostrarImagen = vista.findViewById(R.id.ivMostrarImagen);
        //edtNombreImagen = vista.findViewById(R.id.edtNombreImagen);
        btn_guardar_registro = vista.findViewById(R.id.btn_guardar_registro);

        //Carga el adapter de los spinner
        new GetTipoActivo().execute();
        new GetTercero().execute();
        new GetDepartamentoEmpresa().execute();


        //carga adapter de estado activo
        ArrayList<String>listEstadoActivo = new ArrayList<>();
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

        spAreaDependencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDepartamentoEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_departamento = hashDepartamentoEmpresa.get(parent.getSelectedItem().toString());
                new GetAreaEmpresa().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spEstadoActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTipoActivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTercero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString().equals("Otro")){
                    findNavController(view).navigate(R.id.action_nav_crear_tercero_to_nav_Tercero);
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

                String text = adapterView.getItemAtPosition(i).toString();
 //               Toast.makeText(adapterView.getContext(), "Ha Seleccionado:  " + text + "la posicion es:" + i, Toast.LENGTH_LONG).show();

                if (i == 2) {
                    new GetTercero().execute();
                    spTercero.setVisibility(View.VISIBLE);

                }else{
                    spTercero.setVisibility(View.INVISIBLE);
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

                uploadImage();
                findNavController(view).navigate(R.id.action_nav_crear_activo_to_nav_VistaActivos);
                Toast.makeText(getContext(),"Guardado Exitosamente",Toast.LENGTH_LONG).show();

  //              Toast.makeText(getContext(), "entro aqui", Toast.LENGTH_SHORT).show();

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

    public void poblarSpinnerTerceros(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            listTercero = new ArrayList<String>();
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                listTercero.add(dato.get("ter_nombre").toString());
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
            listTipoAtivo.add("Seleccione Tipo de Activo");
            for (int i = 0; i < datos.getJSONArray(DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(DATOS).get(i);
                hashTipoActivo.put(dato.getString("tip_tipo"),dato.getString("tip_id"));
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
                hashDepartamentoEmpresa.put(dato.getString("dem_descrip"),dato.getString("dem_id"));
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
                hashAreaEmpresa.put(dato.getString("are_descrip"),dato.getString("are_id"));
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
            if(null!=id_departamento) {
                JsonObjectRequest solicitud = new JsonObjectRequest(URL_AREA_EMPRESA+id_departamento, null, new Response.Listener<JSONObject>() {
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
