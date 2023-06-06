package com.rubik.rubikinteractive.bistonapp.Entidades;

/**
 * Created by RubikInteractive on 4/30/19.
 */

public class cs_estacion {

    private int cs_num;
    private int cs_estado;

    public cs_estacion( int cs_idestado, int cs_estado) {
        this.cs_num = cs_idestado;
        this.cs_estado = cs_estado;
    }

    public cs_estacion(){

    }

    public int getCs_num() {
        return cs_num;
    }

    public void setCs_num(int cs_idestado) {
        this.cs_num = cs_idestado;
    }

    public int getCs_estado() {
        return cs_estado;
    }

    public void setCs_estado(int cs_estado) {
        this.cs_estado = cs_estado;
    }



}
