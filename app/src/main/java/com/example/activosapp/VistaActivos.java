package com.example.activosapp;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
//public class VistaActivos extends Fragment {
//
//
//    public VistaActivos() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_vista_activos, container, false);
//    }
//
//}
public class VistaActivos extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_LAWYER = 2;
    private VolleyRP volley;
    private RequestQueue mRequest;

    private static  final String URL_VER_ACTIVOS = "http://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerActivos.php?act_empresaid=";
    private static  final String URL_VER_AREA = "http://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerAreaEmpresa.php?are_id=";
    //private LawyersDbHelper mLawyersDbHelper;

    private ListView listActivo;
    private ActivosCursorAdapter activosCursorAdapter;
    //private FloatingActionButton mAddButton;


    public VistaActivos() {
        // Required empty public constructor
    }

    public static VistaActivos newInstance() {
        return new VistaActivos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vista_activos, container, false);
        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();

        // Referencias UI
        listActivo = (ListView) root.findViewById(R.id.activo_list);
        activosCursorAdapter = new ActivosCursorAdapter(getActivity(), null);
        //mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        listActivo.setAdapter(activosCursorAdapter);

        // Eventos
        listActivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Cursor currentItem = (Cursor) activosCursorAdapter.getItem(i);
//                String currentLawyerId = currentItem.getString(
//                        currentItem.getColumnIndex(LawyerEntry.ID));
//
//                showDetailScreen(currentLawyerId);
            }
        });
//        mAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAddScreen();
//            }
//        });


//        getActivity().deleteDatabase(LawyersDbHelper.DATABASE_NAME);

        // Instancia de helper
//        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        // Carga de datos
        loadActivos();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
//                case AddEditLawyerActivity.REQUEST_ADD_LAWYER:
//                    showSuccessfullSavedMessage();
//                    loadActivos();
//                    break;
                case REQUEST_UPDATE_DELETE_LAWYER:
                    loadActivos();
                    break;
            }
        }
    }

    private void loadActivos() {
        new ActivosLoadTask().execute();
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

    private class ActivosLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            JsonObjectRequest solicitud = new JsonObjectRequest("", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject datos) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.println(Log.WARN, "JOANYDERROR", error.toString());
                }
            });

            VolleyRP.addToQueue(solicitud, mRequest, getContext(), volley);

            Cursor cursor = new MatrixCursor(new String[]{});
//            return mLawyersDbHelper.getAllLawyers();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                activosCursorAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }

}
