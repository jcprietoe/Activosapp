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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class CrearActivo extends Fragment {

    public CrearActivo() {

    }

    private VolleyRP volley;
    private RequestQueue mRequest;
    public static ArrayList<String> listTercero;
    private static final String TERCERO_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerTercero.php";


    Button btnBuscarImagen;
    ImageView ivMostrarImagen;


    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    private static final String UPLOAD_IMAGE_URL = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/upload_image.php";

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";


    View vista;
    Spinner spSiNo, spTercero;
    String[] tpodocumento;
    EditText fechamatricula, fechafabricacion, edtnoplaca, edtNombreImagen;
    EditText edtmodelo, edtreferencia, edtlinea, edtserial, edtserialmotor, edtserialpartes, edtdescripcion;

    Button btn_guardar_registro;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_crear_activo, container, false);

        //llama de variable

        spTercero = vista.findViewById(R.id.spTercero);
        spSiNo = vista.findViewById(R.id.spPregunta);
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
        edtNombreImagen = vista.findViewById(R.id.edtNombreImagen);
        btn_guardar_registro = vista.findViewById(R.id.btn_guardar_registro);

        //spinner terceros
        new GetTercero().execute();
        spTercero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //Spiner Pregunta
        final List<String> list = new ArrayList<String>();
        list.add("Seleccionar");
        list.add("No");
        list.add("Si");

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSiNo.setAdapter(arrayAdapter);
        spSiNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String text = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Ha Seleccionado:  " + text + "la posicion es:" + i, Toast.LENGTH_LONG).show();

                if (i == 2) {
                    spTercero.setVisibility(View.VISIBLE);


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

    public void poblarSpinnerTerceros(JSONObject datos) {
//        Log.println(Log.WARN, "JOANYDDDDDDDDDDDDD", datos.toString());
        try {
            listTercero = new ArrayList<String>();
            for (int i = 0; i < datos.getJSONArray("datos").length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray("datos").get(i);
                listTercero.add(dato.get("Tercero").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listTercero);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTercero.setAdapter(spinnerAdapter);
    }

    private class GetTercero extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            volley = VolleyRP.getInstance(getContext());
            mRequest = volley.getRequestQueue();

            JsonObjectRequest solicitud = new JsonObjectRequest(TERCERO_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                    poblarSpinnerTerceros(datos);
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
