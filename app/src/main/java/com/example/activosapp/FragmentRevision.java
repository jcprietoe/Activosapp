package com.example.activosapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.activosapp.ui.crear_activo.CrearActivo;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRevision extends Fragment { public static final int REQUEST_UPDATE_DELETE_LAWYER = 2;
    private VolleyRP volley;
    private RequestQueue mRequest;
    private MatrixCursor cursor;

    private static  final String URL_REVISION = "https://www.gerenciandomantenimiento.com/activos/mantenimientoapp/obtenerItemsRevision.php";


    private ListView listRevision;
    private RevisionCursorAdapter revisionCursorAdapter;
    //private FloatingActionButton mAddButton;


    public FragmentRevision() {
        // Required empty public constructor
    }

    public static FragmentRevision newInstance() {
        return new FragmentRevision();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_revision, container, false);
        volley = VolleyRP.getInstance(getContext());
        mRequest = volley.getRequestQueue();

        // Referencias UI
        listRevision = root.findViewById(R.id.revision_list);
        revisionCursorAdapter = new RevisionCursorAdapter(getActivity(), null);
        //mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);


        // Setup
        listRevision.setAdapter(revisionCursorAdapter);

        // Eventos
        listRevision.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Cursor currentItem = (Cursor) revisionCursorAdapter.getItem(i);
//                String currentLawyerId = currentItem.getString(
//                        currentItem.getColumnIndex(LawyerEntry.ID));
//
//                showDetailScreen(currentLawyerId);
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


//        getActivity().deleteDatabase(LawyersDbHelper.DATABASE_NAME);

        // Instancia de helper
//        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        // Carga de datos
        loadRevision();

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        loadRevision();
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

    private void poblarCursorAdapter(JSONObject datos){
        try {
            cursor = new MatrixCursor(new String[]{"_id","des_revision"});
            for (int i = 0; i < datos.getJSONArray(CrearActivo.DATOS).length(); i++) {
                JSONObject dato = (JSONObject) datos.getJSONArray(CrearActivo.DATOS).get(i);
                cursor.addRow(new Object[]{0,dato.getString("descrip_rev")
                        });
            }
            Log.println(Log.WARN, "CURSOR:Cantidad", String.valueOf(cursor.getCount()));
            if (cursor != null && cursor.getCount() > 0) {
                revisionCursorAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class RevisionLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonObjectRequest solicitud = new JsonObjectRequest(URL_REVISION, null, new Response.Listener<JSONObject>() {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if (cursor != null && cursor.getCount() > 0) {
//                activosCursorAdapter.swapCursor(cursor);
//            } else {
//                // Mostrar empty state
//            }
        }
    }

}
