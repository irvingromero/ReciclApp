package reciclapp.reciclapp.SesionRecicladora;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import reciclapp.reciclapp.InicioDeSesion.Inicio;
import reciclapp.reciclapp.Interfaces.InterRecicla;
import reciclapp.reciclapp.R;

public class SesionRecicladora extends AppCompatActivity implements InterRecicla {

    private String logeado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent i = new Intent(SesionRecicladora.this, Inicio.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { }
            }).setIcon(android.R.drawable.ic_delete).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.imagenes_SesionRe)
        {

        } else if (id == R.id.modificarDatos_SesionRe)
        {
            Intent i = new Intent(this, ModificarRecicladora.class);
            i.putExtra("usuario", logeado);
            startActivity(i);
            finish();

        } else if (id == R.id.ubicacion_SesionRe)
        {
            Intent i = new Intent(SesionRecicladora.this, UbicacionRecicladora.class);
            i.putExtra("usuario", logeado);
            startActivity(i);
            finish();

        } else if (id == R.id.agregarHorario_re)
        {
            agregarHorario();

        } else if (id == R.id.agregarRecicladora_re)
        {
            Toast.makeText(this, "Proximamente", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.cerrarSesion_re)
        {
            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(SesionRecicladora.this, Inicio.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { }
                    }).setIcon(android.R.drawable.ic_delete).show();

        }else if (id == R.id.infoDesarrollador)
        {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void agregarHorario()
    {
        AlertDialog.Builder ventanita = new AlertDialog.Builder(this);
        ventanita.setTitle("Horario del negocio");
            //// CARGA EL LAYOUT DEL HORARIO ////
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.agregar_horario, null);
        ventanita.setView(dialogView);

        Spinner spin = findViewById(R.id.spLunHrAb);

        ventanita.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ventanita.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});

        ventanita.show();
    }
}