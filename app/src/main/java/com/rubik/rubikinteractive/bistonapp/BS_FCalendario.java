package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Integer.parseInt;


public class BS_FCalendario extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    CardView btn_detalle;
    // CalendarView calendarView;
    CompactCalendarView calendarView;
    TextView txtMes;
    SimpleDateFormat dateFormatMonth;
    String [][] pros_cita;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    Utilidades manager;
    int tamanoarray = 0;
    private static int codselect = 0;
    private static String selfecha = "";
    BS_FProximaCita sampleFragment;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this frgment
        dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
        getActivity().setTitle("Calendario");
        View v =  inflater.inflate(R.layout.fragment_fcalendario, container, false);
        calendarView = (CompactCalendarView)v.findViewById(R.id.calendarView);
        txtMes = (TextView) v.findViewById(R.id.txtMes);
        txtMes.setText(dateFormatMonth.format(new Date()));
        btn_detalle = (CardView)v.findViewById(R.id.btn_volve);
        btn_detalle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(codselect != 0){
                    sampleFragment = new BS_FProximaCita();
                    Bundle bundle = new Bundle();
                    bundle.putInt("codigo",codselect);
                    bundle.putString("fecha",selfecha);
                    bundle.putInt("idreturn",1);
                    sampleFragment.setArguments(bundle);
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.contenedor,sampleFragment).commit();
                }else{
                    Toast.makeText(getContext(), "Fecha elegida no contiene Cita ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        prgDialog.show();
        manager = new Utilidades(getContext());
        CargarFechasCalendario();

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getContext();
                codselect = Buscarcodigo(String.valueOf(dateClicked.getTime()));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                selfecha = formatter.format(dateClicked.getTime());
                //Toast.makeText(getContext(), "Dia:"+, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtMes.setText(dateFormatMonth.format(firstDayOfNewMonth));
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

        return v;
    }


    private void volverAtras() {
        FragmentManager manager = getFragmentManager();
        BS_FPrincipal principal = new BS_FPrincipal();
        manager.beginTransaction().replace(R.id.contenedor, principal).commit();

    }

    private int Buscarcodigo(String mili){
        int cod = 0;
        for(int i=0; i<tamanoarray; i++){

            if(pros_cita[i][0].equals(mili)){
                cod = Integer.parseInt(pros_cita[i][1]);
                return cod;

            }
        }
        return cod;
    }

    private void CargarFechasCalendario() {

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

        String url = "https://app.bistoncorp.com/php/connect_app/db_getfechas.php?idorg="+idorg+"&idest="+idestab;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, this);
        jsonObjectRequest.setShouldCache(false);
        request.add(jsonObjectRequest);

    }

    @Override
    public void onResponse(JSONObject response) {

        prgDialog.hide();
        JSONArray json = response.optJSONArray("fechas");
        tamanoarray = json.length();
        if(tamanoarray>0){
            pros_cita = new String[json.length()][3];

            try {
                for(int i=0; i<json.length(); i++){
                    JSONObject jsonObject=null;
                    jsonObject = json.getJSONObject(i);

                    /***Agregando fecha***/
                    String myDate = jsonObject.optString("fc_fecha");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(myDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long millis = date.getTime();

                    Event evt = new Event(Color.BLUE, millis, jsonObject.optString("fc_tratamiento"));
                    calendarView.addEvent(evt);

                    pros_cita[i][0] = String.valueOf(millis);
                    pros_cita[i][1] = jsonObject.optString("fc_codigo");
                    pros_cita[i][2] = jsonObject.optString("fc_tratamiento");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(), "No contiene información para presentar", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        Toast.makeText(getContext(), "No contiene información para presentar", Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }


}
