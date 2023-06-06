package com.rubik.rubikinteractive.bistonapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.FragmentManager;

import com.rubik.rubikinteractive.bistonapp.R;

import static java.lang.Integer.parseInt;

/**
 * Created by RubikInteractive on 2/6/19.
 */

public class BS_ADProxVisita extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [][] prox_cita;
    int [] prox_citaimg;
    FragmentManager frag;
    BS_FDetalleVisitaap sampleFragment;
    int codvxa,codvis = 0;

    //GregorianCalendar

    public BS_ADProxVisita(FragmentManager manager, Context contexto, String [][] datos, int [] image){
        this.contexto = contexto;
        this.prox_cita = datos;
        this.prox_citaimg = image;
        frag =manager;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    


    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.ele_proxvisita, null);
        TextView  orga = (TextView)vista.findViewById(R.id.txt_organizacion);
        TextView  esta = (TextView)vista.findViewById(R.id.txt_estab);
        TextView  fecha = (TextView)vista.findViewById(R.id.txt_fechavisita);
        TextView estatus = (TextView)vista.findViewById(R.id.txt_estatus);
        ImageView imgcita =  (ImageView) vista.findViewById(R.id.img_proxvisita);



        orga.setText("Organizacion: " + prox_cita[i][5]);
        esta.setText("Establecimiento: " + prox_cita[i][6]);
        fecha.setText("Fecha: " + prox_cita[i][2]);
        estatus.setText("Estado: " + prox_cita[i][7]);

        imgcita.setImageResource(prox_citaimg[1]);
        imgcita.setTag(i);




        vista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                codvxa = 0;
                codvis = 0;
                codvxa = parseInt(prox_cita[i][0]);
                codvis = parseInt(prox_cita[i][1]);
                sampleFragment = new BS_FDetalleVisitaap();
                Bundle bundle = new Bundle();
                bundle.putInt("codvxa",codvxa);
                bundle.putInt("codvis",codvis);
                bundle.putInt("idreturn",2);
                sampleFragment.setArguments(bundle);
                frag.beginTransaction().replace(R.id.contenedorap,sampleFragment).commit();
            }
        });

        return vista;
    }

    @Override
    public int getCount() {
        return prox_cita.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
