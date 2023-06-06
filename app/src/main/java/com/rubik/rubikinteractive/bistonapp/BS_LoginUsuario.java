package com.rubik.rubikinteractive.bistonapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;

import org.json.JSONException;
import org.json.JSONObject;
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_usuario;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_token;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import java.util.HashMap;
import java.util.Map;


public class BS_LoginUsuario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    CardView btn_login;

    // Internet detector
    AlertDialogManager alert = new AlertDialogManager();
    ConnectionDetector cd;
    EditText txt_usuario;
    EditText txt_contra;// Register button
    TextView txt_info;
    Button btnIniciar;
    ProgressDialog prgDialog, prgDialog2;
    private Utilidades manager;
    private cl_usuario usu;
    cs_token val_token;
    StringRequest strRequest;

    String usuario = "";
    String contra = "";


    RequestQueue request, request2;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs__login_usuario);


        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(BS_LoginUsuario.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        txt_usuario = (EditText) findViewById(R.id.txt_usuarioa);
        txt_contra = (EditText) findViewById(R.id.txt_clavea);
        txt_info = (TextView) findViewById(R.id.txt_nuevousuario);


        btn_login = (CardView) findViewById(R.id.btn_volve);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Buscando Usuario en BistonCorp Server, Por Favor espere...");
        prgDialog.setCancelable(false);

        btn_login = (CardView) findViewById(R.id.btn_volve);
        request = Volley.newRequestQueue(getApplicationContext());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CargarWebService();

            }

        });

        txt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                solicitarInformacion();

            }

        });





    }

    private void volverAtras() {

        Intent intent = new Intent(BS_LoginUsuario.this, BS_seltipousuario.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(KeyEvent.KEYCODE_BACK,event);
            volverAtras();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void CargarWebService(){

        usuario = txt_usuario.getText().toString();
        contra = txt_contra.getText().toString();

        if(usuario.trim().length() > 0 && contra.trim().length() > 0) {
            prgDialog.show();
            String url = "https://app.bistoncorp.com/php/connect_app/db_getUser.php?usuario="+usuario+"&contra="+contra;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, null);
            request.add(jsonObjectRequest);
        }else{
            Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


    private void solicitarInformacion(){
        final CharSequence[] opciones = {"ok"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Más Información");
        builder.setMessage("Para solicitar su Usuario y Clave, comuniquese con el Administrador del App Bistoncorp");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                // Do something with value!
            }
        });

        builder.show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {

            usu = new cl_usuario();
            prgDialog.hide();
            if(response.getInt("us_codigo") != 0){
                usu.setCodigo(response.getInt("us_codigo"));
                usu.setCedula(response.getString("us_cedula"));
                usu.setNombre(response.getString("us_nombre"));
                usu.setApellido(response.getString("us_apellido"));
                usu.setSexo(response.getString("us_sexo"));
                usu.setTelefono(response.getString("us_telefono"));
                usu.setCorreo(response.getString("us_correo"));
                usu.setDireccion(response.getString("us_direccion"));
                usu.setNomorg(response.getString("us_org"));
                usu.setOrganizacion(response.getInt("us_numorg"));
                usu.setUsuario(response.getString("us_usuario"));
                usu.setContra(response.getString("us_clave"));
                usu.setTipousuario(response.getInt("us_tipousu"));
                usu.setToken(Utilidades.token_local);
                Toast.makeText(getApplicationContext(), "Ha ingresado correctamente", Toast.LENGTH_SHORT).show();
                abrir_ingreso();
            }else{
                Toast.makeText(getApplicationContext(), "Error: Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void abrir_ingreso(){
        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Buscando Dispositivo de Registro");
        prgDialog2.setCancelable(false);
        request2 = Volley.newRequestQueue(getApplicationContext());
        Registra_nuevotoken();
        request2.add(strRequest);

        Intent i = new Intent(getApplicationContext(), BS_MenuCliente.class);
        manager = new Utilidades(this);
        manager.insertar_usuario(usu);
        startActivity(i);
        finish();
    }

    private void Registra_nuevotoken() {
        prgDialog2.show();
        val_token = new cs_token();
        val_token.setIdusuario(usu.getCodigo());
        val_token.setToken(usu.getToken());

        String url = "https://app.bistoncorp.com/php/connect_app/db_regtoken.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                prgDialog2.hide();
                if(response.equals("registrado")){
                    Toast.makeText(getApplicationContext(), "Nuevo usuario registrado al dispositivo exitosamente.", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        prgDialog2.hide();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String newDataArray = gson.toJson(val_token);
                params.put("regtoken", newDataArray);
                return params;
            }
        };
    }




    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        Toast.makeText(getApplicationContext(), "No se pudo conectar: "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }



}

