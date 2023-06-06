package com.rubik.rubikinteractive.bistonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.rubik.rubikinteractive.bistonapp.R;

import static java.lang.Integer.parseInt;

//import android.support.v4.app.FragmentManager;

/**
 * Created by RubikInteractive on 2/6/19.
 */

public class BS_ADReagenda extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [][] reagendas;
    int [] prox_citaimg;
    FragmentManager frag;
    BS_FDetalleVisitaap sampleFragment;
    int codvxa,codvis = 0;
    //GregorianCalendar

    public BS_ADReagenda(FragmentManager manager, Context contexto, String [][] datos, int [] image){
        this.contexto = contexto;
        this.reagendas = datos;
        this.prox_citaimg = image;
        frag =manager;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    public BS_ADReagenda(FragmentManager fragmentManager, FragmentActivity activity, String[][] reagendas, int[] prox_citaimg) {
    }


    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.ele_reagendada, null);
        TextView  tratamiento = (TextView)vista.findViewById(R.id.txt_tratamiento);
        TextView  fechaact = (TextView)vista.findViewById(R.id.txt_fechaact);
        TextView  fechanew = (TextView)vista.findViewById(R.id.txt_fechanew);
        TextView  estado = (TextView)vista.findViewById(R.id.txt_estado);

        ImageView imgcita =  (ImageView) vista.findViewById(R.id.img_proxvisita);



        tratamiento.setText("Tratamiento: " + reagendas[i][0]);
        fechaact.setText("Fecha Actual: " + reagendas[i][1]);
        fechanew.setText("Fecha Nueva: " + reagendas[i][2]);
        estado.setText("Estado: " + reagendas[i][3]);

        imgcita.setImageResource(prox_citaimg[1]);
        imgcita.setTag(i);

        return vista;
    }

    @Override
    public int getCount() {
        return reagendas.length;
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
