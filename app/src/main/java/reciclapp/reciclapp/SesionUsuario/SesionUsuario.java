package reciclapp.reciclapp.SesionUsuario;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.Desarrollador;
import reciclapp.reciclapp.Inicio.MapaInicio;
import reciclapp.reciclapp.InicioDeSesion.Inicio;
import reciclapp.reciclapp.R;
import reciclapp.reciclapp.Reciclaje.GuiaReciclaje;
import reciclapp.reciclapp.Reciclaje.Manualidades;

public class SesionUsuario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String usuario;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private boolean busquedaMaterial;
    private TextView mostrarUsuario;

    private String materialBuscado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_usuario);

        usuario = getIntent().getExtras().getString("usuario");
        setTitle("Inicio");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View cabecera = navigationView.getHeaderView(0);
        mostrarUsuario = cabecera.findViewById(R.id.mostrarUsuario_sesionUsuario);
        mostrarUsuario.setText("Usuario: "+usuario);

        Bundle bundle = new Bundle();
        MapaInicio mi = new MapaInicio();
        bundle.putString("sesionUsuario", usuario);
        mi.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionUsuario, mi).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            getSupportActionBar().show();
            drawer.setDrawerLockMode(drawer.LOCK_MODE_UNLOCKED);

            if(busquedaMaterial == true)
            {
                setTitle("Inicio");
                Bundle bundle = new Bundle();
                MapaInicio mi = new MapaInicio();
                bundle.putString("material", null);
                bundle.putString("sesionUsuario", usuario);
                mi.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionUsuario, mi)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();

                    busquedaMaterial = false;
            }
            else
            {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.modificarDatos_sesionUsuario)
        {
            Bundle bundle = new Bundle();
            ModificarUsuario mu = new ModificarUsuario();
            bundle.putString("usuario", usuario);
            mu.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainSesionUsuario, mu, "modificarUsuario");
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.addToBackStack(null);
            ft.commit();

            getSupportActionBar().hide();
            drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);

        } else if (id == R.id.buscarMaterial_sesionUsuario)
        {
            ventanaMateriales();

        } else if (id == R.id.mejorPrecio_sesionUsuario)
        {
            buscarMejorPrecio();

        }  else if (id == R.id.masCercano_sesionUsuario)
        {
            masCernana();

        }else if (id == R.id.cerrarSesion_sesionUsuario)
        {
            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SesionUsuario.this, Inicio.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_delete).show();

        }
        else if (id == R.id.guiaReciclaje_sesionUsuario)
        {
            Intent intent = new Intent(this, GuiaReciclaje.class);
            startActivity(intent);

        } else if (id == R.id.manualidades_sesionUsuario)
        {
            Intent intent = new Intent(this, Manualidades.class);
            startActivity(intent);

        }else if (id == R.id.donar_sesionUsuario)
        {

        }else if (id == R.id.infoDesarrollador_sesionUsuario)
        {
            Desarrollador d = new Desarrollador();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainSesionUsuario, d, "fragments");
            ft.setTransition(FragmentTransaction.TRANSIT_UNSET);
            ft.addToBackStack(null);
            ft.commit();

            getSupportActionBar().hide();
            drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            int indiceMateriales = 0;

            do{
                materiales [indiceMateriales] = consulta.getString(0);
                indiceMateriales ++;
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
                    busquedaMaterial = true;
                    materialBuscado = seleccion;

                    Bundle bundle = new Bundle();
                    MapaInicio mi = new MapaInicio();
                    bundle.putString("material", seleccion);
                    bundle.putString("sesionUsuario", usuario);
                    mi.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionUsuario, mi).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                }
            }
        });
        ventanita.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});

        ventanita.show();
    }

    private void buscarMejorPrecio()
    {
        if(busquedaMaterial == true)
        {
            Bundle bundle = new Bundle();
            MapaInicio mi = new MapaInicio();
            bundle.putString("materialPrecio", materialBuscado);
            bundle.putString("sesionUsuario", usuario);
            mi.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionUsuario, mi).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
        }
        else
        {
            Toast.makeText(this, "Debes buscar un material primero", Toast.LENGTH_SHORT).show();
        }
    }

    private void masCernana()
    {
        if(busquedaMaterial == true)
        {

        }
        else
        {
            Toast.makeText(this, "Debes buscar un material primero", Toast.LENGTH_SHORT).show();
        }
    }
}