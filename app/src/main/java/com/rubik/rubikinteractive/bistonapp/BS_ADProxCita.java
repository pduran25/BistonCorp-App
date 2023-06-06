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
 **/

public class BS_ADProxCita  extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [][] prox_cita;
    int [] prox_citaimg;
    FragmentManager frag;
    BS_FDetalleVisita sampleFragment;
    int codigo = 0;
    //GregorianCalendar

    public BS_ADProxCita(FragmentManager manager,Context contexto, String [][] datos, int [] image){
        this.contexto = contexto;
        this.prox_cita = datos;
        this.prox_citaimg = image;
        frag =manager;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.ele_proxcita, null);
        TextView  tratamiento = (TextView)vista.findViewById(R.id.txt_tratamiento);
        TextView  fecha = (TextView)vista.findViewById(R.id.txt_fechacita);
        TextView  establecimiento = (TextView)vista.findViewById(R.id.txt_establecimiento);

        ImageView imgcita =  (ImageView) vista.findViewById(R.id.img_proxcita);



        tratamiento.setText("Tratamiento: " + prox_cita[i][1]);
        fecha.setText("Fecha Visita: " + prox_cita[i][2]);
        establecimiento.setText("Establecimiento: " + prox_cita[i][3]);

        imgcita.setImageResource(prox_citaimg[0]);
        imgcita.setTag(i);




        vista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                codigo = 0;
                codigo = parseInt(prox_cita[i][0]);
                sampleFragment = new BS_FDetalleVisita();
                Bundle bundle = new Bundle();
                bundle.putInt("codigo",codigo);
                bundle.putInt("idreturn",2);
                sampleFragment.setArguments(bundle);
                frag.popBackStack();
                frag.beginTransaction().replace(R.id.contenedor,sampleFragment).commit();


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
