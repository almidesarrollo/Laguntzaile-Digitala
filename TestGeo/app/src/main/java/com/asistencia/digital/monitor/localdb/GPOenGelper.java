package com.asistencia.digital.monitor.localdb;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GPOenGelper extends SQLiteOpenHelper {

    public  GPOenGelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public  GPOenGelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     String sentencia,sentencia2;

        sentencia="CREATE TABLE Localizacion (latitud REAL,longitud REAL ,milis REAL)";
        sentencia2="CREATE TABLE CodigoCliente (id_codigo REAL)";
        db.execSQL(sentencia);
        db.execSQL(sentencia2);
    }

    public void reiniciarPuntos(SQLiteDatabase db)
    {
        String sentencia;

        sentencia="DELETE FROM Localizacion";

        db.execSQL(sentencia);
    }
    public void insertarCodigo(SQLiteDatabase db,int id)
    {
        String sentencia;
        sentencia="Insert Into CodigoCliente(id_codigo) values ("+id+")";
        db.execSQL(sentencia);
    }
    public void getId()
    {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * INSERTA EN LA BASE DE DATOS DE SQLLITE
     * */
     public static void insertSQLite(String latitud, String longitud, SQLiteDatabase db) {


        String sentence = "INSERT INTO localizacion (latitud,longitud,milis) values('" + latitud + "','" + longitud + "','"+System.currentTimeMillis()+"')";
        try {
            db.execSQL(sentence);
        }catch (SQLException e)
        {
            Log.e("DB_ERROR", "ERROR DE BASE DE DATOS \n" + e.toString());
        }

    }
}