package com.rubik.rubikinteractive.bistonapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_lampara;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BS_ARegistro3_5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BS_ARegistro3_5 extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int codvxa, codvis, idreturn, numest, regestacion, totnum, num_index;
    private ArrayList<EditText> listaEdit;


    private String[]header ={"# Lámpara","Población"};
    String [][] lamparas;
    List<String> listaLamparas;
    TablaDinamicalmp tblamp;
    LinearLayout ll;
    TableLayout tl;
    String [] agrega;
    ControlServicio control;
    ArrayList<cs_lampara> Listado_lamp;
    CardView btn_siguiente, btn_volver;
    cs_lampara objlamp;
    Utilidades manager;
    BS_RegistroImg registroimg;
    int exist_lamp = 0;
    BS_ARegistro2 registro2;
    BS_ARegistro3 registro3;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BS_ARegistro3_5.
     */
    // TODO: Rename and change types and number of parameters
    public static BS_ARegistro3_5 newInstance(String param1, String param2) {
        BS_ARegistro3_5 fragment = new BS_ARegistro3_5();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Control de Lámparas");
        ll = (LinearLayout)inflater.inflate(R.layout.fragment_registro3_5, container, false);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
            numest = getArguments().getInt("numest",0);
            regestacion = getArguments().getInt("regestacion",0);
            exist_lamp = getArguments().getInt("existLamp",0);
        }
        totnum = 9;
        num_index = 0;
        agrega = new String[2];
        manager = new Utilidades(getContext());
        Utilidades.listaEdit = new ArrayList<>();
        CargarDatosExistentes();
        tl = (TableLayout)ll.findViewById(R.id.tl_lamparas);
        btn_siguiente = (CardView)ll.findViewById(R.id.btn_siguiente);
        btn_siguiente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CargarValores();
                if(Listado_lamp.size()>0){
                    AlmacenarLamparas();
                    FragmentManager manager = getFragmentManager();

                    registroimg = new BS_RegistroImg();
                    Bundle bundle = new Bundle();
                    bundle.putInt("codvxa",codvxa);
                    bundle.putInt("codvis",codvis);
                    bundle.putInt("idreturn",idreturn);
                    bundle.putInt("numest",numest);
                    bundle.putInt("regestacion",regestacion);
                    bundle.putInt("existLamp",exist_lamp);
                    registroimg.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.contenedorap, registroimg).commit();



                }else{
                    Toast.makeText(getContext(), "No ha ingresado valores en lamparas", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_volver = (CardView)ll.findViewById(R.id.btn_volver);
        tblamp = new TablaDinamicalmp(tl,getContext());
        tblamp.addHeader(header);
        tblamp.backgroundHeader(Color.parseColor("#F26524"));
        tblamp.backgroundData(Color.WHITE,Color.GRAY);
        while(num_index<=totnum){
            agrega[0] = (num_index + 1)+ "";
            if(Listado_lamp!=null)
                agrega[1] = (Listado_lamp.get(num_index).getCl_pob()!=0)?""+Listado_lamp.get(num_index).getCl_pob():"0";
            else
                agrega[1] = "0";
            tblamp.addItem(agrega);
            num_index++;
        }

        btn_volver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                volverAtras35();
            }
        });

        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
        ll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtras35();
                    return true;
                }
                return false;
            }
        });



        return ll;

    }


    private void AlmacenarLamparas(){
        control = Utilidades.serv_activo;
        control.setList_lampara(Listado_lamp);
        control.setReglampara(1);
        Utilidades.serv_activo = control;
        manager.EliminarLampara(codvxa);
        manager.insertar_listlampara(Listado_lamp, codvxa);
    }




    private void volverAtras35() {
        CargarValores();
        AlmacenarLamparas();
        FragmentManager manager = getFragmentManager();
        if(regestacion != 0){
            registro3 = new BS_ARegistro3();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa", codvxa);
            bundle.putInt("codvis", codvis);
            bundle.putInt("idreturn", idreturn);
            bundle.putInt("regestacion",regestacion);
            registro3.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro3).commit();
        }else{
            registro2 = new BS_ARegistro2();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa", codvxa);
            bundle.putInt("codvis", codvis);
            bundle.putInt("idreturn", idreturn);
            registro2.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro2).commit();
        }


    }



    private void CargarValores(){
        listaEdit = (ArrayList<EditText>) Utilidades.listaEdit;
        int i=0;
        int valid = 0;
        EditText newS;
        Listado_lamp = new ArrayList<>();
        while(i<listaEdit.size()){
            newS  = (EditText)listaEdit.get(i);
            valid = Integer.parseInt(newS.getText().toString());
            objlamp = new cs_lampara(i+1,valid);
            Listado_lamp.add(objlamp);
            i++;
        }


    }

    public void CargarDatosExistentes() {
        if(Utilidades.serv_activo.getList_lampara()!=null){
            Listado_lamp = new ArrayList<cs_lampara>();
            Listado_lamp = Utilidades.serv_activo.getList_lampara();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}