package com.rubik.rubikinteractive.bistonapp.Conexion;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.rubik.rubikinteractive.bistonapp.Entidades.cs_lampara;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_tratamiento;
import com.rubik.rubikinteractive.bistonapp.BS_FMensajeria;
import com.rubik.rubikinteractive.bistonapp.BS_FMsjsalida;
import com.rubik.rubikinteractive.bistonapp.Entidades.ControlServicio;
import com.rubik.rubikinteractive.bistonapp.Entidades.ar_area;
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_usuario;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_consideracion;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_estacion;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_idimagen;
import com.rubik.rubikinteractive.bistonapp.Entidades.cs_regimagen;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RubikInteractive on 4/4/19.
 */

public class Utilidades {

    public static final String TABLA_USUARIO = "usuario";
    public static final String US_COD = "codigo";
    public static final String US_CED = "cedula";
    public static final String US_NOM = "nombre";
    public static final String US_APE = "apellido";
    public static final String US_SEX = "sexo";
    public static final String US_TEL = "telefono";
    public static final String US_COR = "correo";
    public static final String US_DIR = "direccion";
    public static final String US_NOMORG = "nomorg";
    public static final String US_ORG = "organizacion";
    public static final String US_USU = "usuario";
    public static final String US_CLA = "clave";
    public static final String US_TUS = "tipousuario";
    public static final String US_ACT = "estabactivo";
    public static final String US_EST = "establecimiento";
    public static final String US_TOK = "token";

    //TABLA DE CONTROL
    public static final String TABLA_CONTROL = "controlservicios";
    public static final String TC_IDVISXAPLI = "tc_idvisxapli";
    public static final String TC_FECHA = "tc_fecha";
    public static final String TC_IDVIS = "tc_id_visi";
    public static final String TC_HINI = "tc_horaini";
    public static final String TC_HFIN = "tc_horafin";
    public static final String TC_OBS = "tc_obs";
    public static final String TC_CHKPREV = "tc_chkprev";
    public static final String TC_CHKCORR = "tc_chkcorr";
    public static final String TC_FIRMA = "tc_firma";
    public static final String TC_IMGDISP = "tc_imgdisp";
    public static final String TC_CHKFOTO = "tc_chkfoto";
    public static final String TC_CHKHORA = "tc_chkhora";
    public static final String TC_ACCPREV = "tc_accprev";
    public static final String TC_ACCCORR = "tc_acccorr";
    public static final String TC_HORRI = "tc_horri";
    public static final String TC_NUMEST = "tc_numest";
    public static final String TC_NUMCONSI = "tc_numconsi";
    public static final String TC_AUTORIZADO = "tc_autorizado";
    public static final String TC_IDAUTO = "tc_idauto"; //
    public static final String TC_REGESTACION = "tc_regestacion";


    //TABLA DE CONSIDERACIONES
    public static final String TABLA_CONSIDERACION = "consideraciones";
    public static final String CS_IDCONSI = "cs_idconsi";
    public static final String CS_CONSIDERACION = "cs_consideracion";
    public static final String CS_IDVISXAPLI = "cs_idvisxapli";

    //TABLA DE TRATAMIENTO
    public static final String TABLA_TRATAMIENTO = "tratamientos";
    public static final String TR_IDTRAT = "tr_idtrat";
    public static final String TR_TRATAMIENTO = "tr_tratamiento";
    public static final String TR_IDVISXAPLI = "tr_idvisxapli";

    //TABLA DE AREAS
    public static final String TABLA_AREAS = "areas";
    public static final String AR_AREA= "ar_area";
    public static final String AR_NOMBAREA = "ar_nombarea";
    public static final String AR_PLAGA = "ar_plaga";
    public static final String AR_NOMBPLAGA = "ar_nombplaga";
    public static final String AR_POB = "ar_pob";
    public static final String AR_NOMBPOB = "ar_nombpob";
    public static final String AR_TRAT = "ar_trat";
    public static final String AR_NOMBTRAT = "ar_nombtrat";
    public static final String AR_IDVISXAPLI = "ar_idvisxapli";

    //TABLA DE ESTACIONES
    public static final String TABLA_ESTACION = "estaciones";
    public static final String ES_NUM = "es_num";
    public static final String ES_ESTADO = "es_estado";
    public static final String ES_IDVISXAPLI = "es_idvisxapli";

    //TABLA DE LAMPARA
    public static final String TABLA_LAMPARA = "lampara";
    public static final String LP_NUM = "lp_num";
    public static final String LP_POB = "lp_estado";
    public static final String LP_IDVISXAPLI = "lp_idvisxapli";



    public static final String TABLA_IMAGEN = "imagen";
    public static final String IMG_ID = "img_id";
    public static final String IMG_DESC =  "img_desc";
    public static final String IMG_IMG =  "img_img";
    public static final String IMG_IDVISXAPLI = "img_idvisxapli";

    public static ControlServicio serv_activo = null;
    public static ArrayAdapter AdpEstados = null;
    public static List<Spinner> listaSpinner = null;
    public static List<EditText> listaEdit = null;
    public static ArrayList<cs_regimagen> ListImagen = null;
    public static String token_local = "";
    public static int continuo = 0;
    public static int rotacion = 0;
    public static int contmsn = 0;

    public static int act_slider = 0;
    public static BS_FMensajeria msn1;
    public static BS_FMsjsalida msn2;

    public static final String CREAR_TABLA_USUARIO ="CREATE TABLE "+TABLA_USUARIO+" (" +
            US_COD + " INTEGER, " +
            US_CED + " TEXT, " +
            US_NOM + " TEXT, " +
            US_APE + " TEXT, " +
            US_SEX + " TEXT, " +
            US_TEL + " TEXT, " +
            US_COR + " TEXT, " +
            US_DIR + " TEXT, " +
            US_NOMORG + " TEXT, " +
            US_ORG + " INTEGER, " +
            US_USU + " TEXT, " +
            US_CLA + " TEXT, " +
            US_TUS + " INTEGER, " +
            US_ACT + " INTEGER, " +
            US_EST + " TEXT, " +
            US_TOK + " TEXT)";


    public static final String CREAR_TABLA_IMAGEN = "CREATE TABLE "+TABLA_IMAGEN+" (" +
            IMG_ID + " INTEGER, " +
            IMG_DESC + " TEXT, " +
            IMG_IMG + " BLOB)";

    public static final String CREAR_TABLA_CONTROL ="CREATE TABLE "+TABLA_CONTROL+" (" +
            TC_IDVISXAPLI + " INTEGER, " +
            TC_FECHA + " TEXT, " +
            TC_IDVIS + " INTEGER, " +
            TC_HINI + " TEXT, " +
            TC_HFIN + " TEXT, " +
            TC_OBS + " TEXT, " +
            TC_CHKPREV + " INTEGER, " +
            TC_CHKCORR + " INTEGER, " +
            TC_FIRMA + " TEXT, " +
            TC_IMGDISP + " INTEGER, " +
            TC_CHKFOTO + " INTEGER, " +
            TC_CHKHORA + " INTEGER, " +
            TC_ACCPREV + " TEXT, " +
            TC_ACCCORR + " TEXT, " +
            TC_HORRI + " TEXT, "+
            TC_NUMEST + " INTEGER, " +
            TC_NUMCONSI + " INTEGER, " +
            TC_AUTORIZADO + " TEXT, " +
            TC_IDAUTO + " TEXT, "+
            TC_REGESTACION + " INTEGER)";

    public static final String CREAR_TABLA_CONSIDERACION = "CREATE TABLE "+TABLA_CONSIDERACION+" (" +
            CS_IDCONSI + " INTEGER, " +
            CS_CONSIDERACION + " TEXT, " +
            CS_IDVISXAPLI + " INTEGER)";

    public static final String CREAR_TABLA_TRATAMIENTO = "CREATE TABLE "+TABLA_TRATAMIENTO+" (" +
            TR_IDTRAT + " INTEGER, " +
            TR_TRATAMIENTO + " TEXT, " +
            TR_IDVISXAPLI + " INTEGER)";


    public static final String CREAR_TABLA_AREAS = "CREATE TABLE "+TABLA_AREAS+" (" +
            AR_AREA + " INTEGER, " +
            AR_NOMBAREA + " TEXT, " +
            AR_PLAGA + " INTEGER, " +
            AR_NOMBPLAGA + " TEXT, " +
            AR_POB + " INTEGER, " +
            AR_NOMBPOB + " TEXT, " +
            AR_TRAT + " INTEGER, " +
            AR_NOMBTRAT + " TEXT, " +
            AR_IDVISXAPLI + " INTEGER)";

    public static final String CREAR_TABLA_ESTACION = "CREATE TABLE "+TABLA_ESTACION+" (" +
            ES_NUM + " INTEGER, " +
            ES_ESTADO + " INTEGER, " +
            ES_IDVISXAPLI + " INTEGER)";

    public static final String CREAR_TABLA_LAMPARA = "CREATE TABLE "+TABLA_LAMPARA+" (" +
            LP_NUM + " INTEGER, " +
            LP_POB + " INTEGER, " +
            LP_IDVISXAPLI + " INTEGER)";




    private ContentValues genera_usuario(int codigo, String cedula, String nombre, String apellido, String sexo, String telefono, String correo, String direccion, String nomorg, int organizacion, String usuario, String clave, int tipousu, int activo, String establecimiento, String token) {
        ContentValues valores = new ContentValues();
        valores.put(US_COD, codigo);
        valores.put(US_CED, cedula);
        valores.put(US_NOM, nombre);
        valores.put(US_APE, apellido);
        valores.put(US_SEX, sexo);
        valores.put(US_TEL, telefono);
        valores.put(US_COR, correo);
        valores.put(US_DIR, direccion);
        valores.put(US_NOMORG, nomorg);
        valores.put(US_ORG, organizacion);
        valores.put(US_USU, usuario);
        valores.put(US_CLA, clave);
        valores.put(US_TUS, tipousu);
        valores.put(US_ACT, activo);
        valores.put(US_EST, establecimiento);
        valores.put(US_TOK, token);
        return valores;
    }


    private ContentValues genera_consideracion(int idconsi, String consideracion, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(CS_IDCONSI, idconsi);
        valores.put(CS_CONSIDERACION, consideracion);
        valores.put(CS_IDVISXAPLI, idvisxapli);
        return valores;
    }



    private ContentValues genera_tratamiento(int idtrat, String tratamiento, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(TR_IDTRAT, idtrat);
        valores.put(TR_TRATAMIENTO, tratamiento);
        valores.put(TR_IDVISXAPLI, idvisxapli);
        return valores;
    }

    private ContentValues genera_areas(int area, String nombarea, int plaga, String nombplaga, int poblacion, String nombpob, int trata, String nombtrata, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(AR_AREA, area);
        valores.put(AR_NOMBAREA, nombarea);
        valores.put(AR_PLAGA, plaga);
        valores.put(AR_NOMBPLAGA, nombplaga);
        valores.put(AR_POB, poblacion);
        valores.put(AR_NOMBPOB, nombpob);
        valores.put(AR_TRAT, trata);
        valores.put(AR_NOMBTRAT, nombtrata);
        valores.put(AR_IDVISXAPLI, idvisxapli);
        return valores;
    }

    private ContentValues genera_estaciones(int num, int estado, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(ES_NUM, num);
        valores.put(ES_ESTADO, estado);
        valores.put(ES_IDVISXAPLI, idvisxapli);
        return valores;
    }

    private ContentValues genera_lampara(int num, int pob, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(LP_NUM, num);
        valores.put(LP_POB, pob);
        valores.put(LP_IDVISXAPLI, idvisxapli);
        return valores;
    }

    private ContentValues genera_control(int id_visxapli, String fecha, int idvisita, String hora_ini, String hora_fin, String obs, int chkprev, int chkcorr, String firma, int imgdisp, int chkfoto, int chkhora, String accprev, String acccorr, String horri, int numest, int numconsi, String autorizado, String idauto, int regestacion) {
        ContentValues valores = new ContentValues();
        valores.put(TC_IDVISXAPLI, id_visxapli);
        valores.put(TC_FECHA, fecha);
        valores.put(TC_IDVIS, idvisita);
        valores.put(TC_HINI, hora_ini);
        valores.put(TC_HFIN, hora_fin);
        valores.put(TC_OBS, obs);
        valores.put(TC_CHKPREV, chkprev);
        valores.put(TC_CHKCORR, chkcorr);
        valores.put(TC_FIRMA, firma);
        valores.put(TC_IMGDISP, imgdisp);
        valores.put(TC_CHKFOTO, chkfoto);
        valores.put(TC_CHKHORA, chkhora);
        valores.put(TC_ACCPREV, accprev);
        valores.put(TC_ACCCORR, acccorr);
        valores.put(TC_HORRI, horri);
        valores.put(TC_NUMEST, numest);
        valores.put(TC_NUMCONSI, numconsi);
        valores.put(TC_AUTORIZADO, autorizado);
        valores.put(TC_IDAUTO, idauto);
        valores.put(TC_REGESTACION, regestacion);
        return valores;
    }

    private ContentValues genera_control2(ControlServicio cs){
        ContentValues valores = new ContentValues();
        valores.put(TC_IDVISXAPLI, cs.getIdvisxapli());
        valores.put(TC_FECHA, cs.getFecha());
        valores.put(TC_IDVIS, cs.getIdvisita());
        valores.put(TC_HINI, cs.getHora_ini());
        valores.put(TC_HFIN, cs.getHora_fin());
        valores.put(TC_OBS, cs.getObs());
        valores.put(TC_CHKPREV, cs.getChkprev());
        valores.put(TC_CHKCORR, cs.getChkcorr());
        valores.put(TC_FIRMA, cs.getFirma());
        valores.put(TC_IMGDISP, cs.getImgdisp());
        valores.put(TC_CHKFOTO, cs.getChkfoto());
        valores.put(TC_CHKHORA, cs.getChkhora());
        valores.put(TC_ACCPREV, cs.getAccprev());
        valores.put(TC_ACCCORR, cs.getAcccorr());
        valores.put(TC_HORRI, cs.getHorri());
        valores.put(TC_NUMEST, cs.getNumest());
        valores.put(TC_NUMCONSI, cs.getNumconsi());
        valores.put(TC_AUTORIZADO, cs.getAutorizado());
        valores.put(TC_IDAUTO, cs.getIdauto());
        valores.put(TC_REGESTACION, cs.getRegestacion());
        return valores;
    }


    private ContentValues genera_imagenes(int idcab, String des, String img){
        ContentValues valores = new ContentValues();
        valores.put(IMG_ID, idcab);
        valores.put(IMG_DESC, des);
        valores.put(IMG_IMG, img);
        return valores;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }

    private ConexionSQLiteHelper helper;
    private SQLiteDatabase db;

    public Utilidades(Context context) {
        helper = new ConexionSQLiteHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertar_usuario(cl_usuario us){
        db.insert(TABLA_USUARIO,null, genera_usuario(us.getCodigo(), us.getCedula(), us.getNombre(),us.getApellido(), us.getSexo(), us.getTelefono(), us.getCorreo(), us.getDireccion(), us.getNomorg(), us.getOrganizacion(), us.getUsuario(), us.getContra(), us.getTipousuario(),0,"----", us.getToken()));
    }

    public void insertar_IMAGEN(cs_idimagen cs, int idvisxapli){
        db.insert(TABLA_IMAGEN,null, genera_imagenes(idvisxapli, cs.getDescripcion(), cs.getImagen()));
    }

    public void insertar_listimagen(ArrayList<cs_idimagen> list_img, int idvisxapli){
        for(int x=0;x<list_img.size();x++) {
            insertar_IMAGEN(list_img.get(x), idvisxapli);
        }
    }

    public void insertar_control(ControlServicio cs){
        db.insert(TABLA_CONTROL,null, genera_control(cs.getIdvisxapli(), cs.getFecha(), cs.getIdvisita(), cs.getHora_ini(), cs.getHora_fin(), cs.getObs(), cs.getChkprev(), cs.getChkcorr(), cs.getFirma(), cs.getImgdisp(), cs.getChkfoto(), cs.getChkhora(), cs.getAccprev(), cs.getAcccorr(), cs.getHorri(), cs.getNumest(), cs.getNumconsi(), cs.getAutorizado(), cs.getIdauto(), cs.getRegestacion()));
    }



    public void insertar_consideracion(cs_consideracion cs, int idvisxapli) {

        db.insert(TABLA_CONSIDERACION, null, genera_consideracion(cs.getIdconsi(), cs.getConsideracion(), idvisxapli));
    }

    public void insertar_listconsi(ArrayList<cs_consideracion> list_consi, int idvisxapli){
        for(int x=0;x<list_consi.size();x++) {
            insertar_consideracion(list_consi.get(x), idvisxapli);
        }
    }

    public void insertar_tratamiento(cs_tratamiento cs, int idvisxapli) {

        db.insert(TABLA_TRATAMIENTO, null, genera_tratamiento(cs.getIdtrat(), cs.getTratamiento(), idvisxapli));
    }

    public void insertar_listtrata(ArrayList<cs_tratamiento> list_trat, int idvisxapli){
        for(int x=0;x<list_trat.size();x++) {
            insertar_tratamiento(list_trat.get(x), idvisxapli);
        }
    }

    public void insertar_areas(ar_area ar, int idvisxapli) {

        db.insert(TABLA_AREAS, null, genera_areas(ar.getAr_area(), ar.getAr_nombarea(), ar.getAr_plaga(),ar.getAr_nombplaga(), ar.getAr_pob(), ar.getAr_nombpob(), ar.getAr_trat(), ar.getAr_tratamiento(), idvisxapli));
    }

    public void insertar_listarea(ArrayList<ar_area> list_area, int idvisxapli){
        for(int x=0;x<list_area.size();x++) {
            insertar_areas(list_area.get(x), idvisxapli);
        }
    }


    public void insertar_estacion(cs_estacion es, int idvisxapli) {

        db.insert(TABLA_ESTACION, null, genera_estaciones(es.getCs_num(), es.getCs_estado(), idvisxapli));
    }

    public void insertar_lampara(cs_lampara la, int idvisxapli) {

        db.insert(TABLA_LAMPARA, null, genera_lampara(la.getCl_num(), la.getCl_pob(), idvisxapli));
    }

    public void insertar_listlampara(ArrayList<cs_lampara> list_lampara, int idvisxapli){
        for(int x=0;x<list_lampara.size();x++) {
            insertar_lampara(list_lampara.get(x), idvisxapli);
        }
    }



    public void insertar_listestacion(ArrayList<cs_estacion> list_estacion, int idvisxapli){
        for(int x=0;x<list_estacion.size();x++) {
            insertar_estacion(list_estacion.get(x), idvisxapli);
        }
    }


    public Cursor CargarDatosUsuario() {
        String[] columnas = new String[]{US_COD, US_CED, US_NOM, US_APE, US_SEX, US_TEL, US_COR, US_DIR, US_NOMORG, US_ORG, US_USU, US_CLA, US_TUS, US_ACT, US_EST, US_TOK};
        return db.query(TABLA_USUARIO, columnas, null, null, null, null, null);
    }

    public Cursor CargarDatosImagen(int id_visxapli) {
        String[] columnas = new String[]{IMG_ID, IMG_DESC, IMG_IMG};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_IMAGEN, columnas, IMG_ID+"=?", parametros, null, null, null);
    }

    public Cursor CargarDatosConsideracion(int id_visxapli) {
        String[] columnas = new String[]{CS_IDCONSI, CS_CONSIDERACION, CS_IDVISXAPLI};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_CONSIDERACION, columnas, CS_IDVISXAPLI+"=?", parametros, null, null, null);
    }

    public Cursor CargarDatosTratamientos(int id_visxapli) {
        String[] columnas = new String[]{TR_IDTRAT, TR_TRATAMIENTO, TR_IDVISXAPLI};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_TRATAMIENTO, columnas, TR_IDVISXAPLI+"=?", parametros, null, null, null);
    }

    public Cursor CargarDatosAreas(int id_visxapli) {
        String[] columnas = new String[]{AR_AREA, AR_NOMBAREA, AR_PLAGA, AR_NOMBPLAGA, AR_POB, AR_NOMBPOB, AR_TRAT, AR_NOMBTRAT, AR_IDVISXAPLI};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_AREAS, columnas, AR_IDVISXAPLI+"=?", parametros, null, null, null);
    }

    public Cursor CargarDatosEstacion(int id_visxapli) {
        String[] columnas = new String[]{ES_NUM, ES_ESTADO, ES_IDVISXAPLI};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_ESTACION, columnas, ES_IDVISXAPLI+"=?", parametros, null, null, null);
    }

    public Cursor CargarDatosLampara(int id_visxapli) {
        String[] columnas = new String[]{LP_NUM, LP_POB, LP_IDVISXAPLI};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_LAMPARA, columnas, LP_IDVISXAPLI+"=?", parametros, null, null, null);
    }


    public Cursor CargarDatosControl(int id_visxapli) {
        String[] columnas = new String[]{TC_IDVISXAPLI, TC_FECHA, TC_IDVIS, TC_HINI, TC_HFIN, TC_OBS, TC_CHKPREV, TC_CHKCORR, TC_FIRMA, TC_IMGDISP, TC_CHKFOTO, TC_CHKHORA, TC_ACCPREV, TC_ACCCORR, TC_HORRI, TC_NUMEST, TC_NUMCONSI, TC_AUTORIZADO, TC_IDAUTO, TC_REGESTACION};
        String[] parametros = {""+id_visxapli};
        return db.query(TABLA_CONTROL, columnas, TC_IDVISXAPLI+"=?", parametros, null, null, null);
    }



    public void Actualiza_establecimiento(int estab, String nomestab, int idusu){
        ContentValues valores = new ContentValues();
        valores.put(US_ACT, estab);
        valores.put(US_EST, nomestab);
        String[] parametros = {""+idusu};
        db.update(TABLA_USUARIO, valores, US_COD+"=?", parametros);
    }


    public void Actualiza_usuario(cl_usuario usuario){
        ContentValues valores = new ContentValues();
        valores.put(US_CED, usuario.getCedula());
        valores.put(US_NOM, usuario.getNombre());
        valores.put(US_APE, usuario.getApellido());
        valores.put(US_SEX, usuario.getSexo());
        valores.put(US_TEL, usuario.getTelefono());
        valores.put(US_COR, usuario.getCorreo());
        valores.put(US_DIR, usuario.getDireccion());
        valores.put(US_NOMORG, usuario.getNomorg());
        valores.put(US_ORG, usuario.getOrganizacion());
        valores.put(US_USU, usuario.getUsuario());
        valores.put(US_CLA, usuario.getContra());
        valores.put(US_TUS, usuario.getTipousuario());
        String[] parametros = {""+usuario.getCodigo()};
        db.update(TABLA_USUARIO, valores, US_COD+"=?", parametros);
    }

    public void Actualiza_control(int id_visxapli, String fecha, int idvisita, String hora_ini, String hora_fin, String obs, int chkprev, int chkcorr, String firma, int imgdisp, int chkfoto, int chkhora, String accprev, String acccorr, String horri, int numest, int numconsi, String autorizado, String idauto, int regestacion){
        ContentValues valores = new ContentValues();
        valores.put(TC_FECHA, fecha);
        valores.put(TC_IDVIS, idvisita);
        valores.put(TC_HINI, hora_ini);
        valores.put(TC_HFIN, hora_fin);
        valores.put(TC_OBS, obs);
        valores.put(TC_CHKPREV, chkprev);
        valores.put(TC_CHKCORR, chkcorr);
        valores.put(TC_FIRMA, firma);
        valores.put(TC_IMGDISP, imgdisp);
        valores.put(TC_CHKFOTO, chkfoto);
        valores.put(TC_CHKHORA, chkhora);
        valores.put(TC_ACCPREV, accprev);
        valores.put(TC_ACCCORR, acccorr);
        valores.put(TC_HORRI, horri);
        valores.put(TC_NUMEST, numest);
        valores.put(TC_NUMCONSI, numconsi);
        valores.put(TC_AUTORIZADO, autorizado);
        valores.put(TC_IDAUTO, idauto);
        valores.put(TC_REGESTACION, regestacion);
        String[] parametros = {""+id_visxapli};
        db.update(TABLA_CONTROL, valores, TC_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_control2(ControlServicio cs){
        ContentValues valores = new ContentValues();
        valores.put(TC_FECHA, cs.getFecha());
        valores.put(TC_IDVIS, cs.getIdvisita());
        valores.put(TC_HINI, cs.getHora_ini());
        valores.put(TC_HFIN, cs.getHora_fin());
        valores.put(TC_OBS, cs.getObs());
        valores.put(TC_CHKPREV, cs.getChkprev());
        valores.put(TC_CHKCORR, cs.getChkcorr());
        valores.put(TC_FIRMA, cs.getFirma());
        valores.put(TC_IMGDISP, cs.getImgdisp());
        valores.put(TC_CHKFOTO, cs.getChkfoto());
        valores.put(TC_CHKHORA, cs.getChkhora());
        valores.put(TC_ACCPREV, cs.getAccprev());
        valores.put(TC_ACCCORR, cs.getAcccorr());
        valores.put(TC_HORRI, cs.getHorri());
        valores.put(TC_NUMEST, cs.getNumest());
        valores.put(TC_NUMCONSI, cs.getNumconsi());
        valores.put(TC_AUTORIZADO, cs.getAutorizado());
        valores.put(TC_IDAUTO, cs.getIdauto());
        valores.put(TC_REGESTACION, cs.getRegestacion());
        String[] parametros = {""+cs.getIdvisxapli()};
        db.update(TABLA_CONTROL, valores, TC_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_consideracion(int idconsi, String consideracion, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(CS_IDCONSI, idconsi);
        valores.put(CS_CONSIDERACION, consideracion);
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_CONSIDERACION, valores, CS_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_consideracion2(cs_consideracion cs, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(CS_IDCONSI, cs.getIdconsi());
        valores.put(CS_CONSIDERACION, cs.getConsideracion());
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_CONSIDERACION, valores, CS_IDVISXAPLI+"=?", parametros);
    }



    public void Actualiza_tratamientos(int idtrat, String tratamiento, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(TR_IDTRAT, idtrat);
        valores.put(TR_TRATAMIENTO, tratamiento);
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_TRATAMIENTO, valores, TR_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_tratamientos2(cs_tratamiento tr, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(TR_IDTRAT, tr.getIdtrat());
        valores.put(TR_TRATAMIENTO, tr.getTratamiento());
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_TRATAMIENTO, valores, TR_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_areas(int area, String nombarea, int plaga, String nombplaga, int poblacion, String nombpob, int trata, String nombtrata, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(AR_AREA, area);
        valores.put(AR_NOMBAREA, nombarea);
        valores.put(AR_PLAGA, plaga);
        valores.put(AR_NOMBPLAGA, nombplaga);
        valores.put(AR_POB, poblacion);
        valores.put(AR_NOMBPOB, nombpob);
        valores.put(AR_TRAT, trata);
        valores.put(AR_NOMBTRAT, nombtrata);
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_AREAS, valores, AR_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_areas2(ar_area ar, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(AR_AREA, ar.getAr_area());
        valores.put(AR_NOMBAREA, ar.getAr_nombarea());
        valores.put(AR_PLAGA, ar.getAr_plaga());
        valores.put(AR_NOMBPLAGA, ar.getAr_nombplaga());
        valores.put(AR_POB, ar.getAr_pob());
        valores.put(AR_NOMBPOB, ar.getAr_nombpob());
        valores.put(AR_TRAT, ar.getAr_trat());
        valores.put(AR_NOMBTRAT, ar.getAr_tratamiento());
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_AREAS, valores, AR_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_estaciones(int num, int estado, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(ES_NUM, num);
        valores.put(ES_ESTADO, estado);
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_ESTACION, valores, ES_IDVISXAPLI+"=?", parametros);
    }

    public void Actualiza_estaciones2(cs_estacion es, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(ES_NUM, es.getCs_num());
        valores.put(ES_ESTADO, es.getCs_estado());
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_ESTACION, valores, ES_IDVISXAPLI+"=?", parametros);
    }


    public void Actualiza_lampara(cs_lampara la, int idvisxapli){
        ContentValues valores = new ContentValues();
        valores.put(LP_NUM, la.getCl_num());
        valores.put(LP_POB, la.getCl_pob());
        String[] parametros = {""+idvisxapli};
        db.update(TABLA_LAMPARA, valores, LP_IDVISXAPLI+"=?", parametros);
    }

    public void EliminarUsuario(){
        db.execSQL("delete from "+ TABLA_USUARIO);
    }

    public void EliminarImagenxIdvisxapli(int idvisxapli){
        db.execSQL("delete from "+ TABLA_IMAGEN + " where " + IMG_ID+" = "+ idvisxapli);
    }

    public void EliminarImagen(){
        db.execSQL("delete from "+ TABLA_IMAGEN);
    }




    public void EliminarControl(int idvisxapli){
        EliminarConsideracion(idvisxapli);
        EliminarTratamientos(idvisxapli);
        EliminarAreas(idvisxapli);
        EliminarEstacion(idvisxapli);
        EliminarImagenxIdvisxapli(idvisxapli);
        db.execSQL("delete from "+ TABLA_CONTROL + " where " + TC_IDVISXAPLI+" = "+ idvisxapli);
    }

    public void EliminarConsideracion(int idvisxapli){
        db.execSQL("delete from "+ TABLA_CONSIDERACION + " where " + CS_IDVISXAPLI+" = "+ idvisxapli);
    }

    public void EliminarTratamientos(int idvisxapli){
        db.execSQL("delete from "+ TABLA_TRATAMIENTO + " where " + TR_TRATAMIENTO+" = "+ idvisxapli);
    }

    public void EliminarAreas(int idvisxapli){
        db.execSQL("delete from "+ TABLA_AREAS + " where " + AR_IDVISXAPLI+" = "+ idvisxapli);
    }

    public void EliminarEstacion(int idvisxapli){
        db.execSQL("delete from "+ TABLA_ESTACION + " where " + ES_IDVISXAPLI+" = "+ idvisxapli);
    }

    public void EliminarLampara(int idvisxapli){
        db.execSQL("delete from "+ TABLA_LAMPARA + " where " + LP_IDVISXAPLI+" = "+ idvisxapli);
    }


    public static void Presenta_Popup(String titulo, String mensaje, Context context){
        final CharSequence[] opciones = {"ok"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                // Do something with value!
            }
        });

        builder.show();
    }




}
