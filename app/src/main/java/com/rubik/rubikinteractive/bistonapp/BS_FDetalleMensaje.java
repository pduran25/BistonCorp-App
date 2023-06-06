package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONException;
import org.json.JSONObject;


public class BS_FDetalleMensaje extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    private int codigo, tipousu;
    private int idreturn;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    LinearLayout ll;
    CardView btnVolver;
    TextView txtfecha, txthora, txtasunto, txtmensaje;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";




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
            CargarDetalleMensaje();
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
