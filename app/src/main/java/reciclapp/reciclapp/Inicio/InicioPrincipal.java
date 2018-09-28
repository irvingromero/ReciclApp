package reciclapp.reciclapp.Inicio;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.Desarrollador;
import reciclapp.reciclapp.InicioDeSesion.Inicio;
import reciclapp.reciclapp.Interfaces.Inicio_InicioPrincipal;
import reciclapp.reciclapp.R;

public class InicioPrincipal extends AppCompatActivity implements Inicio_InicioPrincipal {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_principal);
        setTitle("Principal");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = new Bundle();
        MapaInicio mi = new MapaInicio();
        bundle.putString("material", null);
        mi.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainInicioPrincipal, mi).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            getSupportActionBar().show();
            drawer.setDrawerLockMode(drawer.LOCK_MODE_UNLOCKED);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sesionRegistro_inicioPrin)
        {
            Intent intent = new Intent(this, Inicio.class);
            startActivity(intent);

        }
        else if (id == R.id.buscarMaterial_inicioPrin)
        {
            ventanaMateriales();

        }
        else if (id == R.id.mejorPrecio_inicioPrin)
        {
            new AlertDialog.Builder(this).setMessage("Debes registrarte o iniciar sesion para usar esta funcion.")
                    .setPositiveButton("Registrarse", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(InicioPrincipal.this, Inicio.class);
                            startActivity(intent);
//                            finish();
                        }
                    })
                    .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { }
                    }).show();

        }
        else if (id == R.id.programador_inicioPrin) {
            Desarrollador d = new Desarrollador();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainInicioPrincipal,  d, "fragment_meters");
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.addToBackStack(null);
            ft.commit();

            drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().hide();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ventanaMateriales()
    {
        final Spinner spinner = new Spinner(this);
        AlertDialog.Builder ventanita = new AlertDialog.Builder(this);
        ventanita.setTitle("Materiales disponibles");

        BaseDeDatos bd = new BaseDeDatos(this, "Materiales", null, 1);
        SQLiteDatabase flujo = bd.getWritableDatabase();
        Cursor consulta = flujo.rawQuery("select material from Materiales", null);
        if(consulta.moveToFirst())
        {
            int totalMatateriales = consulta.getCount();
            String [] materiales = new String [totalMatateriales];
            int indice = 0;

            do{
                materiales [indice] = consulta.getString(0);
                indice ++;
            }while (consulta.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materiales);
            spinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            spinner.setAdapter(adapter);
        }
        else
        {
            String [] nulo = new String [1];
            nulo [0] = "No hay recicladoras disponibles";
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nulo);
            spinner.setAdapter(adapter);
        }
        flujo.close();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(spinner);
        ventanita.setView(linearLayout);

        ventanita.setPositiveButton("Buscar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String seleccion = spinner.getSelectedItem().toString();

                if(!seleccion.equals("No hay recicladoras disponibles"))
                {
                    Bundle bundle = new Bundle();
                    MapaInicio mi = new MapaInicio();
                    bundle.putString("material", seleccion);
                    mi.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainInicioPrincipal, mi).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                }
            }
        });
        ventanita.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});

        ventanita.show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}