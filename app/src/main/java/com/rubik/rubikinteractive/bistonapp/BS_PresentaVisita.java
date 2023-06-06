package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_idimagen;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_listReg;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BS_PresentaVisita extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, CalificaDialog.CalificaDialogListener {



    CardView btnVolver;
    CardView btnCalificar;
    CardView btnSubir, btnVer;
    RatingBar rt_estrellas;
    TextView txt_estcalf;
    private Utilidades manager;
    ArrayList<cs_idimagen> List_regimagen;

    ControlServicio control;

    LinearLayout ll;
    TableLayout tltrat, tlcon, tlarea, tlesta, tllamp;

    private String[]htratamiento ={"Tratamiento","Insumo","Medida"};
    private String[]hconsi ={"Consideracion"};
    private String[]hareas ={"Areas","Plaga Tratada","Población"};
    private String[]hestacion ={"Numero","Estado"};
    private String[]hlampara ={"Numero","Población"};

    String fecha, hini, hfin, orga, esta, dir, sector, autorizado, observacion, hri, accprev, acccorr, idautorizado,fechaest, hiniest, hfinest;

    String [][] tratamiento;
    String [][] consideracion;
    String [][] areas;
    String [][] estaciones;
    String [][] lamparas;


    private ArrayList<String[]> rowstrat = new ArrayList<>();
    private ArrayList<String[]> rowsconsi = new ArrayList<>();
    private ArrayList<String[]> rowsinsu = new ArrayList<>();
    private ArrayList<String[]> rowsarea = new ArrayList<>();
    private ArrayList<String[]> rowsestacion = new ArrayList<>();
    //

    //Para la conexion
    RequestQueue request, request2, request3;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog, prgDialog2,prgDialog3;
    private int idcontrol, retorna;
    private TablaDinamica tbtrat, tbcon, tbarea, tbesta, tblamp;
    TextView txt_org, txt_estab, txt_fvisita, txt_hinicio, txt_hfin, txt_sector, txt_auto, txt_dir, txt_trat, txt_obs, txt_ri, txt_accprev, txt_acccorr, txt_nomapli, txt_finiest, txt_hiniest, txt_hfinest;
    int num_index;
    int idaplicador;
    float puntuacion;
    String nomapli;
    int idvisxapli = 0;
    float puntvisita;
    int iscalificado;
    RegistroAplicador regaplica;
    StringRequest strRequest, strRequest2;
    cs_listReg listReg;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    String url = "";
    int idcrono = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Control de Servicio");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_presentavisita, container, false);

        tltrat = (TableLayout)ll.findViewById(R.id.tl_tratamiento);
        tlcon = (TableLayout)ll.findViewById(R.id.tl_consi);
        tlarea = (TableLayout)ll.findViewById(R.id.tl_areas);
        tlesta = (TableLayout)ll.findViewById(R.id.tl_estacion);
        tllamp = (TableLayout)ll.findViewById(R.id.tl_lampara);

        txt_org = (TextView)ll.findViewById(R.id.txt_org);
        txt_estab = (TextView)ll.findViewById(R.id.txt_estab);
        txt_fvisita = (TextView)ll.findViewById(R.id.txt_fvisita);
        txt_hinicio = (TextView)ll.findViewById(R.id.txt_hinicio);
        txt_hfin = (TextView)ll.findViewById(R.id.txt_hfin);
        txt_sector = (TextView)ll.findViewById(R.id.txt_sector);
        txt_dir = (TextView)ll.findViewById(R.id.txt_direccion);
        txt_trat = (TextView)ll.findViewById(R.id.txt_tratamiento);
        txt_obs = (TextView)ll.findViewById(R.id.txt_obs);
        txt_ri = (TextView)ll.findViewById(R.id.txtReingreso);
        txt_accprev = (TextView)ll.findViewById(R.id.txt_prev);
        txt_acccorr = (TextView)ll.findViewById(R.id.txt_corr);
        txt_estcalf = (TextView)ll.findViewById(R.id.txt_estcalf);
        txt_nomapli = (TextView)ll.findViewById(R.id.txt_nomapli);
        txt_auto = (TextView)ll.findViewById(R.id.txt_nomauto);
        txt_finiest = (TextView)ll.findViewById(R.id.txt_fvisitaest);
        txt_hiniest = (TextView)ll.findViewById(R.id.txt_hinicioest);
        txt_hfinest = (TextView)ll.findViewById(R.id.txt_hfinest);


        if(getArguments()!= null){
            idcontrol =  getArguments().getInt("idcontrol",0);
            retorna =  getArguments().getInt("return",0);
        }

        tbtrat = new TablaDinamica(tltrat,getContext());
        tbtrat.addHeader(htratamiento);
        tbtrat.backgroundHeader(Color.parseColor("#F26524"));
        tbtrat.backgroundData(Color.WHITE,Color.GRAY);

        tbcon = new TablaDinamica(tlcon,getContext());
        tbcon.addHeader(hconsi);
        tbcon.backgroundHeader(Color.parseColor("#F26524"));
        tbcon.backgroundData(Color.WHITE,Color.GRAY);

        tbarea = new TablaDinamica(tlarea,getContext());
        tbarea.addHeader(hareas);
        tbarea.backgroundHeader(Color.parseColor("#F26524"));
        tbarea.backgroundData(Color.WHITE,Color.GRAY);

        tbesta = new TablaDinamica(tlesta,getContext());
        tbesta.addHeader(hestacion);
        tbesta.backgroundHeader(Color.parseColor("#F26524"));
        tbesta.backgroundData(Color.WHITE,Color.GRAY);

        tblamp = new TablaDinamica(tllamp,getContext());
        tblamp.addHeader(hlampara);
        tblamp.backgroundHeader(Color.parseColor("#F26524"));
        tblamp.backgroundData(Color.WHITE,Color.GRAY);


        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        request = Volley.newRequestQueue(getContext());
        CargarDatosControl();

        btnCalificar = (CardView)ll.findViewById(R.id.btn_calificar);
        rt_estrellas = (RatingBar) ll.findViewById(R.id.rt_estrellas);
       // rt_estrellas.setIsIndicator(true);
        txt_estcalf.setText("No ha sido calificada aún");

        if(retorna == 1){
            btnCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openDialog();
                    if(iscalificado == 0){
                        txt_estcalf.setText("Tu calificación de esta visitas es:" + rt_estrellas.getRating());
                        rt_estrellas.setIsIndicator(true);
                        btnCalificar.setVisibility(View.INVISIBLE);
                        iscalificado = 1;
                        puntvisita = rt_estrellas.getRating();
                        if(puntuacion == 0)
                            puntuacion = puntvisita;
                        else
                            puntuacion = (puntvisita + puntuacion)/2;

                        regaplica = new RegistroAplicador();
                        regaplica.setIdaplicador(idaplicador);
                        regaplica.setIdvisxapli(idvisxapli);
                        regaplica.setPuntuacion(puntuacion);
                        regaplica.setPuntvisita(puntvisita);

                        prgDialog2 = new ProgressDialog(getContext());
                        prgDialog2.setMessage("Cargando...");
                        prgDialog2.setCancelable(false);
                        request2 = Volley.newRequestQueue(getContext());
                        Enviar_Puntuacion();
                        strRequest.setShouldCache(false);
                        request2.add(strRequest);

                    }

                }
            });
        }

        num_index = 0;

        btnVolver = (CardView)ll.findViewById(R.id.btn_volve);

        btnVer = (CardView)ll.findViewById(R.id.btn_verimg);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //btnVolver.setVisibility(View.INVISIBLE);
        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               volverAtras();

            }
        });

        btnSubir = (CardView)ll.findViewById(R.id.btn_subirimg);
        btnSubir.setVisibility(View.INVISIBLE);


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
        if(retorna == 1){
            FragmentManager managera = getFragmentManager();
            managera.beginTransaction().replace(R.id.contenedor, new BS_ClienteVisitada()).commit();
        }else{
            FragmentManager managera = getFragmentManager();
            managera.beginTransaction().replace(R.id.contenedorap, new BS_AVisitada()).commit();
        }
    }


    private void Enviar_Puntuacion() {
        prgDialog2.show();
        String url = "https://app.bistoncorp.com/php/connect_app/db_savePuntuacion.php";

        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                prgDialog2.hide();
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
                String newDataArray = gson.toJson(regaplica);
                params.put("regaplica", newDataArray);
                return params;
            }
        };
    }


    private void CargarDatosControl() {
        prgDialog.show();
            String url = "https://app.bistoncorp.com/php/connect_app/db_getInfodetalle.php?idcontrol=" + idcontrol;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, null);
            jsonObjectRequest.setShouldCache(false);
            request.add(jsonObjectRequest);
    }


    private ArrayList<String[]> getDataconsi(){
        String [] consi = new String[1];
        for(int i=0; i<consideracion.length; i++){
            consi[0] = consideracion[i][0];
            rowsconsi.add(consi);
        }
        return rowsconsi;
    }

    private ArrayList<String[]> getDatatrat(){
        String [] insu = new String[1];
        for(int i=0; i<tratamiento.length; i++){
            insu[0] = tratamiento[i][0];
            rowsinsu.add(insu);
        }
        return rowsinsu;
    }

    private ArrayList<String[]> getDataarea(){
        String [] are = new String[6];
        for(int i=0; i<areas.length; i++){
            are[0] = areas[i][0];
            are[1] = areas[i][1];
            are[2] = areas[i][2];
            are[3] = areas[i][3];
            are[4] = areas[i][4];
            are[5] = areas[i][5];
            rowsarea.add(are);
        }
        return rowsarea;
    }

    private ArrayList<String[]> getDataestacion(){
        String [] esta = new String[3];
        for(int i=0; i<estaciones.length; i++){
            esta[0] = estaciones[i][0];
            esta[1] = estaciones[i][1];
            esta[2] = estaciones[i][2];
            rowsestacion.add(esta);
        }
        return rowsestacion;
    }

    public void onStart(){
        super.onStart();
        initControls();
    }

    private void initControls(){

    }

    @Override
    public void onResponse(JSONObject response) {
        prgDialog.hide();

        try {
            JSONArray json = response.optJSONArray("cabecera");
            JSONObject jsonObject=null;
            jsonObject = json.getJSONObject(0);
            fecha = jsonObject.optString("ca_fecha");
            hini = jsonObject.optString("ca_hini");
            hfin = jsonObject.optString("ca_hfin");
            orga = jsonObject.optString("ca_org");
            esta = jsonObject.optString("ca_est");
            dir  = jsonObject.optString("ca_dir");
            sector  = jsonObject.optString("ca_sector");
            autorizado  = jsonObject.optString("ca_auto");
            idautorizado  = jsonObject.optString("ca_idauto");
            observacion  = jsonObject.optString("ca_obs");
            hri  = jsonObject.optString("ca_horari");
            accprev  = jsonObject.optString("ca_accprev");
            acccorr  = jsonObject.optString("ca_acccorr");
            idaplicador = jsonObject.getInt("ca_codapli");
            puntuacion = Float.parseFloat(jsonObject.getString("ca_puntuacion"));
            nomapli = jsonObject.optString("ca_aplicador");
            txt_nomapli.setText(nomapli);

            idvisxapli = jsonObject.getInt("ca_idvisxapli");
            idcrono = jsonObject.getInt("ca_idcrono");
            iscalificado = jsonObject.getInt("ca_iscalificado");
            puntvisita = Float.parseFloat(jsonObject.getString("ca_puntvisita"));

            url = "https://app.bistoncorp.com/imgCabControl.php?idcontrol="+idcontrol+"&idcrono="+idcrono;

            JSONArray jsonvis = response.optJSONArray("fechavisita");
            JSONObject jsonVisita=null;
            jsonVisita = jsonvis.getJSONObject(0);

            fechaest = jsonVisita.optString("fv_inicio");
            hiniest = jsonVisita.optString("fv_hini");
            hfinest = jsonVisita.optString("fv_hfin");

            if(retorna != 1){
                btnCalificar.setVisibility(View.INVISIBLE);
                rt_estrellas.setIsIndicator(true);
            }

            if(iscalificado != 0){
                txt_estcalf.setText("Tu calificación de esta visita es: " + puntvisita);
                rt_estrellas.setRating(puntvisita);
                rt_estrellas.setIsIndicator(true);
                btnCalificar.setVisibility(View.INVISIBLE);
            }

            if(retorna == 2){
                if(Consultar_contiene_imagenes(idvisxapli)>0){
                    btnSubir.setVisibility(View.VISIBLE);
                    btnSubir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            prgDialog3 = new ProgressDialog(getContext());
                            prgDialog3.setMessage("Cargando...");
                            prgDialog3.setCancelable(false);
                            request3 = Volley.newRequestQueue(getContext());
                            subir_Imagenes(idvisxapli);
                            strRequest2.setShouldCache(false);
                            request3.add(strRequest2);

                        }
                    });
                }
            }


            JSONArray json2 = response.optJSONArray("tratamiento");
            if(json2 != null)
                tratamiento = new String[json2.length()][3];

            JSONArray json4 = response.optJSONArray("consideracion");
            if(json4 != null)
                consideracion = new String[json4.length()][1];

            JSONArray json5 = response.optJSONArray("areas");
            areas = new String[json5.length()][3];

            JSONArray json6 = response.optJSONArray("estacion");
            if(json6 != null)
              estaciones = new String[json6.length()][2];

            JSONArray json7 = response.optJSONArray("lampara");
            if(json7 != null)
                lamparas = new String[json7.length()][2];



            String [] tr = new String[3];
            if(json2!= null) {
                for (int j = 0; j < json2.length(); j++) {
                    JSONObject jsonObject2 = null;
                    jsonObject2 = json2.getJSONObject(j);
                    tratamiento[j][0] = jsonObject2.optString("tr_nomtrata");
                    tratamiento[j][1] = jsonObject2.optString("tr_insumo");
                    tratamiento[j][2] = jsonObject2.optString("tr_cantidad");
                    tr[0] = tratamiento[j][0];
                    tr[1] = tratamiento[j][1];
                    tr[2] = tratamiento[j][2];
                    tbtrat.addItemfijo(tr);
                }
            }


            if(json4 != null){
                String [] consi = new String[1];
                for(int l=0; l<json4.length(); l++){
                    JSONObject jsonObject4=null;
                    jsonObject4 = json4.getJSONObject(l);
                    consideracion[l][0] = jsonObject4.optString("cn_consi");
                    consi[0] = consideracion[l][0];
                    tbcon.addItemfijo(consi);
                }
            }


            String [] ar = new String[3];
            for(int m=0; m<json5.length(); m++){
                JSONObject jsonObject5=null;
                jsonObject5 = json5.getJSONObject(m);

                areas[m][0] = new String(jsonObject5.optString("ar_area").getBytes("ISO-8859-1"), "UTF-8");
                areas[m][1] = new String(jsonObject5.optString("ar_plaga").getBytes("ISO-8859-1"), "UTF-8");
                areas[m][2] = new String(jsonObject5.optString("ar_poblacion").getBytes("ISO-8859-1"), "UTF-8");
                ar[0] = areas[m][0];
                ar[1] = areas[m][1];
                ar[2] = areas[m][2];
                tbarea.addItemfijo(ar);
            }

            if(json6 != null){
                String [] est = new String[2];
                for(int n=0; n<json6.length(); n++){
                    JSONObject jsonObject6=null;
                    jsonObject6 = json6.getJSONObject(n);
                    estaciones[n][0] = jsonObject6.optString("es_numero");
                    estaciones[n][1] = jsonObject6.optString("es_estado");
                    est[0] = estaciones[n][0];
                    est[1] = estaciones[n][1];
                    tbesta.addItemfijo(est);
                }
            }

            if(json7 != null){
                String [] lmp = new String[2];
                for(int n=0; n<json7.length(); n++){
                    JSONObject jsonObject7=null;
                    jsonObject7 = json7.getJSONObject(n);
                    lamparas[n][0] = jsonObject7.optString("lp_numero");
                    lamparas[n][1] = jsonObject7.optString("lp_pob");
                    lmp[0] = lamparas[n][0];
                    lmp[1] = lamparas[n][1];
                    tblamp.addItemfijo(lmp);
                }
            }


            txt_org.setText(orga);
            txt_estab.setText(esta);
            txt_fvisita.setText(fecha);
            txt_hinicio.setText(hini);
            txt_hfin.setText(hfin);
            txt_sector.setText(sector);
            txt_auto.setText(autorizado);
            txt_finiest.setText(fechaest);
            txt_hiniest.setText(hiniest);
            txt_hfinest.setText(hfinest);
            txt_dir.setText(dir);
            txt_obs.setText(observacion);
            txt_ri.setText(hri.equals("")?"------":hri);
            txt_accprev.setText(accprev.equals("")?"------":accprev);
            txt_acccorr.setText(acccorr.equals("")?"------":acccorr);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    private void subir_Imagenes(int visxapli){
        manager = new Utilidades(getContext());
        Cursor cursor = manager.CargarDatosImagen(visxapli);
        int idvisxapl = 0;
        String descripcion = "", imagen = "";
        int cant = 0;
        List_regimagen = new ArrayList<>();
        cs_idimagen regtemp;

        if (cursor.getCount() > 0) {
            int pos_id = cursor.getColumnIndex(manager.IMG_ID);
            int pos_desc = cursor.getColumnIndex(manager.IMG_DESC);
            int pos_img = cursor.getColumnIndex(manager.IMG_IMG);


            if (cursor.getCount() > 0) {
                //todos los datos del Usuario son conocidos desde este momento.
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    idvisxapl = cursor.getInt(pos_id);
                    descripcion = cursor.getString(pos_desc);
                    imagen = cursor.getString(pos_img);
                    if(idvisxapl == visxapli){
                        regtemp = new cs_idimagen();
                        regtemp.setIdvisxapli(idvisxapl);
                        regtemp.setDescripcion(descripcion);
                        regtemp.setImagen(imagen);

                        List_regimagen.add(regtemp);
                    }
                }
            }
        }



        listReg = new cs_listReg();
        listReg.setIdvisxapli(visxapli);
        listReg.setList_regimagen(List_regimagen);

        prgDialog3.show();
        String url = "https://app.bistoncorp.com/php/connect_app/db_saveImagenes.php";

        strRequest2 = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                prgDialog3.hide();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                btnSubir.setVisibility(View.INVISIBLE);
                Eliminar_Imagenes(idvisxapli);
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        prgDialog3.hide();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String newDataArray = gson.toJson(listReg);
                params.put("arreglo", newDataArray);
                return params;
            }
        };

    }

    private void Eliminar_Imagenes(int idvisxapli) {
        // EliminarImagenxIdvisxapli
        manager = new Utilidades(getContext());
        manager.EliminarImagenxIdvisxapli(idvisxapli);
    }




    private int Consultar_contiene_imagenes(int visxapli) {
        manager = new Utilidades(getContext());
        Cursor cursor = manager.CargarDatosImagen(visxapli);
        int idvisxapl = 0;
        int cant = 0;

        if (cursor.getCount() > 0) {
            int pos_id = cursor.getColumnIndex(manager.IMG_ID);



            if (cursor.getCount() > 0) {
                //todos los datos del Usuario son conocidos desde este momento.
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    idvisxapl = cursor.getInt(pos_id);

                    if(idvisxapl == visxapli)
                        cant++;
                }
            }
        }

        return cant;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        Toast.makeText(getContext(), "No se pudo conectar: "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }


    @Override
    public void applyCalification(float estrellas) {
        rt_estrellas.setRating(estrellas);
    }
}
