package com.example.dam2_yeray.modulomovil.BaseDatos;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class GPOenGelper extends SQLiteOpenHelper {

    public  GPOenGelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public  GPOenGelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     String sentencia;

        sentencia="CREATE TABLE Localizacion (latitud REAL,longitud REAL ,milis REAL)";

        db.execSQL(sentencia);
    }

    public void reiniciarPuntos(SQLiteDatabase db)
    {
        String sentencia;

        sentencia="DELETE FROM Localizacion";

        db.execSQL(sentencia);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
