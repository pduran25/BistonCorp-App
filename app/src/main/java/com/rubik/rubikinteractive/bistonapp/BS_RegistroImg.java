package com.rubik.rubikinteractive.bistonapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_idimagen;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_regimagen;
import com.rubik.rubikinteractive.bistonapp.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class BS_RegistroImg extends Fragment {

    ListView lista;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog;
    ArrayList<cs_regimagen> List_regimg;
    ArrayList<cs_idimagen> List_idimg;
    int num_index = 0;
    cs_regimagen regimagen;
    cs_idimagen idimagen;
    TextView txtObs;
    Button btnExaminar;
    CardView btnAgrega, btnSiguiente, btnVolver;
    ImageView imgReg;
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";
    private static final String CARPETA_IMAGEN = "imagenes/";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;
    File fileimagen;
    Bitmap bitmap;
    Bitmap bitmap_reg;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private int maxfoto = 5;
    private int codvxa, codvis, idreturn, numest, regestacion;
    ControlServicio control;
    BS_ARegistro4 registro4;
    BS_ARegistro3 registro3;
    BS_ARegistro2 registro2;
    BS_ARegistro3_5 registro35;
    int band_add;
    Utilidades manager;
    int exist_lamp = 0;

    String [] desc_img;

    Bitmap [] reg_img;

    String val_desc;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    int image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Registra Imágenes");
        View v = inflater.inflate(R.layout.fragment_apregistroimg, container, false);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
            numest = getArguments().getInt("numest",0);
            regestacion = getArguments().getInt("regestacion",0);
            exist_lamp = getArguments().getInt("existLamp",0);

        }

        lista = (ListView)v.findViewById(R.id.limagen);
        txtObs = (TextView) v.findViewById(R.id.txtObs);
        btnExaminar = (Button) v.findViewById(R.id.btnExaminar);
        btnAgrega = (CardView) v.findViewById(R.id.btnAgregar);
        imgReg = (ImageView) v.findViewById(R.id.imgRegistro);
        btnSiguiente = (CardView) v.findViewById(R.id.btnSiguiente);
        btnVolver = (CardView) v.findViewById(R.id.btnVolver);
        manager = new Utilidades(getContext());

        btnExaminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoOpciones();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlmacenarImagenes();
                FragmentManager manager = getFragmentManager();
                registro4 = new BS_ARegistro4();
                Bundle bundle = new Bundle();
                bundle.putInt("codvxa",codvxa);
                bundle.putInt("codvis",codvis);
                bundle.putInt("idreturn",idreturn);
                bundle.putInt("numest",numest);
                bundle.putInt("regestacion",regestacion);
                bundle.putInt("existLamp",exist_lamp);
                registro4.setArguments(bundle);
                manager.beginTransaction().replace(R.id.contenedorap, registro4).commit();
            }
        });

        band_add = 0;
        inicializarListas();
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtrasI();
            }
        });

        btnAgrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((band_add == 0) && (bitmap_reg != null) && (txtObs.getText().toString()!= "")){
                    band_add = 1;
                    val_desc = txtObs.getText().toString();

                    regimagen = new cs_regimagen();
                    regimagen.setIdimagen(bitmap_reg);
                    regimagen.setDescripcion(val_desc);
                    List_regimg.add(regimagen);

                    idimagen = new cs_idimagen();
                    idimagen.setImagen(regimagen.getImagen());
                    idimagen.setDescripcion(regimagen.getDescripcion());
                    List_idimg.add(idimagen);

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

                    initControls();
                }



            }
        });


        Cargar_Listado();

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    volverAtrasI();
                    return true;
                }
                return false;
            }
        });

        return v;

    }


    private void inicializarListas(){
        desc_img = new String[maxfoto];
        reg_img = new Bitmap[maxfoto];
    }

    private void volverAtrasI() {

        AlmacenarImagenes();

        /*if(exist_lamp == 1) {
            FragmentManager manager = getFragmentManager();
            registro35 = new BS_ARegistro3_5();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa",codvxa);
            bundle.putInt("codvis",codvis);
            bundle.putInt("idreturn",idreturn);
            bundle.putInt("numest",numest);
            bundle.putInt("regestacion",regestacion);
            registro35.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro35).commit();
        }else if(regestacion == 1){
            FragmentManager manager = getFragmentManager();
            registro3 = new BS_ARegistro3();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa",codvxa);
            bundle.putInt("codvis",codvis);
            bundle.putInt("idreturn",idreturn);
            registro3.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro3).commit();
        }else{*/
            FragmentManager manager = getFragmentManager();
            registro2 = new BS_ARegistro2();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa",codvxa);
            bundle.putInt("codvis",codvis);
            bundle.putInt("idreturn",idreturn);
            registro2.setArguments(bundle);
            manager.beginTransaction().replace(R.id.contenedorap, registro2).commit();
        //}

    }

    private void AlmacenarImagenes(){
        control = Utilidades.serv_activo;
        if(List_idimg.size()!=0){
            control.setList_regimagen(List_idimg);
            Utilidades.serv_activo = control;
            manager.EliminarImagenxIdvisxapli(codvxa);
            manager.insertar_listimagen(List_idimg, codvxa);
            Utilidades.ListImagen = List_regimg;
        }

    }

    private void mostrarDialogoOpciones(){
        final CharSequence[] opciones = {"Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una Opción:");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                    if(opciones[i].equals("Elegir de Galeria")){
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"),COD_SELECCIONA);
                    }else{
                        dialog.dismiss();
                    }

            }
        });
        builder.show();

    }

    private void abrirCamara() {

        File myfile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        Boolean isCreada = myfile.exists();
        if(isCreada==false){
            myfile.mkdirs();
            isCreada = true;
        }
        if(isCreada==true){
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+".jpg";
            path = Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre;
            fileimagen = new File(path);

            Intent it =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            if(Build.VERSION.SDK_INT>=24){
              try{
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);

                }catch(Exception e){
                    e.printStackTrace();
               }
           }

            it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileimagen));

            startActivityForResult(it, COD_FOTO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bMapRotate = null;
        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                try {
                    //bitmap_reg = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), miPath);
                    bitmap_reg = decodeSampledBitmapFromUri(getContext(), miPath, 600, 800);
                } catch (IOException e) {
                    e.printStackTrace();

                }

                imgReg.setImageBitmap(bitmap_reg);
                //imgReg.setImageURI(miPath);
                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener(){

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("Path", ""+path);
                    }
                });





                bitmap_reg = decodeSampledBitmapFromFile(path, 600,800);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap_reg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                String encodedImage = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);


                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                    default:
                        break;
                }


                bMapRotate = Bitmap.createBitmap(bitmap_reg, 0, 0,bitmap_reg.getWidth(),bitmap_reg.getHeight(), matrix, true);
                imgReg.setImageBitmap(bMapRotate);
                bitmap_reg = bMapRotate;

                break;
        }

       bitmap_reg = redimensionarImagen(bitmap_reg, 600,800);
    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int reqWidth, int reqHeight) throws FileNotFoundException {
        Bitmap bitmap = null;
        try {
            // Get input stream of the image
            final BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream iStream = context.getContentResolver().openInputStream(imageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
            iStream = context.getContentResolver().openInputStream(imageUri);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap_reg, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap_reg.getWidth();
        int alto = bitmap_reg.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;

            Matrix matrix =  new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);
            return Bitmap.createBitmap(bitmap_reg, 0, 0, ancho, alto, matrix,false);

        }else{
            return bitmap_reg;
        }


    }

    private void Cargar_Listado(){
        List_regimg = new ArrayList<>();
        List_idimg = new ArrayList<>();
        if(Utilidades.serv_activo.getList_regimagen()!=null){

            List_idimg = Utilidades.serv_activo.getList_regimagen();


            desc_img = new String[List_idimg.size()];
            reg_img = new Bitmap[List_idimg.size()];
            num_index = 0;
            while(num_index<List_idimg.size()) {
                idimagen = List_idimg.get(num_index);
                reg_img[num_index] = StringToBitMap(idimagen.getImagen());
                desc_img[num_index] = idimagen.getDescripcion();
                num_index++;
            }

            initControls();
        }
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    public void onStart(){
        super.onStart();
    }

    private void initControls(){
        imgReg.setImageResource(R.drawable.aspercion);
        txtObs.setText("");
        BS_ADRegImg base = new BS_ADRegImg(getFragmentManager(),getActivity(),desc_img,reg_img, List_regimg);
        lista.setAdapter(base);
        bitmap_reg = null;
        band_add = 0;

    }




}
