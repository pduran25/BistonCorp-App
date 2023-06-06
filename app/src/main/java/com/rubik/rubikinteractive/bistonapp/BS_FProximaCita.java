package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


public class BS_FProximaCita extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    ListView lista;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    Utilidades manager;
    SwipeRefreshLayout swipeRefreshLayout;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    String [][] prox_cita = {
            {"Asperción", "Guayaquil 12 de Febrero del 2019", "Establecimiento: Kennedy Norte"},
            {"Termonebulización", "Guayaquil 12 de Febrero del 2019", "Establecimiento: 9 de Octubre"},
            {"Granualado", "Guayaquil 12 de Febrero del 2019", "Establecimiento: Alborada"},
            {"Nebulización", "Guayaquil 24 de Febrero del 2019", "Establecimiento: Kennedy Norte"},
            {"Asperción", "Guayaquil 24 de Febrero del 2019", "Establecimiento: Alborada"},
            {"Termonebulización", "Guayaquil 12 de Marzo del 2019", "Establecimiento: Kennedy Norte"}
    };

    String [][] pros_cita;

    int codigo, returno;
    String fecha = "";


    int [] prox_citaimg = {R.drawable.aspercion, R.drawable.termonebu, R.drawable.granulado, R.drawable.nebulizacion, R.drawable.aspercion, R.drawable.termonebu};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Próxima Cita");
        View v = inflater.inflate(R.layout.fragment_fproximacita, container, false);
        lista = (ListView)v.findViewById(R.id.lproxcita);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.Swipe1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        Cargar_Listado();
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                swipeRefreshLayout.setRefreshing(false);
                                                            }
                                                        },4000);
                                                    }
                                                }

        );

        if(getArguments()!= null){
            codigo =  getArguments().getInt("codigo",0);
            fecha =  getArguments().getString("fecha","");
            returno = getArguments().getInt("returno",0);
        }

        Cargar_Listado();

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
        if(codigo == 0){
            FragmentManager manager = getFragmentManager();
            BS_FPrincipal principal = new BS_FPrincipal();
            manager.beginTransaction().replace(R.id.contenedor, principal).commit();
        }else{
            FragmentManager manager = getFragmentManager();
            BS_FCalendario principal = new BS_FCalendario();
            manager.beginTransaction().replace(R.id.contenedor, principal).commit();
        }


    }

    private void Cargar_Listado(){
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        prgDialog.show();
        manager = new Utilidades(getContext());
        Cargar_arreglovisita();
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

        String url;
        if(codigo == 0)
            url = "https://app.bistoncorp.com/php/connect_app/db_getfechas.php?idorg="+idorg+"&idest="+idestab;
        else
            url = "https://app.bistoncorp.com/php/connect_app/db_getfechacal.php?idorg="+idorg+"&idest="+idestab+"&fecha="+fecha;
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
        JSONArray json = response.optJSONArray("fechas");

        if(json.length()>0){
            pros_cita = new String[json.length()][4];
            try {
                for(int i=0; i<json.length(); i++){
                    JSONObject jsonObject=null;
                    jsonObject = json.getJSONObject(i);
                    pros_cita[i][0] = jsonObject.optString("fc_codigo");
                    pros_cita[i][1] = jsonObject.optString("fc_tratamiento");
                    pros_cita[i][2] = jsonObject.optString("fc_fecha") + " " + jsonObject.optString("fc_hora");
                    pros_cita[i][3] = jsonObject.optString("fc_direccion");
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


        lista.setAdapter(new BS_ADProxCita(getFragmentManager(),getActivity(),pros_cita,prox_citaimg));

    }




}
