package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_tratamiento;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_consideracion;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BS_ARegistro1 extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    CardView btnSgt, btnVolver;
    LinearLayout ll;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    private int codvxa, codvis, idreturn;
    String [][] tratamientos;
    String [][] insumos;
    String [][] consideraciones;
    BS_FDetalleVisitaap sampleFragment;
    BS_ARegistro2 registro2;

    LinearLayout ltrat;
    LinearLayout lins;
    LinearLayout lcon;
    TextView fecha;
    TextView hora;

    ArrayList<cs_tratamiento> List_trat;
    ArrayList<cs_consideracion> List_consi;

    ArrayList<cs_tratamiento> sel_trat;
    ArrayList<cs_consideracion> sel_consi;

    ArrayList<CheckBox> chk_trat;
    ArrayList<CheckBox> chk_consi;

    ControlServicio control;
    String fecha_interna;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    Utilidades manager;
    int bandera = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Control de servicio 1 de 4");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_apregistro1, container, false);


        btnSgt = (CardView)ll.findViewById(R.id.btn_siguiente);
        btnVolver = (CardView)ll.findViewById(R.id.btn_volve);
        ltrat = (LinearLayout)ll.findViewById(R.id.ln_tratamientos);
        lins = (LinearLayout)ll.findViewById(R.id.ln_insumos);
        lcon = (LinearLayout)ll.findViewById(R.id.ln_consi);
        fecha = (TextView)ll.findViewById(R.id.txt_fecha);
        hora = (TextView)ll.findViewById(R.id.txt_hora);

        sel_trat = new ArrayList<>();
        sel_consi = new ArrayList<>();

        manager = new Utilidades(getContext());
        if(Utilidades.serv_activo == null){
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            int dia = today.monthDay;
            int mes = today.month;
            int anio = today.year;
            mes = mes + 1;

            int hor = today.hour;
            int min = today.minute;

            String txt_dia;
            String txt_mes;
            if(dia<10){
                txt_dia = "0" + dia;
            }else{
                txt_dia = ""+dia;
            }

            if(mes<10){
                txt_mes = "0" + mes;
            }else{
                txt_mes = ""+mes;
            }


            String txt_hor;
            String txt_min;
            fecha.setText(txt_dia+"/"+txt_mes+"/"+anio);
            fecha_interna = anio+"-"+txt_mes+"-"+txt_dia;
            if(hor<10){
                txt_hor = "0"+hor;
            }else{
                txt_hor = ""+hor;
            }

            if(min<10) {
                txt_min = "0" + min;
            }else{
                txt_min = "" + min;
            }
            hora.setText(txt_hor+":"+txt_min);


        }else{

            String fecha_reg = Utilidades.serv_activo.getFecha().toString();
            String hora_reg = Utilidades.serv_activo.getHora_ini().toString();

            String [] fechaarr = fecha_reg.split("-");
            String anio_reg = fechaarr[0];
            String mes_reg = fechaarr[1];
            String dia_reg = fechaarr[2];

            fecha.setText(dia_reg+"/"+mes_reg+"/"+anio_reg);
            fecha_interna = fecha_reg;
            hora.setText(hora_reg);

        }


        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
        }

        if(Utilidades.serv_activo != null){
            int codva = Utilidades.serv_activo.getIdvisxapli();
            int codvs = Utilidades.serv_activo.getIdvisita();
            if(codva != codvxa && codvs != codvis){
                Utilidades.serv_activo = new ControlServicio();
                Utilidades.serv_activo = null;
            }
        }

        btnSgt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                revisar_seleccionados();

                if(sel_trat.size()>0){
                    Almacenar_cabecera();

                    registro2 = new BS_ARegistro2();
                    FragmentManager manager = getFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putInt("codvxa",codvxa);
                    bundle.putInt("codvis",codvis);
                    bundle.putInt("idreturn",idreturn);
                    registro2.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.contenedorap, registro2).commit();
                    manager.getFragments();


                }else{
                    Toast.makeText(getContext(), "No ha completado todo este formulario", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               volverAtras1();
            }
        });




        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        request = Volley.newRequestQueue(getContext());



        CargarDatosControl();

        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
        ll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtras1();
                    return true;
                }
                return false;
            }
        });



        return ll;

    }

    private void volverAtras1() {

        FragmentManager manager = getFragmentManager();
        sampleFragment = new BS_FDetalleVisitaap();
        Bundle bundle = new Bundle();
        bundle.putInt("codvxa",codvxa);
        bundle.putInt("codvis",codvis);
        bundle.putInt("idreturn",idreturn);
        sampleFragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.contenedorap,sampleFragment).commit();
        Utilidades.serv_activo = null;

    }


    private void Almacenar_cabecera(){

        if(Utilidades.serv_activo == null){
            control = new ControlServicio();
            control.setFecha(fecha_interna);
            control.setHora_ini(hora.getText().toString());
            bandera = 1; //INSERTAR
        }else{
            control = Utilidades.serv_activo;
            bandera = 2; //ACTUALIZAR
        }


        control.setIdvisxapli(codvxa);
        control.setList_trat(sel_trat);
        control.setNumconsi(sel_consi.size());
        control.setList_consi(sel_consi);
        control.setIdvisita(codvis);

        Utilidades.serv_activo = control;


        if(bandera == 1){
            manager.insertar_control(control);
            manager.insertar_listconsi(sel_consi, codvxa);
            manager.insertar_listtrata(sel_trat, codvxa);
        }else if(bandera == 2){
            manager.Actualiza_control2(control);
            manager.EliminarConsideracion(codvxa);
            manager.insertar_listconsi(sel_consi, codvxa);
            manager.EliminarTratamientos(codvxa);
            manager.insertar_listtrata(sel_trat, codvxa);
        }

    }

    private void revisar_seleccionados(){
        int i = 0;
        CheckBox chsel;
        int id = 0;


        i =0;
        id = 0;
        chsel = null;
        sel_trat = new ArrayList<>();

        while(i< chk_trat.size()){
            chsel = chk_trat.get(i);
            if(chsel.isChecked()){
                id = chsel.getId();
                sel_trat.add(List_trat.get(i));
            }
            i++;
        }

        i =0;
        id = 0;
        chsel = null;

        sel_consi = new ArrayList<>();

        while(i< chk_consi.size()){
            chsel = chk_consi.get(i);
            if(chsel.isChecked()){
                id = chsel.getId();
                sel_consi.add(List_consi.get(i));
            }
            i++;
        }
    }

    private void Actualizar_checks(){
        if(Utilidades.serv_activo != null) {

            control = Utilidades.serv_activo;
            sel_trat = control.getList_trat();
            int i =0;
            int id = 0;
            CheckBox chsel = null;

            if(sel_trat!= null){
                while(i<sel_trat.size()){
                    for(int j=0;j<chk_trat.size();j++){
                        chsel = chk_trat.get(j);
                        if(chsel.getId() == sel_trat.get(i).getIdtrat()){
                            chk_trat.get(j).setChecked(true);
                        }
                    }
                    i++;
                }
            }


            sel_consi = control.getList_consi();
            i =0;
            id = 0;
            chsel = null;

            if(sel_consi != null){
                while(i<sel_consi.size()){
                    for(int j=0;j<chk_consi.size();j++){
                        chsel = chk_consi.get(j);
                        if(chsel.getId() == sel_consi.get(i).getIdconsi()){
                            chk_consi.get(j).setChecked(true);
                        }
                    }
                    i++;
                }
            }


        }

    }

    private Date stringToDate(String aDate, String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    private void CargarDatosControl() {
        prgDialog.show();
        if (codvis > 0) {

            String url = "https://app.bistoncorp.com/php/connect_app/db_gettratins.php?idvisita=" + codvis;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, null);
            jsonObjectRequest.setShouldCache(false);
            request.add(jsonObjectRequest);
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
    public void onResponse(JSONObject response) {
        prgDialog.hide();
        prgDialog.dismiss();
        JSONArray json = response.optJSONArray("tratamientos");
        tratamientos = new String[json.length()][3];
        List_trat = new ArrayList<>();
        chk_trat = new ArrayList<>();

        try {

            for(int i=0; i<json.length(); i++) {

                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                tratamientos[i][0] = jsonObject.optString("tr_codtv");
                String valtv = jsonObject.optString("tr_codtv");
                tratamientos[i][1] = jsonObject.optString("tr_codigo");
                tratamientos[i][2] = jsonObject.optString("tr_tratamiento");

                if(i>0){
                    TextView txtespacio1 = new TextView(getContext());
                    txtespacio1.setTypeface(null, Typeface.BOLD);
                    txtespacio1.setText("----------------");
                    txtespacio1.setHeight(70);
                    txtespacio1.setGravity(Gravity.CENTER_VERTICAL);

                    TextView txtespacio2 = new TextView(getContext());
                    txtespacio2.setTypeface(null, Typeface.BOLD);
                    txtespacio2.setText("----------------");
                    txtespacio2.setHeight(70);
                    txtespacio2.setGravity(Gravity.CENTER_VERTICAL);

                    lins.addView(txtespacio1);
                    ltrat.addView(txtespacio2);
                }

                TextView txttrat = new TextView(getContext());
                txttrat.setTypeface(null, Typeface.BOLD);
                txttrat.setText("Tratamiento:");
                txttrat.setHeight(70);
                txttrat.setGravity(Gravity.CENTER_VERTICAL);

                ltrat.addView(txttrat);

                CheckBox chktrat = new CheckBox(getContext());
                chktrat.setTypeface(null, Typeface.BOLD);
                chktrat.setId(Integer.parseInt(tratamientos[i][0]));
                chktrat.setText(tratamientos[i][2]);
                chktrat.setHeight(70);
                chktrat.setGravity(Gravity.CENTER_VERTICAL);
                chk_trat.add(chktrat);



                lins.addView(chktrat);


                cs_tratamiento trat = new cs_tratamiento();
                trat.setIdtrat(Integer.parseInt(tratamientos[i][0]));
                trat.setTratamiento(tratamientos[i][2]);
                List_trat.add(trat);



                String texto = "insumos";
                texto = texto.concat(valtv.toString());
                JSONArray json2 = jsonObject.optJSONArray(texto);
                insumos = new String[json2.length()][2];

                TextView tit = new TextView(getContext());
                tit.setTypeface(null, Typeface.BOLD);
                tit.setText("Insumo:");
                tit.setGravity(Gravity.CENTER_VERTICAL);
                ltrat.addView(tit);

                TextView tic = new TextView(getContext());
                tic.setTypeface(null, Typeface.BOLD);
                tic.setText("Cantidad:");
                tic.setGravity(Gravity.CENTER_VERTICAL);
                lins.addView(tic);

                for(int j=0; j<json2.length(); j++) {

                    JSONObject jsonObject2 = null;
                    jsonObject2 = json2.getJSONObject(j);
                    insumos[j][0] = jsonObject2.optString("in_insumo");
                    insumos[j][1] = jsonObject2.optString("in_cantidad");

                    TextView ins = new TextView(getContext());
                    ins.setText(insumos[j][0]);
                    ins.setGravity(Gravity.CENTER_VERTICAL);
                    ltrat.addView(ins);

                    TextView cnt = new TextView(getContext());
                    cnt.setText(insumos[j][1]);
                    cnt.setGravity(Gravity.CENTER_VERTICAL);
                    lins.addView(cnt);

                }





            }



            JSONArray json3 = response.optJSONArray("consideraciones");
            consideraciones = new String[json3.length()][2];
            List_consi = new ArrayList<>();
            chk_consi = new ArrayList<>();

            for(int k=0; k<json3.length(); k++){
                JSONObject jsonObject3=null;
                jsonObject3 = json3.getJSONObject(k);
                consideraciones[k][0] = jsonObject3.optString("cs_codigo");
                //consideraciones[k][1] = jsonObject3.optString("cs_consideracion");

                consideraciones[k][1] = new String(jsonObject3.getString("cs_consideracion").getBytes("ISO-8859-1"), "UTF-8");

                cs_consideracion consi = new cs_consideracion();
                consi.setIdconsi(Integer.parseInt(consideraciones[k][0]));
                consi.setConsideracion(consideraciones[k][1]);
                List_consi.add(consi);



                CheckBox ch = new CheckBox(getContext());
                ch.setId(Integer.parseInt(consideraciones[k][0]));
                ch.setText(consideraciones[k][1]);
                chk_consi.add(ch);
                lcon.addView(ch);



            }


            Actualizar_checks();

        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




    }
}
