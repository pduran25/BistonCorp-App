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



import static java.lang.Integer.parseInt;

/**
 * Created by RubikInteractive on 2/6/19.
 */

public class BS_ADClienteVisitada extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [][] visitada;
    int [] visitaimg;
    FragmentManager frag;
    BS_PresentaVisita sampleFragment;
    int idcontrol = 0;
    //GregorianCalendar

    public BS_ADClienteVisitada(FragmentManager manager, Context contexto, String [][] datos, int [] image){
        this.contexto = contexto;
        this.visitada = datos;
        this.visitaimg = image;
        frag =manager;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.ele_clientevisitada, null);
        TextView  tratamiento = (TextView)vista.findViewById(R.id.txt_tratamiento);
        TextView  fecha = (TextView)vista.findViewById(R.id.txt_fechavisitada);
        TextView  establecimiento = (TextView)vista.findViewById(R.id.txt_establecimiento);
        TextView  puntuacion = (TextView)vista.findViewById(R.id.txt_puntuacion);

        ImageView imgcita =  (ImageView) vista.findViewById(R.id.img_visitada);



        tratamiento.setText("Tratamiento realizado: " + visitada[i][2]);
        fecha.setText("Visita Realizada: " + visitada[i][1]);
        establecimiento.setText("Establecimiento: " + visitada[i][3]);
        if(Integer.parseInt(visitada[i][4])==0){
            puntuacion.setText("Puntuación: N/C");
        }else{
            puntuacion.setText("Puntuación: "+visitada[i][5]+"/5");
        }

        imgcita.setImageResource(visitaimg[0]);
        imgcita.setTag(i);

        vista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                idcontrol = 0;
                idcontrol = parseInt( visitada[i][0]);
                sampleFragment = new BS_PresentaVisita();
                Bundle bundle = new Bundle();
                bundle.putInt("idcontrol",idcontrol);
                bundle.putInt("return",1);
                sampleFragment.setArguments(bundle);
                frag.popBackStack();
                frag.beginTransaction().replace(R.id.contenedor,sampleFragment).commit();


            }
        });

        return vista;
    }

    @Override
    public int getCount() {
        return visitada.length;
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
