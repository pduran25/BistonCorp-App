package com.rubik.rubikinteractive.bistonapp.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RubikInteractive on 4/4/19.
 */

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME ="bs_bistoncorp";
    private static final int DB_SCHEME_VERSION = 14;


    public ConexionSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
        db.execSQL(Utilidades.CREAR_TABLA_IMAGEN);
        db.execSQL(Utilidades.CREAR_TABLA_CONTROL);
        db.execSQL(Utilidades.CREAR_TABLA_CONSIDERACION);
        db.execSQL(Utilidades.CREAR_TABLA_TRATAMIENTO);
        db.execSQL(Utilidades.CREAR_TABLA_AREAS);
        db.execSQL(Utilidades.CREAR_TABLA_ESTACION);
        db.execSQL(Utilidades.CREAR_TABLA_LAMPARA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_IMAGEN);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_CONTROL);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_CONSIDERACION);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_TRATAMIENTO);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_AREAS);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_ESTACION);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_LAMPARA);
        onCreate(db);
    }
}
