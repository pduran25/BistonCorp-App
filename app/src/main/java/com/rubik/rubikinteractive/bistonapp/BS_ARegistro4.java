package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.app.TimePickerDialog;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.provider.MediaStore;

import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_idimagen;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class BS_ARegistro4 extends Fragment {


    CardView btnGuarda,btnLimpiar, btnVolver;
    LinearLayout ll;
    LinearLayout canvas;
    Vista vista;
    private int codvxa, codvis, idreturn, numest, regestacion, reglampara;
    EditText edtObs;
    CheckBox chkprev, chkcorr, chkhora, chkfoto;
    EditText edtprev, edtcorr, edthora, edtidauto, edtauto;
    ControlServicio control;
    String hora_fin;
    BS_RegistroImg registroimg;

    //Para la conexion
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialoga;
    StringRequest strRequest;
    BS_ARegistro2 registro2;
    BS_ARegistro3 registro3;
    int cont_toque = 0;
    Button btnhora;
    Bitmap bitmap_firma;
    Calendar c;
    String val_hora = "";
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private Utilidades manager;
    ArrayList<cs_idimagen> list_regimagen;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Control de servicio 4 de 4");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_apregistro4, container, false);


       // getActivity().setContentView(vista);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
            numest = getArguments().getInt("numest",0);
            regestacion = getArguments().getInt("regestacion",0);
            reglampara = getArguments().getInt("existLamp",0);
        }

        edtObs = (EditText)ll.findViewById(R.id.edtObvs);
        chkprev = (CheckBox)ll.findViewById(R.id.chkAccprv);
        chkcorr = (CheckBox)ll.findViewById(R.id.chkAccCorr);
        chkhora = (CheckBox)ll.findViewById(R.id.chkHoraRI);
        chkfoto = (CheckBox)ll.findViewById(R.id.chkFotos);

        edtprev = (EditText)ll.findViewById(R.id.edtPrev);
        edtcorr = (EditText)ll.findViewById(R.id.edtCorr);
        edthora = (EditText)ll.findViewById(R.id.edtHoraRI);
        btnhora = (Button)ll.findViewById(R.id.btnHora);
        edtauto = (EditText)ll.findViewById(R.id.Edtauto);
        edtidauto = (EditText)ll.findViewById(R.id.Edtidauto);

        canvas = (LinearLayout)ll.findViewById(R.id.ly_canvas);
        vista = new Vista(canvas.getContext(), canvas.getX(), canvas.getY());

        canvas.addView(vista);
        manager = new Utilidades(getContext());

        list_regimagen = new ArrayList<>();

        btnhora.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(chkhora.isChecked()) {
                    c = Calendar.getInstance();
                    int hora = c.get(Calendar.HOUR_OF_DAY);
                    int min = c.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String txthor, txtmin;
                            if (hourOfDay < 10)
                                txthor = "0" + hourOfDay;
                            else
                                txthor = "" + hourOfDay;

                            if (minute < 10)
                                txtmin = "0" + minute;
                            else
                                txtmin = "" + minute;
                            val_hora = txthor + ":" + txtmin;
                            edthora.setText(val_hora);
                        }
                    }, hora, min, true);
                    timePickerDialog.show();
                }

            }

        });

        btnGuarda = (CardView)ll.findViewById(R.id.btn_guardar);
        btnGuarda.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(edtObs.getText().toString().compareTo("")!=0){
                    if(cont_toque == 0){
                        if(edtidauto.getText().toString().compareTo("")!=0 && edtauto.getText().toString().compareTo("")!=0){
                            AlmacenarObservacion();
                            prgDialoga = new ProgressDialog(getContext());
                            prgDialoga.setMessage("Cargando...");
                            prgDialoga.setCancelable(false);
                            request = Volley.newRequestQueue(getContext());
                            EnviarDatos();
                            Utilidades.ListImagen = null;
                            strRequest.setShouldCache(false);
                            request.add(strRequest);
                            cont_toque ++;
                        }else
                            Toast.makeText(getContext(), "Falta llenar información de la persona Autorizada", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getContext(), "Falta llenar una Observacion", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLimpiar = (CardView)ll.findViewById(R.id.btn_limpiar);

        btnLimpiar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Limpiando la imagen", Toast.LENGTH_SHORT).show();
                vista.limpiar_firma();
            }
        });

        btnVolver = (CardView)ll.findViewById(R.id.btn_volver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtras4();


            }
        });

        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
        ll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(prgDialoga != null)
                   prgDialoga.dismiss();
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtras4();
                    return true;
                }else
                     return false;
            }
        });


        CargarDatosExistentes();




        return ll;

    }


    private void guardarFirma(){
        vista.setDrawingCacheEnabled(true);
        String imgSaved = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), vista.getDrawingCache(), UUID.randomUUID().toString()+".png","drawing");


        bitmap_firma = Bitmap.createBitmap(vista.getDrawingCache());
       // bitmap_firma = vista.getBitmapFromView(vista);

        if(imgSaved!=null){
           // Toast savedToast = Toast.makeText(getContext(),"Firma ha sido Grabada!", Toast.LENGTH_SHORT);
           // savedToast.show();
        }else{
          //  Toast unsavedToast = Toast.makeText(getContext(),"Error! Firma no ha sido Grabada!", Toast.LENGTH_SHORT);
          //  unsavedToast.show();
        }

        vista.destroyDrawingCache();
    }




    private void volverAtras4() {

        AlmacenarObservacion();
        FragmentManager managera = getFragmentManager();
        registroimg = new BS_RegistroImg();
        Bundle bundle = new Bundle();
        bundle.putInt("codvxa",codvxa);
        bundle.putInt("codvis",codvis);
        bundle.putInt("idreturn",idreturn);
        bundle.putInt("numest",numest);
        bundle.putInt("regestacion",regestacion);
        bundle.putInt("existLamp",reglampara);
        registroimg.setArguments(bundle);
        managera.beginTransaction().replace(R.id.contenedorap, registroimg).commit();

    }

    public void CargarDatosExistentes() {
        if(Utilidades.serv_activo.getObs()!=null){
            control = Utilidades.serv_activo;
            edtObs.setText(control.getObs());
            if(control.getChkprev()==1){
                chkprev.setChecked(true);
                edtprev.setText(control.getAccprev());
            }
            if(control.getChkcorr()==1){
                chkcorr.setChecked(true);
                edtcorr.setText(control.getAcccorr());
            }
            if(control.getChkhora()==1){
                chkhora.setChecked(true);
                edthora.setText(control.getHorri());
            }
            edtauto.setText(control.getAutorizado());
            edtidauto.setText(control.getIdauto());
        }
    }

    private void AlmacenarObservacion(){
        guardarFirma();
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        String txt_hor, txt_min;
        int hor = today.hour;
        int min = today.minute;

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
        hora_fin =txt_hor+":"+txt_min;

        control = Utilidades.serv_activo;
        control.setObs(edtObs.getText().toString());

        //accion preventiva
        if(chkprev.isChecked()){
            control.setChkprev(1);
            control.setAccprev(edtprev.getText().toString());
        }else{
            control.setChkprev(0);
            control.setAccprev("");
        }

        //accion correctiva
        if(chkcorr.isChecked()){
            control.setChkcorr(1);
            control.setAcccorr(edtcorr.getText().toString());
        }else{
            control.setChkcorr(0);
            control.setAcccorr("");
        }

        control.setFirma(bitmap_firma);


        //hora reingreso
        if(chkhora.isChecked()){
            control.setChkhora(1);
            control.setHorri(edthora.getText().toString());
        }else{
            control.setChkhora(0);
            control.setHorri("");
        }

        if(chkfoto.isChecked()){
            control.setChkfoto(1);
        }else{
            control.setChkfoto(0);
        }

        control.setHora_fin(hora_fin);
        control.setNumest(numest);
        control.setIdauto(edtidauto.getText().toString());
        control.setAutorizado(edtauto.getText().toString());
        control.setRegestacion(regestacion);
        control.setReglampara(reglampara);
        Utilidades.serv_activo = control;

        manager.Actualiza_control2(control);

    }

    private void EnviarDatos() {
        prgDialoga.show();
        String url = "https://app.bistoncorp.com/php/connect_app/db_sendDatos.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                prgDialoga.hide();
                prgDialoga.dismiss();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                FragmentManager managera = getFragmentManager();
                managera.beginTransaction().replace(R.id.contenedorap, new BS_AProximaVisita()).commit();
                EliminarInformacion();

            }
        },
        new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error)
            {
                prgDialoga.hide();
                prgDialoga.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                control.setImgdisp(0);
                list_regimagen = null;
                if(control.getChkfoto()==0){
                    if(control.getList_regimagen().size()>0){
                        control.setImgdisp(1);
                    }
                    list_regimagen = control.getList_regimagen();
                    control.setList_regimagen(null);
                }
                String newDataArray = gson.toJson(control);
                params.put("arreglo", newDataArray);
                return params;
            }
        };
    }

    private void EliminarInformacion() {
        manager.EliminarControl(codvxa);
        if(list_regimagen!=null && list_regimagen.size()>0){
            registrar_dispositivoimg(codvxa);
        }
    }


    private void registrar_dispositivoimg(int idvisxapli) {
        int i = 0;
        manager = new Utilidades(getContext());
        cs_idimagen idtemp;
        for(i=0;i<list_regimagen.size();i++){
            idtemp = list_regimagen.get(i);
            idtemp.setIdvisxapli(idvisxapli);
            manager.insertar_IMAGEN(idtemp,idvisxapli);
        }


    }





    public void onStart(){
        super.onStart();
    }






}
