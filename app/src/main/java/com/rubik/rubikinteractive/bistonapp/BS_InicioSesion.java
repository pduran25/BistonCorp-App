package com.rubik.rubikinteractive.bistonapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rubik.rubikinteractive.bistonapp.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class BS_InicioSesion extends AppCompatActivity {

    private Utilidades manager;
    public static final String TAG = "NOTICIAS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs__inicio_sesion);
        manager = new Utilidades(this);
        //manager.EliminarUsuario();
        if(validaPermisos()){
            Continuar_trabajo();
        }

       // validaPermisos();

    }

    private void Continuar_trabajo() {
        Consultar_exiteUsuarioActivo();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }
                // Get new Instance ID token
                String token = task.getResult().getToken();
                Utilidades.token_local = token;
                // Log and toast
                //String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, "EL TOKEN ES:" + token);
                //Toast.makeText(BS_InicioSesion.this, token, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validaPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults.length == 2 && grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Continuar_trabajo();
            }else{
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual(){
        final CharSequence[] opciones = {"si","no"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea configurar los permisos de forma manual?");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(opciones[i].equals("si")){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cargarDialogoRecomendacion(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    private void Consultar_exiteUsuarioActivo() {
        Cursor cursor = manager.CargarDatosUsuario();
        if (cursor.getCount() > 0) {
            int tipous = 0;
            int pos_tus = cursor.getColumnIndex(manager.US_TUS);
            if (cursor.getCount() > 0) {
                //todos los datos del Usuario son conocidos desde este momento.
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    tipous = cursor.getInt(pos_tus);
                }
                Utilidades.continuo = 1;
            }
            if(tipous == 2 || tipous == 1) {
                //new Handler().postDelayed(new Runnable() {
                  //  @Override
                   // public void run() {
                        Intent intent = new Intent(BS_InicioSesion.this, BS_MenuCliente.class);
                        startActivity(intent);
                        finish();
                  //  }
               // }, 4000);
            }else if(tipous == 3){
               // new Handler().postDelayed(new Runnable() {
                //    @Override
                 //   public void run() {
                        Intent intent = new Intent(BS_InicioSesion.this, BS_MenuAplicador.class);
                        startActivity(intent);
                        finish();
                   //}
               // }, 4000);
            }
        }else{
            Utilidades.continuo = 0;
           // new Handler().postDelayed(new Runnable(){
              //  @Override
              //  public void run(){
                    Intent intent = new Intent(BS_InicioSesion.this, BS_seltipousuario.class);
                    startActivity(intent);
                    finish();
               // }
            //},4000);
        }
    }
}
