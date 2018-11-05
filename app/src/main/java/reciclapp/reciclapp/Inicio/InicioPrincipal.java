package reciclapp.reciclapp.Inicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import reciclapp.reciclapp.Reciclaje.GuiaReciclaje;
import reciclapp.reciclapp.Reciclaje.Manualidades;
import reciclapp.reciclapp.SesionRecicladora.SesionRecicladora;
import reciclapp.reciclapp.SesionUsuario.SesionUsuario;

/**************************************************************************
 *Pendientes detalles
 * - Materiales repetidos en la busqueda de un material
 * - Cuando se hace una busqueda y se revisa la info detallada
 * al dar back quita la busqueda del material
 * - Bug al puntuar la segunda recicladora
 * - Al guardar horarios como cerrado dos veces
 * el switch no sale como encendido
 *
 * REGISTROS
 * - Validar que sean minimo 3 caracteres
 * - No caracteres especiales
 ***************************************************************************/
public class InicioPrincipal extends AppCompatActivity implements Inicio_InicioPrincipal {

    private DrawerLayout drawer;
    private boolean mapaInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        sesionGuardada();

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
        bundle.putString("sesionUsuario", null);
        bundle.putString("material", null);
        mi.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainInicioPrincipal, mi).commit();
    }

    private void sesionGuardada()
    {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "nada");

        if(!usuario.equals("nada"))
        {
            /// busqueda en la bd, para saber si el usuario es recicladora o usuario
            BaseDeDatos bdReci = new BaseDeDatos(this, "Recicladoras", null, 1);
            SQLiteDatabase dllReci = bdReci.getWritableDatabase();
            Cursor consultaRecicla = dllReci.rawQuery("select usuario from Recicladoras where usuario ='" + usuario + "'", null);
            if (consultaRecicla.moveToFirst())
            {
                Intent i = new Intent(this, SesionRecicladora.class);
                i.putExtra("usuario", usuario);
                startActivity(i);
                finish();
            }
            dllReci.close();

            BaseDeDatos bd = new BaseDeDatos(this, "Usuarios", null, 1);
            SQLiteDatabase dllUsu = bd.getWritableDatabase();
            Cursor consultaUsuario = dllUsu.rawQuery("select usuario from Usuarios where usuario ='" + usuario + "'", null);
            if (consultaUsuario.moveToFirst())
            {
                Intent i = new Intent(this, SesionUsuario.class);
                i.putExtra("usuario", usuario);
                startActivity(i);
                finish();
            }
            dllUsu.close();
        }
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

            if(mapaInicio == true)
            {
                setTitle("Principal");
                Bundle bundle = new Bundle();
                MapaInicio mi = new MapaInicio();
                bundle.putString("material", null);
                mi.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.mainInicioPrincipal, mi)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();

                mapaInicio = false;
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

        if (id == R.id.sesionRegistro_inicioPrin)
        {
            Intent intent = new Intent(this, Inicio.class);
            startActivity(intent);
            finish();
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
                            finish();
                        }
                    })
                    .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { }
            }).show();

        }
        else if (id == R.id.masCercano_inicioPrin)
        {
            new AlertDialog.Builder(this).setMessage("Debes registrarte o iniciar sesion para usar esta funcion.")
                    .setPositiveButton("Registrarse", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(InicioPrincipal.this, Inicio.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { }
                    }).show();
        }

        else if (id == R.id.programador_inicioPrin)
        {
            Desarrollador d = new Desarrollador();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainInicioPrincipal,  d, "fragment_meters");
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.addToBackStack(null);
            ft.commit();

            drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().hide();
        }
        else if (id == R.id.guiaReciclaje_inicioPrin)
        {
            Intent intent = new Intent(this, GuiaReciclaje.class);
            startActivity(intent);
        }
        else if (id == R.id.manualidades_inicioPrin)
        {
            Intent intent = new Intent(this, Manualidades.class);
            startActivity(intent);
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
                    mapaInicio = true;

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