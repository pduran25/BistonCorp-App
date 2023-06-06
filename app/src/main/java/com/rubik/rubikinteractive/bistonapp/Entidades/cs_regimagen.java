package com.rubik.rubikinteractive.bistonapp.Entidades;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class cs_regimagen {

    private Bitmap idimagen;
    private String descripcion;
    private String imagen;
    private int idvisxapli;

    public int getIdvisxapli() {
        return idvisxapli;
    }

    public void setIdvisxapli(int idvisxapli) {
        this.idvisxapli = idvisxapli;
    }



    public Bitmap getIdimagen() {
        return idimagen;
    }

    public void setIdimagen(Bitmap idimagen) {
        this.idimagen = idimagen;
        this.imagen = convertirImgString(idimagen);
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String convertirImgString(Bitmap bitmap){
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }


}
