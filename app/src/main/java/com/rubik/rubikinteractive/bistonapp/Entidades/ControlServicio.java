package com.rubik.rubikinteractive.bistonapp.Entidades;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by RubikInteractive on 4/30/19.
 */

public class ControlServicio {

    private int idvisxapli;
    private String fecha;
    private int idvisita;
    private String hora_ini;
    private String hora_fin;
    private String obs;
    private int chkprev;
    private int chkcorr;
    private String firma;
    private int imgdisp;
    private int chkfoto;
    private int chkhora;
    private String accprev;
    private String acccorr;
    private String horri;
    private ArrayList<cs_consideracion> List_consi;
    private ArrayList<cs_tratamiento> List_trat;
    private ArrayList<ar_area> List_area;
    private ArrayList<cs_estacion> List_estacion;
    private ArrayList<cs_idimagen> List_regimagen;
    private ArrayList<cs_lampara> List_lampara;
    private int numest;
    private int numconsi;
    private String autorizado;
    private String idauto;
    private int regestacion;
    private int reglampara = 0;




    public void setFirma(Bitmap firmabmp) {
        this.firma = convertirImgString(firmabmp);
    }

    public String convertirImgString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }


    public String getFirma() {
        return firma;
    }







    public ArrayList<cs_idimagen> getList_regimagen() {
        return List_regimagen;
    }

    public void setList_regimagen(ArrayList<cs_idimagen> list_regimagen) {
        List_regimagen = list_regimagen;
    }

    public int getRegestacion() {
        return regestacion;
    }

    public void setRegestacion(int regestacion) {
        this.regestacion = regestacion;
    }




    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public String getIdauto() {
        return idauto;
    }

    public void setIdauto(String idauto) {
        this.idauto = idauto;
    }


    public int getChkfoto() {
        return chkfoto;
    }

    public void setChkfoto(int chkfoto) {
        this.chkfoto = chkfoto;
    }

    public int getImgdisp() {
        return imgdisp;
    }

    public void setImgdisp(int imgdisp) {
        this.imgdisp = imgdisp;
    }



    public int getNumconsi() {
        return numconsi;
    }

    public void setNumconsi(int numconsi) {
        this.numconsi = numconsi;
    }



    public int getNumest() {
        return numest;
    }

    public void setNumest(int numest) {
        this.numest = numest;
    }



    public String getAccprev() {
        return accprev;
    }

    public void setAccprev(String accprev) {
        this.accprev = accprev;
    }

    public String getAcccorr() {
        return acccorr;
    }

    public void setAcccorr(String acccorr) {
        this.acccorr = acccorr;
    }

    public String getHorri() {
        return horri;
    }

    public void setHorri(String horri) {
        this.horri = horri;
    }

    public int getChkprev() {
        return chkprev;
    }

    public void setChkprev(int chkprev) {
        this.chkprev = chkprev;
    }

    public int getChkcorr() {
        return chkcorr;
    }

    public void setChkcorr(int chkcorr) {
        this.chkcorr = chkcorr;
    }

    public int getChkhora() {
        return chkhora;
    }

    public void setChkhora(int chkhora) {
        this.chkhora = chkhora;
    }




    public ControlServicio() {

    }

    public int getIdvisxapli() {
        return idvisxapli;
    }

    public void setIdvisxapli(int idvisxapli) {
        this.idvisxapli = idvisxapli;
    }


    public String getHora_ini() {
        return hora_ini;
    }

    public void setHora_ini(String hora_ini) {
        this.hora_ini = hora_ini;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public ArrayList<cs_consideracion> getList_consi() {
        return List_consi;
    }

    public void setList_consi(ArrayList<cs_consideracion> list_consi) {
        List_consi = list_consi;
    }

    public ArrayList<cs_tratamiento> getList_trat() {
        return List_trat;
    }

    public void setList_trat(ArrayList<cs_tratamiento> list_trat) {
        List_trat = list_trat;
    }

    public ArrayList<ar_area> getList_area() {
        return List_area;
    }

    public void setList_area(ArrayList<ar_area> list_area) {
        List_area = list_area;
    }

    public ArrayList<cs_estacion> getList_estacion() {
        return List_estacion;
    }

    public void setList_estacion(ArrayList<cs_estacion> list_estacion) {
        List_estacion = list_estacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdvisita() {
        return idvisita;
    }

    public void setIdvisita(int idvisita) {
        this.idvisita = idvisita;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public ArrayList<cs_lampara> getList_lampara() {
        return List_lampara;
    }

    public void setList_lampara(ArrayList<cs_lampara> list_lampara) {
        List_lampara = list_lampara;
    }

    public int getReglampara() {
        return reglampara;
    }

    public void setReglampara(int reglampara) {
        this.reglampara = reglampara;
    }
}
