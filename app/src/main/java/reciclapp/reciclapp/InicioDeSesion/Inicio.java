package reciclapp.reciclapp.InicioDeSesion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import reciclapp.reciclapp.Inicio.InicioPrincipal;
import reciclapp.reciclapp.Interfaces.InterInicio;
import reciclapp.reciclapp.R;

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
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent i = new Intent(Inicio.this, InicioPrincipal.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }
}