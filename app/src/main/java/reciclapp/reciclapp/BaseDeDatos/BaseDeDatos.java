package reciclapp.reciclapp.BaseDeDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper{

    public BaseDeDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDatos)
    {
        baseDeDatos.execSQL("create table Usuarios(usuario text primary key, correo text, contra text)");
        baseDeDatos.execSQL("create table Recicladoras(usuario text , correo text, contra text, nombre text, telefono text, calle text, calle2 text, colonia text, numeroInt int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}