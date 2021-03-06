package com.example.activosapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.activosapp.ui.crear_activo.CrearActivo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRevision extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_LAWYER = 2;
    private VolleyRP volley;
    private RequestQueue mRequest;


    private static final String URL_VER_REVISION_ID = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerItems.php?item_tipid=";
    private static final String URL_VER_REVISION_ID2 = "&itemperso_actid=";


    private ArrayList<String> desItemPerso;
    private ListView listRevision;
    List<ItemRevision> itemRevisions;
    private Button boton, btnAddItem;
    private String tipoActivo;
    private String itemPerso;
    DialogFragmentItems prueba;
    //private FloatingActionButton mAddButton;


    public FragmentRevision() {
        // Required empty public constructor
    }

    public static FragmentRevision newInstance() {
        return new FragmentRevision();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_revision, container, false);
        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();
        //Recibo
        SharedPreferences preferencias = getActivity().getSharedPreferences("id_tipo", Context.MODE_PRIVATE);
        tipoActivo = preferencias.getString("tipo_Activo", "");

        SharedPreferences preferencesperso = getActivity().getSharedPreferences("id_activo", Context.MODE_PRIVATE);
        itemPerso = preferencesperso.getString("select_Activo", "");

        // Referencias UI
        listRevision = root.findViewById(R.id.revision_list);
        boton = root.findViewById(R.id.button2);
        btnAddItem = root.findViewById(R.id.btnAgregarItem);
        //mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        listRevision.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent evento) {
                if (prueba != null && prueba.getIsLoad()) {
                    itemRevisions.clear();
                    loadRevision();
                    prueba.setIsLoad(false);
                }
                return false;
            }
        });
        // Setup
        itemRevisions = new ArrayList<ItemRevision>(50);

        listRevision.setAdapter(new CustomArrayAdapter(getContext(), itemRevisions));
        // Eventos
        listRevision.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Cursor currentItem = (Cursor) revisionCursorAdapter.getItem(i);
//                String currentItemId = currentItem.getString(
//                        currentItem.getColumnIndex("des_revision"));
//                Toast.makeText(getContext(), currentItemId, Toast.LENGTH_SHORT).show();

//                showDetailScreen(currentItemId);
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "funciona", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prueba = new DialogFragmentItems();
                prueba.show(getActivity().getFragmentManager(), "customPicker");
            }
        });
//        (new Handler()).postDelayed(new Runnable() {
//
//            public void run() {
//                loadRevision();
//            }
//        }, 42000);
//        mAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAddScreen();
//            }
//        });
        // Carga de datos
        loadRevision();
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadRevision();
    }

    public void loadRevision() {
        new FragmentRevision.RevisionLoadTask().execute();
    }

//    private void showSuccessfullSavedMessage() {
//        Toast.makeText(getActivity(),
//                "Abogado guardado correctamente", Toast.LENGTH_SHORT).show();
//    }

//    private void showAddScreen() {
//        Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
//        startActivityForResult(intent, AddEditLawyerActivity.REQUEST_ADD_LAWYER);
//    }
//
//    private void showDetailScreen(String lawyerId) {
//        Intent intent = new Intent(getActivity(), LawyerDetailActivity.class);
//        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, lawyerId);
//        startActivityForResult(intent, REQUEST_UPDATE_DELETE_LAWYER);
//    }

    private void poblarCursorAdapter(JSONObject datos) {
        try {
            listRevision.setAdapter(null);
            for (int i = 0; i < datos.getJSONArray(CrearActivo.DATOS).length(); i++) {
                ItemRevision itemRevision = new ItemRevision();
                JSONObject dato = (JSONObject) datos.getJSONArray(CrearActivo.DATOS).get(i);
                itemRevision.setTitle(dato.getString("campo"));
                itemRevisions.add(itemRevision);
            }
            if (itemRevisions != null) {
                CustomArrayAdapter adapter = new CustomArrayAdapter(getContext(), itemRevisions);
                listRevision.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class RevisionLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            if (tipoActivo != null && tipoActivo.trim() != "" && itemPerso != null && itemPerso.trim() != "") {
                JsonObjectRequest solicitud = new JsonObjectRequest(URL_VER_REVISION_ID + tipoActivo + URL_VER_REVISION_ID2 + itemPerso, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject datos) {
                        poblarCursorAdapter(datos);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.WARN, "JOANYDERROR", error.toString());
                    }
                });

                VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if (itemRevisions.size()>0) {
//                listRevision.setAdapter(new CustomArrayAdapter(getContext(), itemRevisions));
//            } else {
//                // Mostrar empty state
//            }
        }
    }

}
