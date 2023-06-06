package com.rubik.rubikinteractive.bistonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_usuario;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_token;
import com.google.gson.Gson;
import com.rubik.rubikinteractive.bistonapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BS_MenuCliente extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Response.Listener<JSONObject>, Response.ErrorListener{

    RequestQueue request, request2;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog prgDialog, prgDialog2, prgDialog3;
    Utilidades manager;
    Menu menue;
    int idusu;
    Cursor cursor = null;
    cs_token val_token, val_token2;
    StringRequest strRequest;
    BS_FPrincipal principal;
    BS_FProximaCita proximaCita;
    BS_FMensajeria entrada;
    BS_FMsjsalida salida;
    BS_ClienteVisitada clientevisitada;
    BS_ListReagenda listReagenda;
    BS_FCalendario calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs__menu_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        manager = new Utilidades(this);
        if(Utilidades.continuo != 0) {
            prgDialog2 = new ProgressDialog(this);
            prgDialog2.setMessage("Buscando Dispositivo de Registro");
            prgDialog2.setCancelable(false);
            request2 = Volley.newRequestQueue(getApplicationContext());
            Revisar_token();
            request2.add(strRequest);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        principal = new BS_FPrincipal();
        fragmentManager.beginTransaction().replace(R.id.contenedor, principal).commit();

    }

    private void Revisar_token() {
        prgDialog2.show();
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
                prgDialog2.hide();
                if(response.equals("NO")){

                    manager.EliminarUsuario();
                    manager.EliminarImagen();
                    Intent intent = new Intent(BS_MenuCliente.this, BS_InicioSesion.class);
                    startActivity(intent);
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
        getMenuInflater().inflate(R.menu.bs__menu_cliente, menu);
        menue = menu;


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
        prgDialog.show();

        Cargar_menuestablecimientos();

        return true;
    }

    private void Cargar_menuestablecimientos(){

        cursor = manager.CargarDatosUsuario();
        int idorg = 0;
        idusu = 0;
        int idestab = 0;
        int tipousu = 0;
        int pos_org = cursor.getColumnIndex(manager.US_ORG);
        int pos_usu = cursor.getColumnIndex(manager.US_COD);
        int pos_act = cursor.getColumnIndex(manager.US_ACT);
        int pos_tipo =  cursor.getColumnIndex(manager.US_TUS);
        int pos_nomorg = cursor.getColumnIndex(manager.US_NOMORG);
        if (cursor.getCount() > 0) {
            //todos los datos del Usuario son conocidos desde este momento.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                idorg = cursor.getInt(pos_org);
                idusu = cursor.getInt(pos_usu);
                idestab = cursor.getInt(pos_act);
                tipousu = cursor.getInt(pos_tipo);
            }
        }

        String url = "";
        request = Volley.newRequestQueue(getApplicationContext());
        //if(tipousu == 1){
        url =  "https://app.bistoncorp.com/php/connect_app/db_getEstablecimientoxusuario.php?idusu="+idusu;
       /* }
        else if(tipousu == 2){
            url =  "https://app.bistoncorp.com/php/connect_app/db_getEstablecimientos.php?idorg="+idorg;
        }*/


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, this);
        jsonObjectRequest.setShouldCache(false);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        prgDialog.hide();
        Toast.makeText(getApplicationContext(), "No se pudo conectar: "+ error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        prgDialog.hide();
        int idestable = 0;
        String nomestab = "";

        try {
        cl_usuario usu = new cl_usuario();
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
        manager.Actualiza_usuario(usu);

        JSONArray json = response.optJSONArray("estab");

            for(int i=0; i<json.length(); i++){
                JSONObject jsonObject=null;
                jsonObject = json.getJSONObject(i);
                menue.add(i, jsonObject.optInt("es_codigo"), i, jsonObject.optString("es_nombre")).setShortcut('3', 'c');
                if(i==0) {
                    idestable = jsonObject.optInt("es_codigo");
                    nomestab = jsonObject.optString("es_nombre");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.Actualiza_establecimiento(idestable,nomestab,idusu);

        principal = new BS_FPrincipal();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor,  principal).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String nom = item.getTitle().toString();
        Toast.makeText(getApplicationContext(), "Establecimiento elegido: "+ nom, Toast.LENGTH_SHORT).show();

        principal = new BS_FPrincipal();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor,  principal).commit();

        manager.Actualiza_establecimiento(id,nom,idusu);
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_inicio) {
            // Handle the camera action

            if(principal == null){
                principal = new BS_FPrincipal();
            }
            fragmentManager.beginTransaction().replace(R.id.contenedor,  principal).commit();

        } else if (id == R.id.nav_proxcita) {

            if(proximaCita == null){
                proximaCita = new BS_FProximaCita();
            }
            principal = null;

            fragmentManager.beginTransaction().replace(R.id.contenedor, proximaCita).commit();
        } else if (id == R.id.nav_entrada) {
            if(entrada == null){
                entrada = new BS_FMensajeria();
                Bundle bundle = new Bundle();
                bundle.putInt("volver",1);
                entrada.setArguments(bundle);
            }

            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedor, entrada).commit();
        } else if (id == R.id.nav_salida) {
            if(salida == null){
                salida = new BS_FMsjsalida();
                Bundle bundle = new Bundle();
                bundle.putInt("volver",1);
                salida.setArguments(bundle);
            }

            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedor, salida).commit();
        }else if (id == R.id.nav_vrealizada) {
            if(clientevisitada == null){
                clientevisitada = new BS_ClienteVisitada();
            }

            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedor, new BS_ClienteVisitada()).commit();
        }
        else if (id == R.id.nav_reagenda) {
            if(listReagenda == null){
                listReagenda = new BS_ListReagenda();
            }

            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedor, listReagenda).commit();
        }else if (id == R.id.nav_calendario) {
            if(calendario == null){
                calendario = new BS_FCalendario();
            }

            principal = null;
            fragmentManager.beginTransaction().replace(R.id.contenedor, calendario).commit();
        }else if (id == R.id.nav_salir) {
            prgDialog3 = new ProgressDialog(this);
            prgDialog3.setMessage("Eliminando Usuario y Dispositivo");
            prgDialog3.setCancelable(false);

            request = Volley.newRequestQueue(getBaseContext());
            Elimina_token();
            request.add(strRequest);
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
        prgDialog3.show();
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
        strRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response)
            {
                prgDialog3.hide();
                if(response.equals("eliminado")){

                    manager.EliminarUsuario();
                    manager.EliminarImagen();
                    Intent intent = new Intent(BS_MenuCliente.this, BS_InicioSesion.class);
                    startActivity(intent);
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        prgDialog3.hide();
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
