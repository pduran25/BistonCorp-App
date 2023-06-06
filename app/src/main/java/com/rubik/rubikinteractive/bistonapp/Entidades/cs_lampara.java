package com.rubik.rubikinteractive.bistonapp.Entidades;

public class cs_lampara {

    private int cl_num;
    private int cl_pob;

    public cs_lampara(int cl_num, int cl_pob) {
        this.cl_num = cl_num;
        this.cl_pob = cl_pob;
    }

    public cs_lampara(){

    }

    public int getCl_num() {
        return cl_num;
    }

    public void setCl_num(int cl_num) {
        this.cl_num = cl_num;
    }

    public int getCl_pob() {
        return cl_pob;
    }

    public void setCl_pob(int cl_pob) {
        this.cl_pob = cl_pob;
    }
}
