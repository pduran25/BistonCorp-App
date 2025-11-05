package com.rubik.rubikinteractive.bistonapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BS_ARegistroSus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BS_ARegistroSus extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BS_FDetalleVisitaap detallevisita;
    private int codvxa, codvis, idreturn;

    LinearLayout ll;
    StringRequest strRequest;

    CardView btnGuardar, btnVolver;
    EditText edtObs;

    String observacion;

    ProgressDialog prgDialog;
    RequestQueue request;
    private Utilidades manager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Control de servicio Suspender");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_apregistrosus, container, false);

        btnGuardar = (CardView)ll.findViewById(R.id.btn_guardar);
        btnVolver = (CardView)ll.findViewById(R.id.btn_volver);
        edtObs = (EditText)ll.findViewById(R.id.edtObvs);



        manager = new Utilidades(getContext());

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
        }

        btnGuardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                observacion = edtObs.getText().toString();
                prgDialog = new ProgressDialog(getContext());
                prgDialog.setMessage("Cargando...");
                prgDialog.setCancelable(false);
                request = Volley.newRequestQueue(getContext());
                Enviar_Suspendida();
                strRequest.setShouldCache(false);
                request.add(strRequest);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                volverAtras();
            }
        });

        return ll;
    }


    private void Enviar_Suspendida() {
        prgDialog.show();
        String url = "https://app.bistoncorp.com/php/connect_app/db_registraSuspendida.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String resp)
            {
                prgDialog.hide();
                Toast.makeText(getContext(), resp, Toast.LENGTH_SHORT).show();
                FragmentManager managera = getFragmentManager();
                managera.beginTransaction().replace(R.id.contenedorap, new BS_AProximaVisita()).commit();
                EliminarInformacion();
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
                String st_codvxa = Integer.toString(codvxa);
                String st_codvis = Integer.toString(codvis);
                params.put("codvis", st_codvis);
                params.put("codvxa", st_codvxa);
                params.put("obs", observacion);
                return params;
            }
        };

    }

    private void EliminarInformacion() {
        manager.EliminarControl(codvxa);
    }


    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        detallevisita = new BS_FDetalleVisitaap();
        Bundle bundle = new Bundle();
        bundle.putInt("codvxa",codvxa);
        bundle.putInt("codvis",codvis);
        bundle.putInt("idreturn",idreturn);
        detallevisita.setArguments(bundle);
        manager.beginTransaction().replace(R.id.contenedorap,detallevisita).commit();
        Utilidades.serv_activo = null;

    }
}