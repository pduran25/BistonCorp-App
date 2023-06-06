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


public class BS_AVisitada extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

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
        getActivity().setTitle("Visitas realizadas");
        View v = inflater.inflate(R.layout.fragment_fvisitada, container, false);
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
        FragmentManager fragmentManager = getFragmentManager();
        BS_APrincipal principal = new BS_APrincipal();
        fragmentManager.beginTransaction().replace(R.id.contenedorap,principal).commit();

    }

    private void Cargar_arreglovisita() {
        Cursor cursor = manager.CargarDatosUsuario();
        int idapli = 0;
        int pos_id = cursor.getColumnIndex(manager.US_COD);
        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                idapli = cursor.getInt(pos_id);
            }
        }
        request = Volley.newRequestQueue(getContext());

        String url =  "https://app.bistoncorp.com/php/connect_app/db_getvisitadas.php?idaplicador="+idapli;
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
        JSONArray json = response.optJSONArray("visitas");

        if(json.length()>0){
            visitadas = new String[json.length()][9];
            try {
                for(int i=0; i<json.length(); i++){
                    JSONObject jsonObject=null;
                    jsonObject = json.getJSONObject(i);
                    visitadas[i][0] = jsonObject.optString("fc_idcontrol");
                    visitadas[i][1] = jsonObject.optString("fc_codvxa");
                    visitadas[i][2] = jsonObject.optString("fc_codvis");
                    visitadas[i][3] = jsonObject.optString("fc_fecha") + " " + jsonObject.optString("fc_hora");
                    visitadas[i][4] = jsonObject.optString("fc_tratamiento");
                    visitadas[i][6] = jsonObject.optString("fc_organizacion");
                    visitadas[i][7] = jsonObject.optString("fc_establecimiento");
                    visitadas[i][8] = jsonObject.optString("fc_imgdsp");
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
        lista.setAdapter(new BS_ADVisitada(getFragmentManager(),getActivity(),visitadas,visitadaimg));

    }


}
