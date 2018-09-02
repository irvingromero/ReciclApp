package reciclapp.reciclapp.SesionRecicladora;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import reciclapp.reciclapp.Inicio.Inicio;
import reciclapp.reciclapp.R;

public class SesionRecicladora extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_recicladora);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
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

        InicioSesionreci inicio = new InicioSesionreci();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionRecicladora, inicio).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.imagenes_SesionRe)
        {

        } else if (id == R.id.modificarDatos_SesionRe)
        {
            fab.hide();

            ModificarRecicladora mr = new ModificarRecicladora();
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainSesionRecicladora,  mr, "fragment_meters");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
            setTitle("Modificar datos");


        } else if (id == R.id.ubicacion_SesionRe)
        {

        } else if (id == R.id.agregarHorario_re)
        {

        } else if (id == R.id.agregarRecicladora_re)
        {

        } else if (id == R.id.cerrarSesion_re)
        {
            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
//                    .setMessage("¿stas seguro de eliminar item?")
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
}