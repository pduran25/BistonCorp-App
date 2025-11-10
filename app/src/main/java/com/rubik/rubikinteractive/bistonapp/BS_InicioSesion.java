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
import com.google.firebase.messaging.FirebaseMessaging;
import com.rubik.rubikinteractive.bistonapp.R;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.CAMERA;

public class BS_InicioSesion extends AppCompatActivity {

    private Utilidades manager;
    public static final String TAG = "NOTICIAS";

    // Array de permisos modernos (solo CAMERA; STORAGE no es válido en API 33+)
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            CAMERA
    };

    // Modern launcher de AndroidX para permisos
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = true;
                for (Boolean granted : result.values()) {
                    if (!granted) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    Continuar_trabajo();
                } else {
                    solicitarPermisosManual();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs__inicio_sesion);
        manager = new Utilidades(this);
        validarPermisosModernos();
    }

    private void validarPermisosModernos() {
        boolean allGranted = true;
        for (String perm : REQUIRED_PERMISSIONS) {
            int check = ContextCompat.checkSelfPermission(this, perm);
            Log.d(TAG, "Permiso " + perm + " check: " + check); // debug
            if (check != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            Log.d(TAG, "Todos los permisos concedidos");
            Continuar_trabajo();
        } else {
            Log.d(TAG, "Faltan permisos, lanzando solicitud");
            requestPermissionsLauncher.launch(REQUIRED_PERMISSIONS);
        }
    }

    private void Continuar_trabajo() {
        Consultar_exiteUsuarioActivo();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getToken failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Utilidades.token_local = token;
                        Log.d(TAG, "EL TOKEN ES:" + token);
                    }
                });
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

    private void Consultar_exiteUsuarioActivo() {
        Cursor cursor = manager.CargarDatosUsuario();
        if (cursor.getCount() > 0) {
            int tipous = 0;
            int pos_tus = cursor.getColumnIndex(manager.US_TUS);
            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    tipous = cursor.getInt(pos_tus);
                }
                Utilidades.continuo = 1;
            }
            if(tipous == 2 || tipous == 1) {
                Intent intent = new Intent(BS_InicioSesion.this, BS_MenuCliente.class);
                startActivity(intent);
                finish();
            }else if(tipous == 3){
                Intent intent = new Intent(BS_InicioSesion.this, BS_MenuAplicador.class);
                startActivity(intent);
                finish();
            }
        }else{
            Utilidades.continuo = 0;
            Intent intent = new Intent(BS_InicioSesion.this, BS_seltipousuario.class);
            startActivity(intent);
            finish();
        }
    }
}
