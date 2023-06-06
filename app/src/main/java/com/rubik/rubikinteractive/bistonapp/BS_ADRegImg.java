package com.rubik.rubikinteractive.bistonapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_idimagen;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_regimagen;
import com.rubik.rubikinteractive.bistonapp.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by RubikInteractive on 2/6/19.
 */

public class BS_ADRegImg extends BaseAdapter{

    private static LayoutInflater inflater = null;
    Context contexto;
    String [] desc_img;
    Bitmap [] reg_img;
    ArrayList<cs_regimagen> List_regimg;
    ArrayList<cs_idimagen> List_idimg;
    FragmentManager frag;
    BS_FDetalleVisita sampleFragment;
    int codigo = 0;
    int num_index = 0;
    cs_regimagen regimagen;
    cs_idimagen idimagen;
    //GregorianCalendar

    public BS_ADRegImg(FragmentManager manager, Context contexto, String [] datos, Bitmap[] image, ArrayList<cs_regimagen> List_regimg){
        this.contexto = contexto;
        this.desc_img = datos;
        this.reg_img = image;
        frag =manager;
        this.List_regimg = List_regimg;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.ele_registroimg, null);
        TextView  txtdesc = (TextView)vista.findViewById(R.id.txtDesc);
        ImageView imgcita =  (ImageView) vista.findViewById(R.id.imgFoto);
        TextView txtBorrar = (TextView)vista.findViewById(R.id.txtBorrar);




        txtdesc.setText(desc_img[i]);
        imgcita.setImageBitmap(reg_img[i]);
        imgcita.setTag(i);

        txtBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utilidades.serv_activo.getList_regimagen()!=null){
                    List_idimg = Utilidades.serv_activo.getList_regimagen();

                    List_idimg.remove(List_idimg.get(i));
                    desc_img = new String[List_idimg.size()];
                    reg_img = new Bitmap[List_idimg.size()];
                    num_index = 0;
                    while(num_index<List_idimg.size()) {
                        idimagen = List_idimg.get(num_index);
                        reg_img[num_index] = StringToBitMap(idimagen.getImagen());
                        desc_img[num_index] = idimagen.getDescripcion();
                        num_index++;
                    }

                    Utilidades.serv_activo.setList_regimagen(List_idimg);
                }


                notifyDataSetChanged();
            }
        });

        return vista;
    }

    @Override
    public int getCount() {
        return desc_img.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }




}
