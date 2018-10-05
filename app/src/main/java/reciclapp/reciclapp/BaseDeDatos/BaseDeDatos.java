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
        baseDeDatos.execSQL("create table Puntuador(usuario text, puntuaje double)");

        baseDeDatos.execSQL("create table Recicladoras(usuario text, correo text, contra text, nombre text, telefono text, calle text, calle2 text, colonia text, numeroInt int)");
        baseDeDatos.execSQL("create table FotoRecicladora(usuario text, imagen blob)");
        baseDeDatos.execSQL("create table Ubicacion(usuario text, latitud double, longitud double)");
        baseDeDatos.execSQL("create table Materiales(usuario text, material text, precio double, unidad text)");
        baseDeDatos.execSQL("create table Puntuacion(usuarioRecicladora text, total double, contador int)");

        baseDeDatos.execSQL("create table Domingo(usuario text, abre text, cierra text)");
        baseDeDatos.execSQL("create table Lunes(usuario text, abre text, cierra text)");
        baseDeDatos.execSQL("create table Martes(usuario text, abre text, cierra text)");
        baseDeDatos.execSQL("create table Miercoles(usuario text, abre text, cierra text)");
        baseDeDatos.execSQL("create table Jueves(usuario text, abre text, cierra text)");
        baseDeDatos.execSQL("create table Viernes(usuario text, abre text, cierra text)");
        baseDeDatos.execSQL("create table Sabado(usuario text, abre text, cierra text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}