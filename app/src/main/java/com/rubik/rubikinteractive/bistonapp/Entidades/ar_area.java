package com.rubik.rubikinteractive.bistonapp.Entidades;

/**
 * Created by RubikInteractive on 4/28/19.
 */

public class ar_area {

    private int ar_area;
    private String ar_nombarea;
    private int ar_plaga;
    private String ar_nombplaga;
    private int ar_pob;
    private String ar_nombpob;
    private int ar_trat;
    private String ar_tratamiento;

    public int getAr_trat() {
        return ar_trat;
    }

    public void setAr_trat(int ar_trat) {
        this.ar_trat = ar_trat;
    }

    public String getAr_tratamiento() {
        return ar_tratamiento;
    }

    public void setAr_tratamiento(String ar_tratamiento) {
        this.ar_tratamiento = ar_tratamiento;
    }

    public ar_area(int ar_area, String ar_nombarea, int ar_plaga, String ar_nombplaga, int ar_pob, String ar_nombpob, int ar_trat, String ar_tratamiento) {
        this.ar_area = ar_area;
        this.ar_nombarea = ar_nombarea;
        this.ar_plaga = ar_plaga;
        this.ar_nombplaga = ar_nombplaga;
        this.ar_pob = ar_pob;
        this.ar_nombpob = ar_nombpob;
        this.ar_trat = ar_trat;
        this.ar_tratamiento = ar_tratamiento;
    }

    public ar_area(){

    }

    public int getAr_area() {
        return ar_area;
    }

    public void setAr_area(int ar_area) {
        this.ar_area = ar_area;
    }

    public int getAr_pob() {
        return ar_pob;
    }

    public void setAr_pob(int ar_pob) {
        this.ar_pob = ar_pob;
    }

    public String getAr_nombpob() {
        return ar_nombpob;
    }

    public void setAr_nombpob(String ar_nombpob) {
        this.ar_nombpob = ar_nombpob;
    }


    public String getAr_nombarea() {
        return ar_nombarea;
    }

    public void setAr_nombarea(String ar_nombarea) {
        this.ar_nombarea = ar_nombarea;
    }

    public int getAr_plaga() {
        return ar_plaga;
    }

    public void setAr_plaga(int ar_plaga) {
        this.ar_plaga = ar_plaga;
    }

    public String getAr_nombplaga() {
        return ar_nombplaga;
    }

    public void setAr_nombplaga(String ar_nombplaga) {
        this.ar_nombplaga = ar_nombplaga;
    }






}
