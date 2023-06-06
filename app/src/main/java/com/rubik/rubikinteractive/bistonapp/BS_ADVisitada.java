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

public class BS_ADVisitada extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [][] visitada;
    int [] visitaimg;
    FragmentManager frag;
    BS_PresentaVisita sampleFragment;
    int idcontrol = 0;
    //GregorianCalendar

    public BS_ADVisitada(FragmentManager manager, Context contexto, String [][] datos, int [] image){
        this.contexto = contexto;
        this.visitada = datos;
        this.visitaimg = image;
        frag =manager;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.ele_visitada, null);
        TextView  organizacion = (TextView)vista.findViewById(R.id.txt_organizacion);
        TextView  fecha = (TextView)vista.findViewById(R.id.txt_fechavisita);
        TextView  establecimiento = (TextView)vista.findViewById(R.id.txt_estab);

        ImageView imgcita =  (ImageView) vista.findViewById(R.id.img_visitada);
        TextView txtpend = (TextView)vista.findViewById(R.id.txt_valpend);



        organizacion.setText("Organización: " + visitada[i][6]);
        fecha.setText("Visita Realizada: " + visitada[i][3]);
        establecimiento.setText("Establecimiento: " + visitada[i][7]);

        if(visitada[i][8].equals("1")){
            txtpend.setText("SI");
        }else{
            txtpend.setText("NO");
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
                bundle.putInt("return",2);
                sampleFragment.setArguments(bundle);
                frag.beginTransaction().replace(R.id.contenedorap,sampleFragment).commit();

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
