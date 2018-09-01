package reciclapp.reciclapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import reciclapp.reciclapp.Interfaces.InterInicio;

public class Inicio extends AppCompatActivity implements InterInicio {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        InicioSesion inicio = new InicioSesion();
        getSupportFragmentManager().beginTransaction().replace(R.id.inicioActivity, inicio).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }
}