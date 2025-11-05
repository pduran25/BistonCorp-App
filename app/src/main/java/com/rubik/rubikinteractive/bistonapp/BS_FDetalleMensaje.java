package com.rubik.rubikinteractive.bistonapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_mensaje;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BS_FDetalleMensaje extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private int codigo, tipousu;
    private int idreturn;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    LinearLayout ll;
    CardView btnVolver, btnMensaje;
    TextView txtfecha, txthora, txtasunto, txtmensaje, lb_titular;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    AlertDialog.Builder alert;

    EditText edt_asunto, edt_mensaje;
    CardView btn_enviar, btn_cerrar;

    cl_mensaje msn;

    AlertDialog dialog;

    String asunto;

    Utilidades manager;

    StringRequest strRequest;

    int idusuario = 0;
    int idorg = 0;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            codigo =  getArguments().getInt("codigo",0);
            tipousu =  getArguments().getInt("tipousu",0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this frgment

        ll = (LinearLayout)inflater.inflate(R.layout.fragment_detallemensaje, container, false);

        if(container == null){
            Toast.makeText(getContext(), "No existe contenedor", Toast.LENGTH_SHORT).show();
        }else{
            getActivity().setTitle("Detalle Mensaje");
            prgDialog = new ProgressDialog(getContext());
            prgDialog.setMessage("Cargando...");
            prgDialog.setCancelable(false);
            request = Volley.newRequestQueue(getContext());

            txtasunto = (TextView) ll.findViewById(R.id.txtAsunto);
            txtfecha = (TextView) ll.findViewById(R.id.txtFecha);
            txthora = (TextView) ll.findViewById(R.id.txtHora);
            txtmensaje = (TextView) ll.findViewById(R.id.txtMensaje);

            btnVolver = (CardView)ll.findViewById(R.id.btn_volve);
            btnMensaje = (CardView)ll.findViewById(R.id.btn_responder);

            manager = new Utilidades(getContext());
            CargarDetalleMensaje();
            Cargar_arreglomensajes();

            btnMensaje.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Openventanamensaje();


                }
            });
            btnVolver.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                   volverAtras();
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

    private void Cargar_arreglomensajes() {
        Cursor cursor = manager.CargarDatosUsuario();
        idusuario = 0;
        idorg = 0;
        tipousu = 0;
        int pos_cod = cursor.getColumnIndex(manager.US_COD);
        int pos_org = cursor.getColumnIndex(manager.US_ORG);
        int pos_tus = cursor.getColumnIndex(manager.US_TUS);
        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                idusuario = cursor.getInt(pos_cod);
                idorg = cursor.getInt(pos_org);
                tipousu = cursor.getInt(pos_tus);
            }
        }
    }

    public void Openventanamensaje(){
        alert = new AlertDialog.Builder(getContext());
        final View escribemensaje = getLayoutInflater().inflate(R.layout.fragment_escribemensaje, null);
        lb_titular = (TextView)escribemensaje.findViewById(R.id.lb_redactar);
        edt_asunto = (EditText) escribemensaje.findViewById(R.id.edt_asunto);
        lb_titular.setText("Responder al Administrador");
        edt_asunto.setText("RE: "+ asunto);
        edt_mensaje = (EditText) escribemensaje.findViewById(R.id.edt_mensaje);
        edt_mensaje.requestFocus();
        btn_enviar = (CardView)escribemensaje.findViewById(R.id.btn_enviar);
        btn_cerrar = (CardView)escribemensaje.findViewById(R.id.btn_cerrar);
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_asunto.getText().toString().length()>0 && edt_mensaje.getText().toString().length()>0){
                    msn = new cl_mensaje();
                    msn.setAsunto(edt_asunto.getText().toString());
                    msn.setMensaje(edt_mensaje.getText().toString());
                    msn.setTipo(2); //mensaje para el administrador
                    msn.setUsuario(idusuario);
                    msn.setOrg(idorg);
                    if(tipousu == 1 || tipousu == 2)
                        msn.setTipousu(1); // 1 = cliente
                    else if(tipousu == 3)
                        msn.setTipousu(2); // 2 = aplicador
                    EnviarMensaje();
                }else{
                    Toast.makeText(getContext(), "Error: Ingrese todos los datos del formulario", Toast.LENGTH_SHORT).show();

                }
            }
        });




        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        alert.setView(escribemensaje);

        dialog =  alert.create();
        dialog.show();


    }

    public void EnviarMensaje(){
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Enviando mensaje ... ");
        prgDialog.setCancelable(false);
        request = Volley.newRequestQueue(getContext());
        GrabarMensaje();
        strRequest.setShouldCache(false);
        request.add(strRequest);
    }

    public void  GrabarMensaje(){
        String url = "https://app.bistoncorp.com/php/connect_app/db_sendmensaje.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                Log.d("presenta",response);
                prgDialog.hide();
                dialog.dismiss();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        prgDialog.hide();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String newDataArray = gson.toJson(msn);
                params.put("valmsj", newDataArray);
                return params;
            }
        };
    }

    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        BS_FMensajeria mensajeria = new BS_FMensajeria();

        Bundle bundle = new Bundle();
        bundle.putInt("volver",tipousu);
        mensajeria.setArguments(bundle);

        if(tipousu == 1)
            manager.beginTransaction().replace(R.id.contenedor, mensajeria).commit();
        else
            manager.beginTransaction().replace(R.id.contenedorap, mensajeria).commit();
    }


    private void CargarDetalleMensaje() {
        prgDialog.show();
        if(codigo > 0) {

            String url = "https://app.bistoncorp.com/php/connect_app/db_getdetmensaje.php?idmensaje="+codigo;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, null);
            request.add(jsonObjectRequest);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            prgDialog.hide();
            if(response.getInt("ms_codigo") != 0){
                txtfecha.setText(response.getString("ms_fecha"));
                txthora.setText(response.getString("ms_hora"));
                txtasunto.setText(response.getString("ms_asunto"));
                asunto = response.getString("ms_asunto");
                txtmensaje.setText(response.getString("ms_cuerpo"));
            }else{
                Toast.makeText(getContext(), "Error: No se pudo conectar la Mensajeria", Toast.LENGTH_SHORT).show();
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
