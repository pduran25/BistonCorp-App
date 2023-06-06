package com.rubik.rubikinteractive.bistonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_token;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import java.util.HashMap;
import java.util.Map;


public class BS_MenuAplicador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    Utilidades manager;
    cs_token val_token, val_token2;
    StringRequest strRequest, strRequest2;
    RequestQueue request, request2;
    ProgressDialog prgDialog, prgDialog2;
    BS_APrincipal principal;
    BS_AProximaVisita proxvis;
    BS_AVisitada visitada;
    BS_FMensajeria mensajeria;
    BS_FMensajeria entrada;
    BS_FMsjsalida salida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs__menu_aplicador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarap);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        manager = new Utilidades(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        principal = new BS_APrincipal();
        fragmentManager.beginTransaction().replace(R.id.contenedorap,principal).commit();

        //openFragment(principal, "principal");

        if(Utilidades.continuo != 0){
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage("Buscando Dispositivo de Registro");
            prgDialog.setCancelable(false);
            request = Volley.newRequestQueue(getBaseContext());
            prgDialog.show();
            Revisar_token();
            request.add(strRequest);
        }


    }

    private void Revisar_token() {

        Cursor cursor = manager.CargarDatosUsuario();
        int idusuario = 0;
        String token = "";

        if (cursor.getCount() > 0) {
            int pos_id = cursor.getColumnIndex(manager.US_COD);
            int pos_token = cursor.getColumnIndex(manager.US_TOK);


            if (cursor.getCount() > 0) {
                //todos los datos del Usuario son conocidos desde este momento.
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    idusuario = cursor.getInt(pos_id);
                    token = cursor.getString(pos_token);
                }
            }
        }

        val_token = new cs_token();
        val_token.setIdusuario(idusuario);
        val_token.setToken(token);


        String url = "https://app.bistoncorp.com/php/connect_app/db_conftoken.php";
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                if(prgDialog.isShowing())
                     prgDialog.hide();
                prgDialog.dismiss();
                if(response.equals("NO")){

                    manager.EliminarUsuario();
                    manager.EliminarImagen();
                    Intent intent = new Intent(BS_MenuAplicador.this, BS_InicioSesion.class);
                    startActivity(intent);
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if(prgDialog.isShowing())
                            prgDialog.hide();
                        prgDialog.dismiss();
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
                params.put("settoken", newDataArray);
                return params;
            }
        };

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bs__menu_aplicador, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String nom = item.getTitle().toString();

        return super.onOptionsItemSelected(item);
    }


    private void openFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment != null) {
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.contenedorap);
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(existingFragment);
        }
        else {
            fragmentTransaction.add(R.id.contenedorap, fragment, tag);
        }
        fragmentTransaction.commit();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_inicioap) {
            // Handle the camera action
            if(principal == null){
               principal = new BS_APrincipal();
            }
           // openFragment(principal, "principal");

            fragmentManager.beginTransaction().replace(R.id.contenedorap,principal).commit();


        } else if (id == R.id.nav_proxcitaap) {
            if(proxvis == null){
                proxvis = new BS_AProximaVisita();
            }
            //openFragment(proxvis, "proxvis");
            fragmentManager.beginTransaction().replace(R.id.contenedorap,proxvis).commit();
        } else if (id == R.id.nav_citarealizada) {
            if(visitada == null){
                visitada = new BS_AVisitada();
            }
            //openFragment(visitada, "visitada");
            fragmentManager.beginTransaction().replace(R.id.contenedorap,visitada).commit();
        }else if (id == R.id.nav_salirap) {
            prgDialog2 = new ProgressDialog(this);
            prgDialog2.setMessage("Eliminando Usuario y Dispositivo");
            prgDialog2.setCancelable(false);

            request2 = Volley.newRequestQueue(getBaseContext());
            Elimina_token();
            request2.add(strRequest2);
        }else if (id == R.id.nav_entrada) {
            if(entrada == null){
                entrada = new BS_FMensajeria();
                Bundle bundle = new Bundle();
                bundle.putInt("volver",2);
                entrada.setArguments(bundle);
            }
            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedorap, entrada).commit();
        }else if (id == R.id.nav_salida) {
            if(salida == null){
                salida = new BS_FMsjsalida();
                Bundle bundle = new Bundle();
                bundle.putInt("volver",2);
                salida.setArguments(bundle);
            }

            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedorap, salida).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    private void Elimina_token() {
        prgDialog2.show();
        prgDialog2.dismiss();
        Cursor cursor = manager.CargarDatosUsuario();
        int idusuario = 0;
        String token = "";

        if (cursor.getCount() > 0) {
            int pos_id = cursor.getColumnIndex(manager.US_COD);
            int pos_token = cursor.getColumnIndex(manager.US_TOK);


            if (cursor.getCount() > 0) {
                //todos los datos del Usuario son conocidos desde este momento.
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    idusuario = cursor.getInt(pos_id);
                    token = cursor.getString(pos_token);
                }
            }
        }

        val_token2 = new cs_token();
        val_token2.setIdusuario(idusuario);
        val_token2.setToken(token);

        String url = "https://app.bistoncorp.com/php/connect_app/db_eliminatoken.php";
        strRequest2 = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                prgDialog2.hide();
                prgDialog2.dismiss();
                if(response.equals("eliminado")){

                    manager.EliminarUsuario();
                    manager.EliminarImagen();
                    Intent intent = new Intent(BS_MenuAplicador.this, BS_InicioSesion.class);
                    startActivity(intent);
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        prgDialog2.hide();
                        prgDialog2.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String newDataArray = gson.toJson(val_token2);
                params.put("deltoken", newDataArray);
                return params;
            }
        };

    }




}
