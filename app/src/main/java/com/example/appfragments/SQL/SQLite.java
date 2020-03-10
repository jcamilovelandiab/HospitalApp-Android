package com.example.appfragments.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;

import java.util.ArrayList;

public class SQLite {

    private sql sql;
    private SQLiteDatabase db;

    public SQLite(Context context) {
        sql = new sql(context);
    }

    public void abrir() {
        Log.i("SQLite",
                "Se abre conexión a la base de datos" +
                        sql.getDatabaseName());
        db = sql.getWritableDatabase();
    }

    public void cerrar() {
        Log.i("SQLite",
                "Se cierra conexión a la base de datos" +
                        sql.getDatabaseName());
        sql.close();
    }

    public boolean addregistroPaciente(
            int id,
            String area,
            String doc,
            String nom,
            String sex,
            String date,
            String age,
            String height,
            String weight,
            String image) {
        ContentValues cv = new ContentValues(); //Equivalente a putExtra
        cv.put("ID_PACIENTE", id);
        cv.put("AREA", area);
        cv.put("DOCTOR", doc);
        cv.put("NOMBRE", nom);
        cv.put("SEXO", sex);
        cv.put("FECHA_ING", date);
        cv.put("EDAD", age);
        cv.put("ESTATURA", height);
        cv.put("PESO", weight);
        cv.put("IMAGEN", image);
        return (db.insert(
                "PACIENTES",
                null, cv) != -1) ? true : false;
    }

    //Leer base de datos
    public Cursor getRegistro() {
        return db.rawQuery("SELECT * FROM PACIENTES", null);
    }


    public ArrayList<String> getPaciente(Cursor cursor) {
        ArrayList<String> listData = new ArrayList<>();
        String item = "";
        if (cursor.moveToFirst()) {
            do {
                item += "ID: [" + cursor.getString(0) + "]\r\n";
                item += "Area: [" + cursor.getString(1) + "]\r\n";
                item += "Doctor: [" + cursor.getString(2) + "]\r\n";
                item += "Nombre: [" + cursor.getString(3) + "]\r\n";
                item += "Sexo: [" + cursor.getString(4) + "]\r\n";
                item += "Fecha de Ingreso: [" + cursor.getString(5) + "]\r\n";
                item += "Edad: [" + cursor.getString(6) + "]\r\n";
                item += "Estatura: [" + cursor.getString(7) + "]\r\n";
                item += "Peso: [" + cursor.getString(8) + "]\r\n";
                listData.add(item); //LO AGREGAMOS AL ARRAYLIST
                item = ""; //LIMPIAMOS LA CADENA ITEM
            } while (cursor.moveToNext()); //MIENTRAS LA CONSULTA TENGA DATOS
        }
        return listData;
    }

    public ArrayList<String> getImagenes(Cursor cursor){
        ArrayList<String> listData = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                listData.add(cursor.getString(9));
            }while (cursor.moveToNext());
        }
        return listData;
    }

    public ArrayList<String> getID(Cursor cursor) {
        ArrayList<String> listData = new ArrayList<>();
        String item = "";
        if (cursor.moveToFirst()) {
            do {
                item += "ID: [" + cursor.getString(0) + "]\r\n";
                listData.add(item);
                item = "";
            } while (cursor.moveToNext());
        }
        return listData;
    }

    public String updatePaciente(
            int id,
            String area,
            String doc,
            String nom,
            String sex,
            String date,
            String age,
            String height,
            String weight,
            String image
    ) {
        ContentValues cv = new ContentValues(); //Equivalente a putExtra
        cv.put("ID_PACIENTE", id);
        cv.put("AREA", area);
        cv.put("DOCTOR", doc);
        cv.put("NOMBRE", nom);
        cv.put("SEXO", sex);
        cv.put("FECHA_ING", date);
        cv.put("EDAD", age);
        cv.put("ESTATURA", height);
        cv.put("PESO", weight);
        cv.put("IMAGEN", image);
        int cant = db.update(
                "PACIENTES",
                cv,
                "ID_PACIENTE=" + id,
                null);
        if (cant == 1) {
            return "Paciente modificado";
        } else {
            return "Error, no se modifico";
        }
    }

    public Cursor getCant(int id) {
        return db.rawQuery(
                "SELECT * FROM PACIENTES WHERE ID_PACIENTE=" + id,
                null);
    }

    public int Eliminar(Editable id) {
        return db.delete(
                "PACIENTES",
                "ID_PACIENTE=" + id,
                null);
    }

}

