package com.rubik.rubikinteractive.bistonapp;


import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;


public class BS_APrincipal extends Fragment {

    CardView btn_proxvisita;
    CardView btn_visitarealizada;
    CardView btn_mensaje;
    CardView btn_reagenda;
    TextView txt_nombre;
    Utilidades manager;
    EasySlider easySlider;
    List<SliderItem> easySliders;
    int mCurCheckPosition;
    ViewPager viewPager;
    boolean flag;

    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this frgment
        getActivity().setTitle("Bistoncorp - Aplicador");
        View v = inflater.inflate(R.layout.fragment_aprincipal, container, false);
        btn_proxvisita = (CardView)v.findViewById(R.id.btn_visitaap);
        btn_visitarealizada = (CardView)v.findViewById(R.id.btn_visitarealizada);
        txt_nombre = (TextView)v.findViewById(R.id.txtAplicador);
        btn_mensaje = (CardView)v.findViewById(R.id.btn_mensaje);
        viewPager = (ViewPager)v.findViewById(R.id.viewId2);
        viewPagerAdapter vpa = new viewPagerAdapter(getContext());
        viewPager.setAdapter(vpa);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),3000,5000);

        flag = true;

        Cargar_datosprincipal();

        btn_proxvisita.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.contenedorap,new BS_AProximaVisita()).commit();
            }
        });

        btn_visitarealizada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                FragmentManager manager = getFragmentManager();
                BS_AVisitada visitada = new BS_AVisitada();
                Bundle bundle = new Bundle();
                bundle.putInt("volver",2);
                visitada.setArguments(bundle);
                //openFragment(visitada, "visitada");
                manager.beginTransaction().replace(R.id.contenedorap,visitada).commit();
            }
        });

        btn_mensaje.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                BS_FMensajeria apmsj = new BS_FMensajeria();
                FragmentManager manager = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt("volver",2);
                apmsj.setArguments(bundle);
                manager.beginTransaction().replace(R.id.contenedorap,apmsj).commit();
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
        FragmentManager fragmentManager = getFragmentManager();
        BS_APrincipal principal = new BS_APrincipal();
        fragmentManager.beginTransaction().replace(R.id.contenedorap,principal).commit();
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if(flag){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                            if(viewPager.getCurrentItem()==0){
                                viewPager.setCurrentItem(1);

                            }else if(viewPager.getCurrentItem()==1){
                                viewPager.setCurrentItem(2);
                            }else{
                                viewPager.setCurrentItem(0);
                            }
                        }


                });
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        flag = false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        flag = false;
        super.onDestroy();
    }

    public void Cargar_sliders(){
        easySliders = new ArrayList<>();
        easySliders.add(new SliderItem("Slider 1",R.drawable.slider1));
        easySliders.add(new SliderItem("Slider 2",R.drawable.slider2));
        easySliders.add(new SliderItem("Slider 3",R.drawable.slider1));
        easySlider.setPages(easySliders);

    }



    public void Cargar_datosprincipal() {
        manager = new Utilidades(getContext());
        Cursor cursor = manager.CargarDatosUsuario();
        int pos_org = 0;
        int pos_est = 0;
        int pos_nom = 0;
        int pos_ape = 0;
        String org = "";
        String est = "";
        String nom = "";
        String ape = "";

        pos_org = cursor.getColumnIndex(manager.US_NOMORG);
        pos_est = cursor.getColumnIndex(manager.US_EST);
        pos_nom = cursor.getColumnIndex(manager.US_NOM);
        pos_ape = cursor.getColumnIndex(manager.US_APE);


        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                org = cursor.getString(pos_org);
                est = cursor.getString(pos_est);
                nom = cursor.getString(pos_nom);
                ape = cursor.getString(pos_ape);
            }
        }

        getActivity().setTitle("Bistoncorp - Aplicador");
        txt_nombre.setText("Bienvenido " + nom +" " + ape);

    }
}
