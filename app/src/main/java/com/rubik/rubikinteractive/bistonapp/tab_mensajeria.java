package com.rubik.rubikinteractive.bistonapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.PagerController;
import com.rubik.rubikinteractive.bistonapp.R;


public class tab_mensajeria extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tab1, tab2;
    FloatingActionButton fb_newmsj;
    int volver = 0;
    AppBarLayout appBar;
    PagerController adapter;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            volver =  getArguments().getInt("volver",0);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Listado de Mensajes");
        View v = inflater.inflate(R.layout.fragment_tabmensajeria, container, false);

        fb_newmsj = (FloatingActionButton) v.findViewById(R.id.fb_newmsj);
        viewPager =(ViewPager) v.findViewById(R.id.vpager);

        if(Utilidades.rotacion == 0){
            View parent = (View) container.getParent();

            if(appBar == null){
                appBar = (AppBarLayout) parent.findViewById(R.id.appCliente);
                tabLayout = new TabLayout(getActivity());
                tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
                appBar.addView(tabLayout);
                llenarviewPager(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                tabLayout.setupWithViewPager(viewPager);
            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            Utilidades.rotacion = 1;
        }



        fb_newmsj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BS_FEscribeMensaje escmsj = new BS_FEscribeMensaje();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.contenedor,escmsj).commit();
            }
        });

        return v;

    }

    private void llenarviewPager(ViewPager viewPager) {
        adapter = new PagerController(getFragmentManager());

        adapter.addFragment(new BS_FMensajeria(), "Entrada");

        adapter.addFragment(new BS_FMsjsalida(), "Salida");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(Utilidades.rotacion == 1){
            appBar.removeView(tabLayout);
            appBar = null;
            adapter.removeFragments();
            Utilidades.rotacion = 0;
        }
    }

}