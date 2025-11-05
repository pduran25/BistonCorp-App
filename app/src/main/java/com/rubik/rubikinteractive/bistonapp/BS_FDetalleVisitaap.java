package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_insumo;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_lampara;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_tratamiento;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.ar_area;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_consideracion;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_estacion;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_idimagen;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;


public class BS_FDetalleVisitaap extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    int codvxa, codvis;
    private int idreturn;
    RequestQueue request, request2;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog, prgDialog2;
    LinearLayout ll;
    CardView btnVolver, btnControl, btnCordon, btnSuspender, btnUbicacion;
    TextView txtfecha, txthora, txtdireccion, txttiempo, txtcomentario, txtorga, txtestab, txtauto;
    TextView txtcantidad1, txtcantidad2, txtcantidad3;
    TextView txtinsumo1, txtinsumo2, txtinsumo3;
    BS_ARegistro1 sampleFragment;

    BS_ARegistroSus registrosus;
    String [][] insumos;
    ArrayList<cs_insumo> List_insumo;
    LinearLayout ltrt ,lins, lcant;
    StringRequest strRequest;
    Utilidades manager;

    String longitud, latitud, label;
    int idcrono = 0;



    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0); // 1 = calendario; 2 = lista visita
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this frgment

        ll = (LinearLayout)inflater.inflate(R.layout.fragment_detallevisitaap, container, false);

        if(container == null){
            Toast.makeText(getContext(), "No existe contenedor", Toast.LENGTH_SHORT).show();
        }else{
            getActivity().setTitle("Detalle Visita");
            prgDialog = new ProgressDialog(getContext());
            prgDialog.setMessage("Cargando...");
            prgDialog.setCancelable(false);
            request = Volley.newRequestQueue(getContext());

            txtfecha = (TextView) ll.findViewById(R.id.txtFechaap);
            txthora = (TextView) ll.findViewById(R.id.txtHoraap);
            txttiempo = (TextView) ll.findViewById(R.id.txtTiempoap);
            txtcomentario = (TextView) ll.findViewById(R.id.txtComentarioap);
            txtorga = (TextView) ll.findViewById(R.id.txtOrgap);
            txtestab = (TextView) ll.findViewById(R.id.txtEstabap);
            txtdireccion = (TextView) ll.findViewById(R.id.txtDireccion);

            lins = (LinearLayout) ll.findViewById(R.id.lb_linsumo);
            lcant = (LinearLayout) ll.findViewById(R.id.lb_lcantidad);
            ltrt = (LinearLayout) ll.findViewById(R.id.lb_ltratamiento);

            btnVolver = (CardView)ll.findViewById(R.id.btn_volve);
            btnControl = (CardView)ll.findViewById(R.id.btn_controlap);
            btnCordon = (CardView)ll.findViewById(R.id.btn_vercordon);

            btnSuspender = (CardView)ll.findViewById(R.id.btn_suspender);
            btnUbicacion = (CardView)ll.findViewById(R.id.btn_ubicacion);

            manager = new Utilidades(getContext());
           CargarDetalleVisita();
            btnVolver.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    volverAtras();

                }
            });

            btnUbicacion.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String uri = "http://maps.google.com/maps?q=loc:" + latitud + "," + longitud + " (" + label + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                }
            });

            btnSuspender.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    registrosus = new BS_ARegistroSus();
                    Bundle bundle = new Bundle();
                    bundle.putInt("codvxa",codvxa);
                    bundle.putInt("codvis",codvis);
                    bundle.putInt("idreturn",idreturn);
                    registrosus.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.contenedorap, registrosus).commit();

                }
            });

            btnCordon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String url = "https://app.bistoncorp.com/imgCordon.php?idcrono="+idcrono;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    // Inicia la actividad que manejará la apertura del enlace
                    startActivity(intent);

                }
            });

            btnControl.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    CargarRegistroControl(codvxa);
                    FragmentManager manager = getFragmentManager();
                    sampleFragment = new BS_ARegistro1();
                    Bundle bundle = new Bundle();
                    bundle.putInt("codvxa",codvxa);
                    bundle.putInt("codvis",codvis);
                    bundle.putInt("idreturn",idreturn);
                    sampleFragment.setArguments(bundle);

                    prgDialog2 = new ProgressDialog(getContext());
                    prgDialog2.setMessage("Cargando...");
                    prgDialog2.setCancelable(false);
                    request2 = Volley.newRequestQueue(getContext());
                    Iniciar_Control();
                    strRequest.setShouldCache(false);
                    request2.add(strRequest);

                    manager.beginTransaction().replace(R.id.contenedorap,sampleFragment).commit();

                }
            });
        }

        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
        ll.setOnKeyListener(new View.OnKeyListener() {
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


        return ll;


    }

    private void CargarRegistroControl(int codvxa) {
        ControlServicio control = null;
        Cursor cursor = manager.CargarDatosControl(codvxa);
        if (cursor.getCount() > 0) {
            control = new ControlServicio();
            int pos_fecha = cursor.getColumnIndex(manager.TC_FECHA);
            int pos_idvis = cursor.getColumnIndex(manager.TC_IDVIS);
            int pos_hini = cursor.getColumnIndex(manager.TC_HINI);
            int pos_hfin = cursor.getColumnIndex(manager.TC_HFIN);
            int pos_obs = cursor.getColumnIndex(manager.TC_OBS);
            int pos_chkprev = cursor.getColumnIndex(manager.TC_CHKPREV);
            int pos_chkcorr = cursor.getColumnIndex(manager.TC_CHKCORR);
            int pos_firma = cursor.getColumnIndex(manager.TC_FIRMA);
            int pos_imgdisp = cursor.getColumnIndex(manager.TC_IMGDISP);
            int pos_chkfoto = cursor.getColumnIndex(manager.TC_CHKFOTO);
            int pos_chkhora = cursor.getColumnIndex(manager.TC_CHKHORA);
            int pos_accprev = cursor.getColumnIndex(manager.TC_ACCPREV);
            int pos_acccorr = cursor.getColumnIndex(manager.TC_ACCCORR);
            int pos_horri = cursor.getColumnIndex(manager.TC_HORRI);
            int pos_numest = cursor.getColumnIndex(manager.TC_NUMEST);
            int pos_numconsi = cursor.getColumnIndex(manager.TC_NUMCONSI);
            int pos_auto = cursor.getColumnIndex(manager.TC_AUTORIZADO);
            int pos_idauto = cursor.getColumnIndex(manager.TC_IDAUTO);
            int pos_reg = cursor.getColumnIndex(manager.TC_REGESTACION);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                control.setFecha(cursor.getString(pos_fecha));
                control.setIdvisita(cursor.getInt(pos_idvis));
                control.setHora_ini(cursor.getString(pos_hini));
                control.setHora_fin(cursor.getString(pos_hfin));
                control.setObs(cursor.getString(pos_obs));
                control.setChkprev(cursor.getInt(pos_chkprev));
                control.setChkcorr(cursor.getInt(pos_chkcorr));
                control.setImgdisp(cursor.getInt(pos_imgdisp));
                control.setChkfoto(cursor.getInt(pos_chkfoto));
                control.setChkhora(cursor.getInt(pos_chkhora));
                control.setAccprev(cursor.getString(pos_accprev));
                control.setAcccorr(cursor.getString(pos_acccorr));
                control.setHorri(cursor.getString(pos_horri));
                control.setNumest(cursor.getInt(pos_numest));
                control.setNumconsi(cursor.getInt(pos_numconsi));
                control.setAutorizado(cursor.getString(pos_auto));
                control.setIdauto(cursor.getString(pos_idauto));
                control.setRegestacion(cursor.getInt(pos_reg));
            }


        }


        if(control != null){

            //CONSIDERACION
            Cursor cursor2 = manager.CargarDatosConsideracion(codvxa);
            cs_consideracion cons = null;
            ArrayList<cs_consideracion> list_cons;
            if (cursor2.getCount() > 0) {
                list_cons = new ArrayList<>();
                cons = new cs_consideracion();
                int pos_idcons = cursor2.getColumnIndex(manager.CS_IDCONSI);
                int pos_cons = cursor2.getColumnIndex(manager.CS_CONSIDERACION);
                for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                    cons = new cs_consideracion();
                    cons.setIdconsi(cursor2.getInt(pos_idcons));
                    cons.setConsideracion(cursor2.getString(pos_cons));
                    list_cons.add(cons);
                }

                control.setList_consi(list_cons);
            }

            //TRATAMIENTO
            Cursor cursor3 = manager.CargarDatosTratamientos(codvxa);
            cs_tratamiento ctrat = null;
            ArrayList<cs_tratamiento> list_trat;
            if (cursor3.getCount() > 0) {
                list_trat = new ArrayList<>();
                ctrat = new cs_tratamiento();
                int pos_idtrat = cursor3.getColumnIndex(manager.TR_IDTRAT);
                int pos_trat = cursor3.getColumnIndex(manager.TR_TRATAMIENTO);
                for (cursor3.moveToFirst(); !cursor3.isAfterLast(); cursor3.moveToNext()) {
                    ctrat = new cs_tratamiento();
                    ctrat.setIdtrat(cursor3.getInt(pos_idtrat));
                    ctrat.setTratamiento(cursor3.getString(pos_trat));
                    list_trat.add(ctrat);
                }

                control.setList_trat(list_trat);
            }

            //AREA
            Cursor cursor4  = manager.CargarDatosAreas(codvxa);
            ar_area area = null;
            ArrayList<ar_area> list_area;
            if (cursor4.getCount() > 0) {
                list_area = new ArrayList<>();
                area = new ar_area();
                int pos_area = cursor4.getColumnIndex(manager.AR_AREA);
                int pos_nombarea = cursor4.getColumnIndex(manager.AR_NOMBAREA);
                int pos_plaga = cursor4.getColumnIndex(manager.AR_PLAGA);
                int pos_nombplaga = cursor4.getColumnIndex(manager.AR_NOMBPLAGA);
                int pos_pob = cursor4.getColumnIndex(manager.AR_POB);
                int pos_nombpob = cursor4.getColumnIndex(manager.AR_NOMBPOB);
                int pos_trat = cursor4.getColumnIndex(manager.AR_TRAT);
                int pos_nombtrat = cursor4.getColumnIndex(manager.AR_NOMBTRAT);

                for (cursor4.moveToFirst(); !cursor4.isAfterLast(); cursor4.moveToNext()) {
                    area = new ar_area();
                    area.setAr_area(cursor4.getInt(pos_area));
                    area.setAr_nombarea(cursor4.getString(pos_nombarea));
                    area.setAr_plaga(cursor4.getInt(pos_plaga));
                    area.setAr_nombplaga(cursor4.getString(pos_nombplaga));
                    area.setAr_pob(cursor4.getInt(pos_pob));
                    area.setAr_nombpob(cursor4.getString(pos_nombpob));
                    area.setAr_trat(cursor4.getInt(pos_trat));
                    area.setAr_tratamiento(cursor4.getString(pos_nombtrat));
                    list_area.add(area);
                }

                control.setList_area(list_area);
            }


            //ESTACIONES
            Cursor cursor5 = manager.CargarDatosEstacion(codvxa);
            cs_estacion est = null;
            ArrayList<cs_estacion> list_est;
            if (cursor5.getCount() > 0) {
                list_est = new ArrayList<>();
                est = new cs_estacion();
                int pos_num = cursor5.getColumnIndex(manager.ES_NUM);
                int pos_estado = cursor5.getColumnIndex(manager.ES_ESTADO);
                for (cursor5.moveToFirst(); !cursor5.isAfterLast(); cursor5.moveToNext()) {
                    est = new cs_estacion();
                    est.setCs_num(cursor5.getInt(pos_num));
                    est.setCs_estado(cursor5.getInt(pos_estado));
                    list_est.add(est);
                }

                control.setList_estacion(list_est);
            }

            //IMAGENES
            Cursor cursor6 = manager.CargarDatosImagen(codvxa);
            cs_idimagen idimagen = null;
            ArrayList<cs_idimagen> list_img;
            if (cursor6.getCount() > 0) {
                list_img = new ArrayList<>();
                idimagen = new cs_idimagen();
                int pos_img = cursor6.getColumnIndex(manager.IMG_IMG);
                int pos_desc = cursor6.getColumnIndex(manager.IMG_DESC);
                for (cursor6.moveToFirst(); !cursor6.isAfterLast(); cursor6.moveToNext()) {
                    idimagen = new cs_idimagen();
                    idimagen.setImagen(cursor6.getString(pos_img));
                    idimagen.setDescripcion(cursor6.getString(pos_desc));
                    list_img.add(idimagen);
                }

                control.setList_regimagen(list_img);
            }

            //LAMPARA
            Cursor cursor7 = manager.CargarDatosLampara(codvxa);
            cs_lampara lamp = null;
            ArrayList<cs_lampara> list_lamp;
            if (cursor7.getCount() > 0) {
                list_lamp = new ArrayList<>();
                lamp = new cs_lampara();
                int pos_num = cursor7.getColumnIndex(manager.LP_NUM);
                int pos_pob = cursor7.getColumnIndex(manager.LP_POB);
                for (cursor7.moveToFirst(); !cursor7.isAfterLast(); cursor7.moveToNext()) {
                    lamp = new cs_lampara();
                    lamp.setCl_num(cursor7.getInt(pos_num));
                    lamp.setCl_pob(cursor7.getInt(pos_pob));
                    list_lamp.add(lamp);
                }

                control.setList_lampara(list_lamp);
            }

        }

        Utilidades.serv_activo = control;

    }

    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        if(idreturn != 0){
            if(idreturn == 1)
                Toast.makeText(getContext(), "Volver a Calendario", Toast.LENGTH_SHORT).show();
                //manager.beginTransaction().replace(R.id.contenedor, new BS_FCalendario()).commit();
            else if(idreturn == 2)
                manager.beginTransaction().replace(R.id.contenedorap, new BS_AProximaVisita()).commit();
        }else{
            Toast.makeText(getContext(), "No se puede retornar", Toast.LENGTH_SHORT).show();
        }
    }

    private void Iniciar_Control() {
        prgDialog2.show();
        String url = "https://app.bistoncorp.com/php/connect_app/db_abrirControl.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String resp)
            {
                prgDialog2.hide();
               // Toast.makeText(getContext(), "Control Ejecu", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        prgDialog2.hide();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String st_codvxa = Integer.toString(codvxa);
                String st_codvis = Integer.toString(codvis);
                params.put("codvxa", st_codvxa);
                params.put("codvis", st_codvis);
                return params;
            }
        };

    }

    private void CargarDetalleVisita() {
        prgDialog.show();
        if(codvis > 0) {

            String url = "https://app.bistoncorp.com/php/connect_app/db_getvisitaaplicador.php?idvisita="+codvis;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, null);
            request.add(jsonObjectRequest);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            prgDialog.hide();
            if(response.getInt("fc_codigo") != 0){

                txtfecha.setText(response.getString("fc_fecha"));
                txthora.setText(response.getString("fc_hora"));
                txttiempo.setText(response.getString("fc_tiempo")+ " Minutos");
                txtcomentario.setText(response.getString("fc_comentario"));
                txtorga.setText(response.getString("fc_organizacion"));
                txtestab.setText(response.getString("fc_establecimiento"));
                txtdireccion.setText(response.getString("fc_direccion"));
                idcrono = response.getInt("fc_idcrono");
                latitud = response.getString("fc_latitud");
                longitud = response.getString("fc_longitud");
                label = response.getString("fc_label");

                JSONArray json = response.optJSONArray("tratins");
                insumos = new String[json.length()][3];
                List_insumo = new ArrayList<>();

                try {
                    for(int i=0; i<json.length(); i++){

                        JSONObject jsonObject=null;
                        jsonObject = json.getJSONObject(i);
                        insumos[i][0] = jsonObject.optString("in_insumo");
                        insumos[i][1] = jsonObject.optString("in_cantidad");
                        insumos[i][2] = jsonObject.optString("in_tratamiento");


                        TextView nombins = new TextView(getContext());
                        TextView cantins = new TextView(getContext());
                        TextView tratam = new TextView(getContext());
                        nombins.setText(insumos[i][0]);
                        cantins.setText(insumos[i][1]);
                        tratam.setText(insumos[i][2]);


                        ltrt.addView(tratam);
                        lins.addView(nombins);
                        lcant.addView(cantins);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getContext(), "Error: No se pudo conectar la visita", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        Toast.makeText(getContext(), "No se pudo conectar: "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }




}
