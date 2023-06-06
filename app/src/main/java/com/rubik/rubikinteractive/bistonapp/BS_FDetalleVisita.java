package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
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
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class BS_FDetalleVisita extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private int codigo;
    private int idreturn;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    LinearLayout ll;
    CardView btnVolver, btnEnviar;
    BS_Reagendar sampleFragment;
    TextView txtfecha, txthora, txttratamiento, txttiempo;
    int reagenda = 0;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            codigo =  getArguments().getInt("codigo",0);
            idreturn = getArguments().getInt("idreturn",0); // 1 = calendario; 2 = lista visita
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this frgment

        ll = (LinearLayout)inflater.inflate(R.layout.fragment_detallevisita, container, false);

        if(container == null){
            Toast.makeText(getContext(), "No existe contenedor", Toast.LENGTH_SHORT).show();
        }else{
            getActivity().setTitle("Detalle Visita");
            prgDialog = new ProgressDialog(getContext());
            prgDialog.setMessage("Cargando...");
            prgDialog.setCancelable(false);
            request = Volley.newRequestQueue(getContext());

            txtfecha = (TextView) ll.findViewById(R.id.txtAsunto);
            txthora = (TextView) ll.findViewById(R.id.txtFecha);
            txttratamiento = (TextView) ll.findViewById(R.id.txtHora);
            txttiempo = (TextView) ll.findViewById(R.id.txtTiempo);
           // txtestado = (TextView) ll.findViewById(R.id.txtEstado);
            btnVolver = (CardView)ll.findViewById(R.id.btn_volve);
            btnEnviar = (CardView)ll.findViewById(R.id.btn_enviar);
            CargarDetalleVisita();
            btnVolver.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    volverAtras();

                }
            });
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reagenda== 0){
                        FragmentManager manager = getFragmentManager();
                        sampleFragment = new BS_Reagendar();
                        Bundle bundle = new Bundle();
                        bundle.putInt("codigo",codigo);
                        bundle.putInt("idreturn",idreturn);
                        sampleFragment.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.contenedor,sampleFragment).commit();
                    }else{
                        Toast.makeText(getContext(), "La petición de reagendamiento ya fue enviada.", Toast.LENGTH_SHORT).show();
                    }

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

    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        if(idreturn != 0){
            if(idreturn == 1)
                manager.beginTransaction().replace(R.id.contenedor, new BS_FCalendario()).commit();
            else if(idreturn == 2)
                manager.beginTransaction().replace(R.id.contenedor, new BS_FProximaCita()).commit();
        }else{
            Toast.makeText(getContext(), "No se puede retornar", Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarDetalleVisita() {
        prgDialog.show();
        if(codigo > 0) {

            String url = "https://app.bistoncorp.com/php/connect_app/db_getvisita.php?idvisita="+codigo;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, null);
            jsonObjectRequest.setShouldCache(false);
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
                txttratamiento.setText(response.getString("fc_tratamiento"));
                txttiempo.setText(response.getString("fc_tiempo")+ " Minutos");

                reagenda = response.getInt("fc_reagenda");
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
