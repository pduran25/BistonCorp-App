package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BS_ClienteVisitada extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    ListView lista;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    Utilidades manager;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    String [][] visitadas;

    int [] visitadaimg = {R.drawable.aspercion, R.drawable.termonebu, R.drawable.granulado, R.drawable.nebulizacion, R.drawable.aspercion, R.drawable.termonebu};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_clientevisitadas, container, false);
        getActivity().setTitle("Visitas recibidas");

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        prgDialog.show();
        manager = new Utilidades(getContext());

        Cargar_arreglovisita();


        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtras();
                    return true;
                }
                return false;
            }
        });


        return v;

    }

    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        BS_FPrincipal principal = new BS_FPrincipal();
        manager.beginTransaction().replace(R.id.contenedor, principal).commit();

    }

    private void Cargar_arreglovisita() {
        Cursor cursor = manager.CargarDatosUsuario();
        int idorg = 0;
        int idestab = 0;
        int pos_org = cursor.getColumnIndex(manager.US_ORG);
        int pos_est = cursor.getColumnIndex(manager.US_ACT);
        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                idorg = cursor.getInt(pos_org);
                idestab = cursor.getInt(pos_est);
            }
        }
        request = Volley.newRequestQueue(getContext());

        String url =  "https://app.bistoncorp.com/php/connect_app/db_getClientevisitadas.php?idorg="+idorg+"&idest="+idestab;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, this);
        jsonObjectRequest.setShouldCache(false);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        Toast.makeText(getContext(), "No contiene información para presentar", Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        prgDialog.hide();
        JSONArray json = response.optJSONArray("visitadas");
        if(json.length()>0) {
            visitadas = new String[json.length()][6];
            try {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    visitadas[i][0] = jsonObject.optString("cv_idcontrol");
                    visitadas[i][1] = jsonObject.optString("cv_fecha");
                    visitadas[i][2] = jsonObject.optString("cv_tratamiento");
                    visitadas[i][3] = jsonObject.optString("cv_establecimiento");
                    visitadas[i][4] = jsonObject.optString("cv_iscalificado");
                    visitadas[i][5] = jsonObject.optString("cv_puntuacion");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initControls();
        }else{
            Toast.makeText(getContext(), "No contiene información para presentar", Toast.LENGTH_SHORT).show();
        }

    }

    public void onStart(){
        super.onStart();
    }

    private void initControls(){

        lista = (ListView)getActivity().findViewById(R.id.lvisitada);
        lista.setAdapter(new BS_ADClienteVisitada(getFragmentManager(),getActivity(),visitadas,visitadaimg));

    }


}
