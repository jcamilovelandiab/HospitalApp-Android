package com.example.appfragments.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sql extends SQLiteOpenHelper {
    private static final String database = "pacientes";
    private static final int VERSION = 2;

    private final String tPacientes = "CREATE TABLE PACIENTES (" +
            "ID_PACIENTE INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "AREA TEXT NOT NULL," +
            "DOCTOR TEXT NOT NULL," +
            "NOMBRE TEXT NOT NULL," +
            "SEXO TEXT NOT NULL," +
            "FECHA_ING TEXT NOT NULL," +
            "EDAD TEXT NOT NULL," +
            "ESTATURA TEXT NOT NULL," +
            "PESO TEXT NOT NULL," +
            "IMAGEN TEXT NOT NULL);";


    //Constructor
    public sql(Context context){
        super(context, database, null, VERSION);
    }

    //metodos heredados
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tPacientes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        if (newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS PACIENTES");
            db.execSQL(tPacientes);
        }
    }
}
