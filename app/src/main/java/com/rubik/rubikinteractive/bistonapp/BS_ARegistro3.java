package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_estacion;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BS_ARegistro3 extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener {


    CardView btnSgt;
    LinearLayout ll;
    CardView btnAgregar, btnEliminar, btnVolver;
    private int codvxa, codvis, idreturn, regestacion;
    private String[]header ={"# Estacion","Estado"};
    Spinner spEstados;




    String [][] estados;
    List<String> listaEstados;
    ArrayAdapter estadoAdapter;

    List<Spinner> listaSpinner;

    String[] strEstacion;
    String[] strEstados;

    //Para la conexion
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;

    int selestacion, selestado;
    String selnomestado;
    EditText edtObs;
    String obs;
    cs_estacion objEstacion;
    ArrayList<cs_estacion> Listado_sel;
    int num_index;
    String [] agrega;
    TablaDinamicaest tbd;
    TableLayout tl;
    BS_ARegistro4 registro4;
    BS_ARegistro2 registro2;
    BS_ARegistro1 registro1;
    BS_ARegistro3_5 registro35;
    BS_ARegistro3 registro3;
    ControlServicio control;
    int tot_num=0;
    int exist_lamp = 0;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    Utilidades manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Control de Estaciones");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_apregistro3, container, false);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
            regestacion = getArguments().getInt("regestacion",0);
            exist_lamp = getArguments().getInt("existLamp",0);
        }

        Inicializardatos();


        tl = (TableLayout)ll.findViewById(R.id.tl);
        tbd = new TablaDinamicaest(tl,getContext());
        tbd.addHeader(header);
        //tbd.addData(getData());
        tbd.backgroundHeader(Color.parseColor("#F26524"));
        tbd.backgroundData(Color.WHITE,Color.GRAY);

        btnSgt = (CardView)ll.findViewById(R.id.btn_siguiente);
        btnVolver = (CardView)ll.findViewById(R.id.btn_volver);

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        request = Volley.newRequestQueue(getContext());

        CargarDatosEstaciones();
        btnSgt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CargarValores();
                if(Listado_sel.size()>0){
                    AlmacenarEstaciones();
                    FragmentManager manager = getFragmentManager();

                    if(exist_lamp == 1){
                        registro35 = new BS_ARegistro3_5();
                        Bundle bundle = new Bundle();
                        bundle.putInt("codvxa",codvxa);
                        bundle.putInt("codvis",codvis);
                        bundle.putInt("idreturn",idreturn);
                        bundle.putInt("numest",tot_num);
                        bundle.putInt("regestacion",regestacion);
                        bundle.putInt("existLamp", exist_lamp);
                        registro35.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.contenedorap, registro35).commit();
                    }else{
                        registro2 = new BS_ARegistro2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("codvxa",codvxa);
                        bundle.putInt("codvis",codvis);
                        bundle.putInt("idreturn",idreturn);
                        registro2.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.contenedorap, registro2).commit();
                    }




                }else{
                    Toast.makeText(getContext(), "No ha ingresado Estaciones", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                volverAtras3();
            }
        });



        ll.setFocusableInTouchMode(true);
        ll.requestFocus();




        ll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtras3();

                    return true;
                }
                return false;
            }
        });


        return ll;

    }

    private void volverAtras3() {
        CargarValores();
        AlmacenarEstaciones();
        FragmentManager manager = getFragmentManager();
        registro1 = new BS_ARegistro1();
        Bundle bundle = new Bundle();
        bundle.putInt("codvxa", codvxa);
        bundle.putInt("codvis", codvis);
        bundle.putInt("idreturn", idreturn);
        registro1.setArguments(bundle);
        manager.beginTransaction().replace(R.id.contenedorap, registro1).commit();

    }

    private void AlmacenarEstaciones(){
        control = Utilidades.serv_activo;
        control.setList_estacion(Listado_sel);
        Utilidades.serv_activo = control;

        manager.EliminarEstacion(codvxa);
        manager.insertar_listestacion(Listado_sel, codvxa);
    }

    private void CargarValores(){
        listaSpinner = Utilidades.listaSpinner;
        int i=0;
        int valid = 0;
        Spinner newS;
        Listado_sel = new ArrayList<>();
        while(i<listaSpinner.size()){
            newS  = (Spinner)listaSpinner.get(i);
            valid = newS.getSelectedItemPosition();
            objEstacion = new cs_estacion(i+1,valid+1);
            Listado_sel.add(objEstacion);
            i++;
        }


    }

    private void Inicializardatos(){
        Listado_sel = new ArrayList<>();
        num_index = 0;
        agrega = new String[2];
        selnomestado = "";
        selestacion = 0;
        selestado = 0;
        spEstados = new Spinner(getContext());
        Utilidades.listaSpinner = new ArrayList<>();
        manager = new Utilidades(getContext());

    }



    private void CargarDatosEstaciones() {
        prgDialog.show();
        String url = "https://app.bistoncorp.com/php/connect_app/db_getInfoEstacion.php?idvisita=" + codvis;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, null);
        jsonObjectRequest.setShouldCache(false);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        prgDialog.hide();
        prgDialog.dismiss();
        try {

            JSONArray json = response.optJSONArray("estaciones");
            JSONObject jsonObject=null;
            jsonObject = json.getJSONObject(0);
            tot_num = Integer.parseInt(jsonObject.optString("num_estacion"));

            JSONArray jsona = response.optJSONArray("reglampara");
            JSONObject jsonObjecta=null;
            jsonObjecta = jsona.getJSONObject(0);
            exist_lamp = Integer.parseInt(jsonObjecta.optString("lp_reglamp"));


            JSONArray json2 = response.optJSONArray("estados");
            estados = new String[json2.length()][2];
            strEstados = new String[json2.length()];

            for(int j=0; j<json2.length(); j++){
                JSONObject jsonObject2=null;
                jsonObject2 = json2.getJSONObject(j);
                estados[j][0] = jsonObject2.optString("ee_codigo");
                estados[j][1] = jsonObject2.optString("ee_estado");
                strEstados[j] = estados[j][1];

            }

            listaEstados = new ArrayList<>();
            Collections.addAll(listaEstados, strEstados);
            //Implemento el adapter con el contexto, layout, listaFrutas
            estadoAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, listaEstados);
            //Cargo el spinner con los datos

            Utilidades.AdpEstados = estadoAdapter;

            num_index = 1;

            while(num_index<=tot_num){
                agrega[0] = num_index+ "";
                agrega[1] = num_index+ "";
                tbd.addItem(agrega);
                num_index++;
            }

            CargarDatosExistentes();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CargarDatosExistentes() {
        if(Utilidades.serv_activo.getList_estacion()!=null){
            Listado_sel = new ArrayList<>();
            Listado_sel = Utilidades.serv_activo.getList_estacion();
            listaSpinner = Utilidades.listaSpinner;
            int i=0;
            int valid = 0;
            Spinner newS;
            while(i<listaSpinner.size()) {
                newS = (Spinner) listaSpinner.get(i);
                objEstacion = Listado_sel.get(i);
                newS.setSelection(objEstacion.getCs_estado()-1);
                listaSpinner.set(i,newS);
                i++;
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        prgDialog.dismiss();
        Toast.makeText(getContext(), "No se pudo conectar: "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
