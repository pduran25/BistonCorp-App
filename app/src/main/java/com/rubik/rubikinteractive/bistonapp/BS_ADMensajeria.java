package com.rubik.rubikinteractive.bistonapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.R;

import static java.lang.Integer.parseInt;

/**
 * Created by RubikInteractive on 2/6/19.
 */

public class BS_ADMensajeria extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [][] mensajes;
    int [] imgmensaje;
    FragmentManager frag;
    Utilidades manager;
    BS_FDetalleMensaje sampleFragment;
    int codigo = 0;
    int tipousu = 0;
    View vista;

    public BS_ADMensajeria(FragmentManager manager, Context contexto, String [][] datos, int [] image, int tipousu){
        this.contexto = contexto;
        this.mensajes = datos;
        this.imgmensaje = image;
        this.tipousu = tipousu;
        frag =manager;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        vista = inflater.inflate(R.layout.ele_mensajeria, null);
        TextView  titulo = (TextView)vista.findViewById(R.id.txt_titulo);
        TextView  fecha = (TextView)vista.findViewById(R.id.txt_fechacorreo);
        TextView  texto = (TextView)vista.findViewById(R.id.txt_texto);

        ImageView imgcita =  (ImageView) vista.findViewById(R.id.img_correo);

        titulo.setText(mensajes[i][1]);
        fecha.setText(mensajes[i][2]);
        texto.setText(mensajes[i][3]);

        imgcita.setImageResource(imgmensaje[0]);
        imgcita.setTag(i);
        manager = new Utilidades(contexto);
        //Cargar_tipousu();

        vista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                codigo = 0;
                codigo = parseInt(mensajes[i][0]);
                sampleFragment = new BS_FDetalleMensaje();
                Bundle bundle = new Bundle();
                bundle.putInt("codigo",codigo);
                bundle.putInt("tipousu",tipousu);
                sampleFragment.setArguments(bundle);

                if(tipousu == 1){
                    FragmentTransaction transaction = frag.beginTransaction();
                    transaction.replace(R.id.contenedor,sampleFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    FragmentTransaction transaction = frag.beginTransaction();
                    transaction.replace(R.id.contenedorap,sampleFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });

        return vista;
    }

    private void Cargar_tipousu() {
        Cursor cursor = manager.CargarDatosUsuario();
        int pos_tus = cursor.getColumnIndex(manager.US_TUS);
        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                tipousu = cursor.getInt(pos_tus);
            }
        }
    }

    @Override
    public int getCount() {
        return mensajes.length;
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
