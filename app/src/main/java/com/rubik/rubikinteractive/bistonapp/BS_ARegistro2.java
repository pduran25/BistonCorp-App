package com.rubik.rubikinteractive.bistonapp;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import com.rubik.rubikinteractive.bistonapp.Entidades.ar_area;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BS_ARegistro2 extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener{



    CardView btnSgt;
    CardView btnVolver;
    LinearLayout ll;
    TableLayout tl;
    private String[]header ={"Area","Plaga","Poblacion", "Tratamiento", "Eliminar"};
    private ArrayList<String[]> rows = new ArrayList<>();

    //Para la conexion
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    private int codvxa, codvis, idreturn;
    TablaDinamica tbd;

    String [][] areas;
    String [][] plagas;
    String [][] poblacion;
    String [][] tratamientos;

    String[] strareas;
    String[] strplagas;
    String[] strpoblacion;
    String[] strtratamiento;

    List<String> listaAreas;
    ArrayAdapter areaAdapter;

    List<String> listaPlagas;
    ArrayAdapter plagaAdapter;

    List<String> listaPoblacion;
    ArrayAdapter poblacionAdapter;

    List<String> listaTratamiento;
    ArrayAdapter tratamientoAdapter;

    String selnomarea;
    int selarea;

    String selnomplaga;
    int selplaga;

    String selnompob;
    int selpob;

    String seltratamiento;
    int seltrat;


    int num_est;

    BS_ARegistro3 registro3;

    Spinner spareas;
    Spinner splagas;
    Spinner spoblacion;
    Spinner stratamiento;

    CardView btnAgregar;
    CardView btnEliminar;

    ar_area areasel;
    ArrayList<ar_area> Listado_sel;
    ArrayList<cs_tratamiento> Listado_trat;
    String [] agrega;

    int num_index;

    BS_ARegistro1 registro1;
    BS_ARegistro3_5 registro35;
    BS_ARegistro4 registro4;
    BS_RegistroImg registroimg;
    ControlServicio control;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    int regestacion = 0;
    Utilidades manager;
    int exist_lamp = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Control de Servicio");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_apregistro2, container, false);
        tl = (TableLayout)ll.findViewById(R.id.tl);



        tbd = new TablaDinamica(tl,getContext());
        tbd.addHeader(header);
        //tbd.addData(getData());
        tbd.backgroundHeader(Color.parseColor("#F26524"));
        tbd.backgroundData(Color.WHITE,Color.GRAY);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
        }

        manager = new Utilidades(getContext());

        num_index = 0;
        num_est = 0;
        btnSgt = (CardView)ll.findViewById(R.id.btn_siguiente);
        btnVolver = (CardView)ll.findViewById(R.id.btn_volver);


        btnSgt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Listado_sel.size()>0){
                    Almacenar_areas();
                    FragmentManager manager = getFragmentManager();

                    /*if(num_est>0 && regestacion==1) {
                        registro3 = new BS_ARegistro3();
                        Bundle bundle = new Bundle();
                        bundle.putInt("codvxa", codvxa);
                        bundle.putInt("codvis", codvis);
                        bundle.putInt("idreturn", idreturn);
                        bundle.putInt("numest",num_est);
                        bundle.putInt("regestacion",regestacion);
                        registro3.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.contenedorap, registro3).commit();
                    }else if(exist_lamp==1) {
                        registro35 = new BS_ARegistro3_5();
                        Bundle bundle = new Bundle();
                        bundle.putInt("codvxa",codvxa);
                        bundle.putInt("codvis",codvis);
                        bundle.putInt("idreturn",idreturn);
                        bundle.putInt("numest",num_est);
                        bundle.putInt("regestacion",regestacion);
                        bundle.putInt("existLamp",exist_lamp);
                        registro35.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.contenedorap, registro35).commit();
                    }else{
*/
                        registroimg = new BS_RegistroImg();
                        Bundle bundle = new Bundle();
                        bundle.putInt("codvxa",codvxa);
                        bundle.putInt("codvis",codvis);
                        bundle.putInt("idreturn",idreturn);
                        bundle.putInt("numest",num_est);
                        bundle.putInt("regestacion",regestacion);
                        bundle.putInt("existLamp",exist_lamp);
                        registroimg.setArguments(bundle);
                        manager.beginTransaction().replace(R.id.contenedorap, registroimg).commit();

                }else{
                    Toast.makeText(getContext(), "No ha seleccionado ninguna Area", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                volverAtras2();
            }
        });


        Listado_trat = Utilidades.serv_activo.getList_trat();

        Inicializardatos();


        btnAgregar = (CardView) ll.findViewById(R.id.btn_agregar);

        btnAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(selarea != 0 && selplaga != 0 && selpob != 0){
                    areasel = new ar_area(selarea,selnomarea, selplaga, selnomplaga, selpob, selnompob, seltrat, seltratamiento);
                    Listado_sel.add(areasel);
                    agrega[0] = selnomarea;
                    agrega[1] = selnomplaga;
                    agrega[2] = selnompob;
                    agrega[3] = seltratamiento;
                    agrega[4] = num_index+ "";
                    tbd.addItem(agrega);
                    num_index++;
                }
            }
        });

        btnEliminar = (CardView) ll.findViewById(R.id.btn_eliminar);

        btnEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<Integer> Listselect = tbd.getSelected();
                int i=0;
                if(Listselect.size()>0 && Listselect.size()<2){
                    while(i<Listselect.size()){


                        if(Listado_sel.remove(Listado_sel.get(Listselect.get(i))))
                            Toast.makeText(getContext(), "Eliminado exitosamente", Toast.LENGTH_SHORT).show();

                        i++;
                    }
                }else if(Listselect.size()>=2){
                    Toast.makeText(getContext(), "Por favor elija un solo item.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "No ha seleccionado ningun item.", Toast.LENGTH_SHORT).show();
                }

                BorrarTabla();
            }
        });



        spareas = (Spinner) ll.findViewById(R.id.spArea);
        spareas.setOnItemSelectedListener(this);

        splagas = (Spinner) ll.findViewById(R.id.spPlaga);
        splagas.setOnItemSelectedListener(this);


        spoblacion = (Spinner) ll.findViewById(R.id.spPoblacion);
        spoblacion.setOnItemSelectedListener(this);

        stratamiento = (Spinner) ll.findViewById(R.id.spTratamiento);
        stratamiento.setOnItemSelectedListener(this);

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        request = Volley.newRequestQueue(getContext());
        CargarDatosAreas();

        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
        ll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtras2();
                    return true;
                }
                return false;
            }
        });


        return ll;

    }

    private void volverAtras2() {

        Almacenar_areas();
       /* if(exist_lamp == 1){
            FragmentManager manager = getFragmentManager();
            registro1 = new BS_ARegistro1();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa", codvxa);
            bundle.putInt("codvis", codvis);
            bundle.putInt("idreturn", idreturn);
            registro1.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro1).commit();
        }*/

        FragmentManager manager = getFragmentManager();



        if(exist_lamp == 1){
            registro35 = new BS_ARegistro3_5();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa",codvxa);
            bundle.putInt("codvis",codvis);
            bundle.putInt("idreturn",idreturn);
            bundle.putInt("numest",num_est);
            bundle.putInt("regestacion",regestacion);
            bundle.putInt("existLamp", exist_lamp);
            registro35.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro35).commit();
        }else if(num_est>0 && regestacion==1) {
            registro3 = new BS_ARegistro3();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa",codvxa);
            bundle.putInt("codvis",codvis);
            bundle.putInt("idreturn",idreturn);
            bundle.putInt("numest",num_est);
            bundle.putInt("regestacion",regestacion);
            bundle.putInt("existLamp", exist_lamp);
            registro3.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro3).commit();
        }else {
            registro1 = new BS_ARegistro1();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa",codvxa);
            bundle.putInt("codvis",codvis);
            bundle.putInt("idreturn",idreturn);
            registro1.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro1).commit();
        }


    }


    private void Almacenar_areas(){
        control = Utilidades.serv_activo;
        control.setList_area(Listado_sel);
        Utilidades.serv_activo = control;

        manager.EliminarAreas(codvxa);
        manager.insertar_listarea(Listado_sel, codvxa);

    }



    private void Inicializardatos(){
        Listado_sel = new ArrayList<>();
        selarea = 0;
        selplaga = 0;
        selpob = 0;
        seltrat = 0;
        agrega = new String[5];


        selnomarea = "";
        selnomplaga = "";
        selnompob = "";
        seltratamiento = "";

    }


    private void CargarDatosAreas() {
        prgDialog.show();
            String url = "https://app.bistoncorp.com/php/connect_app/db_getInfodatos.php?idvisita=" + codvis;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, null);
            jsonObjectRequest.setShouldCache(false);
            request.add(jsonObjectRequest);
    }

    private ArrayList<String[]> getData(){
        num_index =0;
        ar_area area;
        while(num_index<Listado_sel.size()){
            area = (ar_area)Listado_sel.get(num_index);
            agrega[0] = area.getAr_nombarea();
            agrega[1] = area.getAr_nombplaga();
            agrega[2] = area.getAr_nombpob();
            agrega[3] = num_index+ "";
            rows.add(agrega);
            num_index++;
        }
        return rows;
    }

    private void BorrarTabla(){
        tbd.RemoveTable();
        tbd = new TablaDinamica(tl,getContext());
        tbd.addHeader(header);
        tbd.backgroundHeader(Color.parseColor("#F26524"));
        tbd.backgroundData(Color.WHITE,Color.GRAY);

        num_index =0;
        ar_area area;
        while(num_index<Listado_sel.size()){
            area = (ar_area)Listado_sel.get(num_index);
            agrega[0] = area.getAr_nombarea();
            agrega[1] = area.getAr_nombplaga();
            agrega[2] = area.getAr_nombpob();
            agrega[3] = area.getAr_tratamiento();
            agrega[4] = num_index+ "";
            tbd.addItem(agrega);
            num_index++;
        }
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
        prgDialog.dismiss();
        JSONArray json = response.optJSONArray("areas");
        areas = new String[json.length()][2];
        strareas = new String[json.length()];

        JSONArray json2 = response.optJSONArray("plagas");
        plagas = new String[json2.length()][2];
        strplagas = new String[json2.length()];

        JSONArray json4 = response.optJSONArray("poblacion");
        poblacion = new String[json4.length()][2];
        strpoblacion = new String[json4.length()];

        tratamientos = new String[Listado_trat.size()][2];
        strtratamiento = new String[Listado_trat.size()];

        try {

            JSONArray json3 = response.optJSONArray("estaciones");
            JSONObject jsonObject3=null;
            jsonObject3 = json3.getJSONObject(0);
            num_est = Integer.parseInt(jsonObject3.optString("num_estacion"));
            regestacion = Integer.parseInt(jsonObject3.optString("regestacion"));

            JSONArray jsona = response.optJSONArray("reglampara");
            JSONObject jsonObjecta=null;
            jsonObjecta = jsona.getJSONObject(0);
            exist_lamp = Integer.parseInt(jsonObjecta.optString("lp_reglamp"));

            for(int i=0; i<json.length(); i++){
                JSONObject jsonObject=null;
                jsonObject = json.getJSONObject(i);
                areas[i][0] = jsonObject.optString("ar_codigo");
                areas[i][1] = new String(jsonObject.getString("ar_nombarea").getBytes("ISO-8859-1"), "UTF-8");



                strareas[i] = areas[i][1];
            }



            listaAreas = new ArrayList<>();
            Collections.addAll(listaAreas, strareas);
            //Implemento el adapter con el contexto, layout, listaFrutas
            areaAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, listaAreas);
            //Cargo el spinner con los datos
            spareas.setAdapter(areaAdapter);

            for(int j=0; j<json2.length(); j++){
                JSONObject jsonObject2=null;
                jsonObject2 = json2.getJSONObject(j);
                plagas[j][0] = jsonObject2.optString("pl_codigo");
                plagas[j][1] =  new String(jsonObject2.getString("pl_nombplaga").getBytes("ISO-8859-1"), "UTF-8");
                strplagas[j] = plagas[j][1];

            }

            listaPlagas = new ArrayList<>();
            Collections.addAll(listaPlagas, strplagas);
            //Implemento el adapter con el contexto, layout, listaFrutas
            plagaAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, listaPlagas);
            //Cargo el spinner con los datos
            splagas.setAdapter(plagaAdapter);


            for(int l=0; l<json4.length(); l++){
                JSONObject jsonObject4=null;
                jsonObject4 = json4.getJSONObject(l);
                poblacion[l][0] = jsonObject4.optString("pb_codigo");
                poblacion[l][1] = new String(jsonObject4.getString("pb_poblacion").getBytes("ISO-8859-1"), "UTF-8");
                strpoblacion[l] = poblacion[l][1];
            }

            listaPoblacion = new ArrayList<>();
            Collections.addAll(listaPoblacion, strpoblacion);
            //Implemento el adapter con el contexto, layout, listaFrutas
            poblacionAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, listaPoblacion);
            //Cargo el spinner con los datos
            spoblacion.setAdapter(poblacionAdapter);

            cs_tratamiento trat;

            for(int m=0; m<Listado_trat.size();m++){
                trat = Listado_trat.get(m);
                tratamientos[m][0] = Integer.toString(trat.getIdtrat());
                tratamientos[m][1] = trat.getTratamiento();
                strtratamiento[m] = tratamientos[m][1];
            }

            listaTratamiento = new ArrayList<>();
            Collections.addAll(listaTratamiento, strtratamiento);
            tratamientoAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, listaTratamiento);

            stratamiento.setAdapter(tratamientoAdapter);


            CargarDatosExistentes();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CargarDatosExistentes(){
        if(Utilidades.serv_activo.getList_area()!=null){
            Listado_sel = new ArrayList<>();
            Listado_sel = Utilidades.serv_activo.getList_area();
            num_index = 0;
            while(num_index<Listado_sel.size()){
                areasel = Listado_sel.get(num_index);
                agrega[0] = areasel.getAr_nombarea();
                agrega[1] = areasel.getAr_nombplaga();
                agrega[2] = areasel.getAr_nombpob();
                agrega[3] = areasel.getAr_tratamiento();
                agrega[4] = num_index+ "";
                tbd.addItem(agrega);
                num_index++;
            }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spArea:
                selarea = Integer.parseInt(areas[position][0]);
                selnomarea = areas[position][1];

                break;

            case R.id.spPlaga:
                selplaga = Integer.parseInt(plagas[position][0]);
                selnomplaga = plagas[position][1];
                break;

            case R.id.spPoblacion:
                selpob = Integer.parseInt(poblacion[position][0]);
                selnompob = poblacion[position][1];
                break;

            case R.id.spTratamiento:
                seltrat = Integer.parseInt(tratamientos[position][0]);
                seltratamiento = tratamientos[position][1];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
