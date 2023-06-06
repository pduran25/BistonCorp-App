package com.rubik.rubikinteractive.bistonapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_mensaje;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BS_FMensajeria extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    ListView lista;
    RequestQueue request, request2;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog, prgDialog2;
    Utilidades manager;
    String [][] list_msn;
    int volver;
    AlertDialog.Builder alert;
    AlertDialog dialog;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    EditText edt_asunto, edt_mensaje;
    CardView btn_enviar, btn_cerrar;
    cl_mensaje msn;
    StringRequest strRequest;
    int idusuario = 0;
    int idorg = 0;
    int tipousu = 0;


    int [] imgmensaje = {R.drawable.aspercion, R.drawable.termonebu, R.drawable.granulado, R.drawable.nebulizacion, R.drawable.aspercion, R.drawable.termonebu};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            volver =  getArguments().getInt("volver",0);
        }
        Log.i("ONCREATE","ejecutando desde oncreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Mensaje entrada");
        View v = inflater.inflate(R.layout.fragment_fmensajeria, container, false);
        lista = (ListView)v.findViewById(R.id.lmensaje);

        manager = new Utilidades(getContext());
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        request = Volley.newRequestQueue(getContext());

        Cargar_arreglomensajes();

        FloatingActionButton fab = v.findViewById(R.id.fb_newmsj);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Openventanamensaje();
            }
        });

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

        Log.i("ONCREATEVIEW","ejecutando desde oncreateview");

        return v;

    }

    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        if(volver == 1){
            BS_FPrincipal principal = new BS_FPrincipal();
            manager.beginTransaction().replace(R.id.contenedor, principal).commit();
        }else{
            BS_APrincipal principal = new BS_APrincipal();
            manager.beginTransaction().replace(R.id.contenedorap,principal).commit();
        }
    }


    public void Openventanamensaje(){
        alert = new AlertDialog.Builder(getContext());
        final View escribemensaje = getLayoutInflater().inflate(R.layout.fragment_escribemensaje, null);
        edt_asunto = (EditText) escribemensaje.findViewById(R.id.edt_asunto);
        edt_mensaje = (EditText) escribemensaje.findViewById(R.id.edt_mensaje);
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
        prgDialog2 = new ProgressDialog(getContext());
        prgDialog2.setMessage("Enviando mensaje ... ");
        prgDialog2.setCancelable(false);
        request2 = Volley.newRequestQueue(getContext());
        GrabarMensaje();
        strRequest.setShouldCache(false);
        request2.add(strRequest);
    }

    public void  GrabarMensaje(){

        String url = "https://app.bistoncorp.com/php/connect_app/db_sendmensaje.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                Log.d("presenta",response);
                prgDialog2.hide();
                dialog.dismiss();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
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
                String newDataArray = gson.toJson(msn);
                params.put("valmsj", newDataArray);
                return params;
            }
        };
    }

    private void Cargar_arreglomensajes() {
        Cursor cursor = manager.CargarDatosUsuario();
        idusuario = 0;
        idorg = 0;
        tipousu =0;
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
        request = Volley.newRequestQueue(getContext());

        String url = "https://app.bistoncorp.com/php/connect_app/db_getmensajes.php?idusuario="+idusuario;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, this);
        jsonObjectRequest.setShouldCache(false);
        request.add(jsonObjectRequest);
    }


    private void Cargar_arreglomensajes(Context context) {
        Cursor cursor = manager.CargarDatosUsuario();
        int idusuario = 0;
        int pos_cod = cursor.getColumnIndex(manager.US_COD);
        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                idusuario = cursor.getInt(pos_cod);
            }
        }
        request = Volley.newRequestQueue(context);

        String url = "https://app.bistoncorp.com/php/connect_app/db_getmensajes.php?idusuario="+idusuario;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, this);
        jsonObjectRequest.setShouldCache(false);
        request.add(jsonObjectRequest);
    }

    public void onStart(){
        super.onStart();
        Log.i("START","ejecutando desde onstart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("RESUME","ejecutando desde onresume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("PAUSE","ejecutando desde onpause");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("SAVE","ejecutando desde onsave");
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
        JSONArray json = response.optJSONArray("mensajes");

        if(json.length()>0){
            list_msn = new String[json.length()][4];
            try {
                for(int i=0; i<json.length(); i++){
                    JSONObject jsonObject=null;
                    jsonObject = json.getJSONObject(i);
                    list_msn[i][0] = jsonObject.optString("ms_codigo");
                    list_msn[i][1] = "Asunto: " + jsonObject.optString("ms_asunto");
                    list_msn[i][2] = "Fecha: " + jsonObject.optString("ms_fecha") + " " + jsonObject.optString("ms_hora");
                    list_msn[i][3] = "Dirección: " + jsonObject.optString("ms_cuerpo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initControls();
        }else{
            Toast.makeText(getContext(), "No contiene información para presentar", Toast.LENGTH_SHORT).show();
        }
    }


    private void initControls(){


        lista.setAdapter(new BS_ADMensajeria(getFragmentManager(),getContext(),list_msn,imgmensaje,volver));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lista.setAdapter(null);
        Log.i("DESTROY","ejecutando desde ondestroy");
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
        Log.i("INSTANCE","ejecutando desde setinstance");
    }
}
