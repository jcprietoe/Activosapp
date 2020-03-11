package com.example.activosapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class DialogFragmentItems extends DialogFragment {

    private VolleyRP volley;
    private RequestQueue mRequest;
    EditText edtItemPerso;
    Button btnGuardarItem, btnCancelarItem;
    String tipoActivo, itemPerso;
    private static final String URL_REG_ITEM_PERSO = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/registrar_Item_Perso.php";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragmentdialog_item_perso, null);
        builder.setView(view);

        edtItemPerso = view.findViewById(R.id.edtItemPerso);
        btnGuardarItem = view.findViewById(R.id.btnGuardarItem);
        btnCancelarItem = view.findViewById(R.id.btnCancelarItem);

        //Recibo
        SharedPreferences preferencias = getActivity().getSharedPreferences("id_tipo", Context.MODE_PRIVATE);
        tipoActivo = preferencias.getString("tipo_Activo", "");

        SharedPreferences preferencesperso = getActivity().getSharedPreferences("id_activo", Context.MODE_PRIVATE);
        itemPerso = preferencesperso.getString("select_Activo", "");

        btnGuardarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                if(!edtItemPerso.getText().toString().trim().equals("")) {
                    volley = VolleyRP.getInstance(getContext());
                    mRequest = volley.getRequestQueue();
                    HashMap<String, String> hashDatos = new HashMap<>();
                    hashDatos.put("descrip_itemperso", edtItemPerso.getText().toString().trim());
                    hashDatos.put("itemperso_actid", itemPerso);
                    hashDatos.put("itemperso_tipid",tipoActivo );


                    JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_REG_ITEM_PERSO, new JSONObject(hashDatos), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject datos) {
                            Toast.makeText(view.getContext(), "Guardado Exitosamente!", Toast.LENGTH_SHORT).show();
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

                dismiss();
            }
        });


        btnCancelarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface frag) {
        super.onDismiss(frag);
        // DO Something
    }

}
