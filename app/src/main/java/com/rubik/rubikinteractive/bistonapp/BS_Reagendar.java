package com.rubik.rubikinteractive.bistonapp;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
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
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_reagenda;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class BS_Reagendar extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private int codigo;
    private int idreturn;
    RequestQueue request, request2;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog, prgDialog2;
    StringRequest strRequest;
    LinearLayout ll;
    CardView btnVolver, btnEnviar;
    TextView txtfecha, txthora, txttratamiento, txttiempo, txtfechanew, txthoranew, txtcomentario;
    Button btnfecha,btnhora;
    DialogFragment datePicker;
    Calendar c;
    DatePickerDialog dpd;
    cl_reagenda rag;
    BS_FDetalleVisita sampleFragment;

        String val_fecha = "";
        String val_hora = "";
    int idusuario = 0;
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

        ll = (LinearLayout)inflater.inflate(R.layout.fragment_reagenda, container, false);

        if(container == null){
            Toast.makeText(getContext(), "No existe contenedor", Toast.LENGTH_SHORT).show();
        }else{
            getActivity().setTitle("Detalle Visita");
            prgDialog = new ProgressDialog(getContext());
            prgDialog.setMessage("Cargando...");
            prgDialog.setCancelable(false);
            request = Volley.newRequestQueue(getContext());

            txtfecha = (TextView) ll.findViewById(R.id.txtFechaact);
            txthora = (TextView) ll.findViewById(R.id.txtHoraact);
            txttratamiento = (TextView) ll.findViewById(R.id.txtTratamiento);
            txttiempo = (TextView) ll.findViewById(R.id.txtTiempo);
            txtfechanew =(TextView) ll.findViewById(R.id.txtFechanew);
            txthoranew =(TextView) ll.findViewById(R.id.txtHoranew);
            txtcomentario = (EditText) ll.findViewById(R.id.txtObservacion);

            btnVolver = (CardView)ll.findViewById(R.id.btn_volve);
            btnEnviar = (CardView)ll.findViewById(R.id.btn_enviar);


            btnfecha = (Button)ll.findViewById(R.id.btnFechanew);
            btnhora = (Button)ll.findViewById(R.id.btnHoranew);


            btnfecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);

                    dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String txtday, txtmonth;
                                month = month + 1;
                                if(dayOfMonth<10)
                                    txtday = "0"+dayOfMonth;
                                else
                                    txtday = ""+ dayOfMonth;

                                if(month <10 )
                                    txtmonth = "0" + month;
                                else
                                    txtmonth = ""+month;

                                val_fecha = year + "-" +txtmonth + "-" + txtday;
                                txtfechanew.setText(val_fecha);
                        }
                    }, year, month,day);
                    dpd.show();
                }
            });


            btnhora.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    c = Calendar.getInstance();
                    int hora = c.get(Calendar.HOUR_OF_DAY);
                    int min = c.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String txthor, txtmin;
                            if(hourOfDay<10)
                                txthor = "0"+hourOfDay;
                            else
                                txthor = "" + hourOfDay;

                            if(minute <10)
                                txtmin = "0"+ minute;
                            else
                                txtmin = ""+minute;
                            val_hora = txthor + ":" + txtmin;
                            txthoranew.setText(val_hora);
                        }
                    },hora, min,true);
                    timePickerDialog.show();

                }

            });


            CargarDetalleVisita();
            btnVolver.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    volver_atras();

                }
            });

            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comenta = "";
                    comenta = txtcomentario.getText().toString();
                    if(!val_fecha.equals("") && !val_hora.equals("") && !comenta.equals("")) {
                        prgDialog2 = new ProgressDialog(getContext());
                        prgDialog2.setMessage("Enviando petición al Servidor");
                        prgDialog2.setCancelable(false);
                        request2 = Volley.newRequestQueue(getContext());
                        EnviarPeticionReagenda();
                        strRequest.setShouldCache(false);
                        request2.add(strRequest);
                    }else{
                        Toast.makeText(getContext(), "Error: Por favor elija las Fechas y coloque un comentario", Toast.LENGTH_SHORT).show();
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
                    volver_atras();
                    return true;
                }
                return false;
            }
        });


        return ll;


    }

    private void volver_atras(){
        FragmentManager manager = getFragmentManager();
        sampleFragment = new BS_FDetalleVisita();
        Bundle bundle = new Bundle();
        bundle.putInt("codigo",codigo);
        bundle.putInt("idreturn",idreturn);
        sampleFragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.contenedor,sampleFragment).commit();
    }


    private void EnviarPeticionReagenda(){
        prgDialog2.show();
        rag = new cl_reagenda();

        String comenta = "";
        comenta = txtcomentario.getText().toString();
        if(!val_fecha.equals("") || !val_hora.equals("") || !comenta.equals("")){
            Utilidades manager = new Utilidades(getContext());
            Cursor cursor = manager.CargarDatosUsuario();
            idusuario = 0;

            if (cursor.getCount() > 0) {
                int pos_id = cursor.getColumnIndex(manager.US_COD);


                if (cursor.getCount() > 0) {
                    //todos los datos del Usuario son conocidos desde este momento.
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        idusuario = cursor.getInt(pos_id);
                    }
                }
            }

            rag.setIdusuario(idusuario);
            rag.setIdvisita(codigo);
            rag.setFecha(val_fecha);
            rag.setHora(val_hora);
            rag.setComentario(comenta);

            String url = "https://app.bistoncorp.com/php/connect_app/db_reagenda.php";
            strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
                @Override
                public void onResponse(String response)
                {
                    prgDialog2.hide();
                    if(response.equals("reagendado")){
                        Toast.makeText(getContext(), "Peticion enviada exitosamente", Toast.LENGTH_SHORT).show();
                        volver_atras();
                    }
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
                    String newDataArray = gson.toJson(rag);
                    params.put("reagenda", newDataArray);
                    return params;
                }
            };

        }else{
            prgDialog2.hide();
            Toast.makeText(getContext(), "Error: Por favor elija las Fechas y coloque un comentario", Toast.LENGTH_SHORT).show();
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
                txttiempo.setText(response.getString("fc_tiempo")+ " Horas");

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
