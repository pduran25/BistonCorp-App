package com.rubik.rubikinteractive.bistonapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

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

    String[] desc_img;
    Bitmap[] reg_img;
    String val_desc;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    int image;

    // Launcher clásico para elegir archivos/fotos
    private ActivityResultLauncher<Intent> getContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }

                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null && imgReg != null) {
                            try {
                                bitmap_reg = decodeSampledBitmapFromUri(requireContext(), uri, 600, 800);
                                bitmap_reg = redimensionarImagen(bitmap_reg, 600, 800);
                                imgReg.setImageBitmap(bitmap_reg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Registra Imágenes");
        View v = inflater.inflate(R.layout.fragment_apregistroimg, container, false);

        if(getArguments()!= null){
            codvxa =  getArguments().getInt("codvxa",0);
            codvis =  getArguments().getInt("codvis",0);
            idreturn = getArguments().getInt("idreturn",0);
            numest = getArguments().getInt("numest",0);
            regestacion = getArguments().getInt("regestacion",0);
            exist_lamp = getArguments().getInt("existLamp",0);
        }

        lista = v.findViewById(R.id.limagen);
        txtObs = v.findViewById(R.id.txtObs);
        btnExaminar = v.findViewById(R.id.btnExaminar);
        btnAgrega = v.findViewById(R.id.btnAgregar);
        imgReg = v.findViewById(R.id.imgRegistro);
        btnSiguiente = v.findViewById(R.id.btnSiguiente);
        btnVolver = v.findViewById(R.id.btnVolver);
        manager = new Utilidades(requireContext());

        btnExaminar.setOnClickListener(view -> mostrarDialogoOpciones());

        btnSiguiente.setOnClickListener(view -> {
            AlmacenarImagenes();
            FragmentManager manager = getFragmentManager();
            registro4 = new BS_ARegistro4();
            Bundle bundle = new Bundle();
            bundle.putInt("codvxa", codvxa);
            bundle.putInt("codvis", codvis);
            bundle.putInt("idreturn", idreturn);
            bundle.putInt("numest", numest);
            bundle.putInt("regestacion", regestacion);
            bundle.putInt("existLamp", exist_lamp);
            registro4.setArguments(bundle);
            if (manager != null) {
                manager.beginTransaction().replace(R.id.contenedorap, registro4).commit();
            }
        });

        band_add = 0;
        inicializarListas();

        btnVolver.setOnClickListener(view -> volverAtrasI());

        btnAgrega.setOnClickListener(view -> {
            if ((band_add == 0) && (bitmap_reg != null) && (!txtObs.getText().toString().isEmpty())) {
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
                while (num_index < List_idimg.size()) {
                    idimagen = List_idimg.get(num_index);
                    reg_img[num_index] = StringToBitMap(idimagen.getImagen());
                    desc_img[num_index] = idimagen.getDescripcion();
                    num_index++;
                }

                Utilidades.serv_activo.setList_regimagen(List_idimg);

                initControls();
            }
        });

        Cargar_Listado();

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener((vw, keyCode, event) -> {
            Log.i(TAG_FRAGMENT, "keyCode: " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                volverAtrasI();
                return true;
            }
            return false;
        });

        return v;
    }

    private void inicializarListas(){
        desc_img = new String[maxfoto];
        reg_img = new Bitmap[maxfoto];
        List_regimg = new ArrayList<>();
        List_idimg = new ArrayList<>();
    }

    private void volverAtrasI() {
        AlmacenarImagenes();

        FragmentManager manager = getFragmentManager();
        registro2 = new BS_ARegistro2();
        Bundle bundle = new Bundle();
        bundle.putInt("codvxa", codvxa);
        bundle.putInt("codvis", codvis);
        bundle.putInt("idreturn", idreturn);
        registro2.setArguments(bundle);
        if (manager != null) {
            manager.beginTransaction().replace(R.id.contenedorap, registro2).commit();
        }
    }

    private void AlmacenarImagenes(){
        control = Utilidades.serv_activo;
        if(List_idimg.size() != 0){
            control.setList_regimagen(List_idimg);
            Utilidades.serv_activo = control;
            manager.EliminarImagenxIdvisxapli(codvxa);
            manager.insertar_listimagen(List_idimg, codvxa);
            Utilidades.ListImagen = List_regimg;
        }
    }

    // Aquí está lo clave: este intent SÍ muestra el cuadro para elegir la app
    private void mostrarDialogoOpciones() {
        final CharSequence[] opciones = {"Elegir de Archivos/Fotos", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Elige una Opción:");
        builder.setItems(opciones, (dialog, i) -> {
            if (opciones[i].equals("Elegir de Archivos/Fotos")) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                Intent chooser = Intent.createChooser(intent, "Selecciona una app");
                getContent.launch(chooser);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int reqWidth, int reqHeight) throws FileNotFoundException {
        Bitmap bitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream iStream = context.getContentResolver().openInputStream(imageUri);

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
            iStream = context.getContentResolver().openInputStream(imageUri);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

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
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap_reg, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap_reg.getWidth();
        int alto = bitmap_reg.getHeight();

        if(ancho > anchoNuevo || alto > altoNuevo){
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix =  new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);
            return Bitmap.createBitmap(bitmap_reg, 0, 0, ancho, alto, matrix, false);
        } else{
            return bitmap_reg;
        }
    }

    private void Cargar_Listado(){
        List_regimg = new ArrayList<>();
        List_idimg = new ArrayList<>();
        if (Utilidades.serv_activo.getList_regimagen() != null){
            List_idimg = Utilidades.serv_activo.getList_regimagen();
            desc_img = new String[List_idimg.size()];
            reg_img = new Bitmap[List_idimg.size()];
            num_index = 0;
            while (num_index < List_idimg.size()) {
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
            return BitmapFactory.decodeStream(inputStream);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void initControls(){
        imgReg.setImageResource(R.drawable.aspercion);
        txtObs.setText("");
        BS_ADRegImg base = new BS_ADRegImg(getFragmentManager(), getActivity(), desc_img, reg_img, List_regimg);
        lista.setAdapter(base);
        bitmap_reg = null;
        band_add = 0;
    }

}
