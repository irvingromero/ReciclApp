package reciclapp.reciclapp.SesionRecicladora;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.Desarrollador;
import reciclapp.reciclapp.InicioDeSesion.Inicio;
import reciclapp.reciclapp.Interfaces.SesionRecicladora_SesionRecicladora;
import reciclapp.reciclapp.R;

public class SesionRecicladora extends AppCompatActivity implements SesionRecicladora_SesionRecicladora {

    private TextView mostrarUsuario;
    private String logeado;
    private ImageView fotoPerfil;
    private boolean imagenSubida;
    private EditText campo_material, campo_precio;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FloatingActionMenu floatingActionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");
        setTitle("Inicio");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionMenu = findViewById(R.id.fam);
        floatingActionMenu.setClosedOnTouchOutside(true);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            //// MODIFICAR LA CABECERA DEL MENU////
        View cabecera = navigationView.getHeaderView(0);
        mostrarUsuario = cabecera.findViewById(R.id.mostrarUsuario_sesionReci);
        mostrarUsuario.setText("Usuario: "+logeado);
        fotoPerfil = cabecera.findViewById(R.id.fotoPerfil_sesionReci);
        cargarFoto();

        Bundle bundle = new Bundle();
        InicioRecicladora inicio = new InicioRecicladora();
        bundle.putString("usuario", logeado);
        inicio.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionRecicladora, inicio).commit();
    }

    private void cargarFoto()
    {
        BaseDeDatos baseDeDatos = new BaseDeDatos(this, "FotoRecicladora", null, 1);
        SQLiteDatabase ft = baseDeDatos.getWritableDatabase();
        Cursor consultaFoto = ft.rawQuery("select imagen from FotoRecicladora where usuario ='" + logeado + "'", null);
        if (consultaFoto.moveToFirst())
        {
                //// CASTEA EL ARREGLO DE BYTES A BITMAP Y LUEGO A IMAGEVIEW ////
            byte [] arregloImagen = consultaFoto.getBlob(0);
            Bitmap fotoBitmap = arreglobyteToBitmap(arregloImagen);
            fotoPerfil.setImageBitmap(fotoBitmap);
            imagenSubida = true;
        }
        ft.close();
    }

    public void agregarMaterial(View v)
    {
        BaseDeDatos bdUbicacion = new BaseDeDatos(this, "Ubicacion", null, 1);
        SQLiteDatabase ubi = bdUbicacion.getWritableDatabase();
            //// VALIDA QUE YA SE HAYA AGREGADO LA UBICACION ANTES DE AGREGAR MATERIALES ////
        Cursor consultaUbi = ubi.rawQuery("select latitud, longitud from Ubicacion where usuario ='"+logeado+"'", null);
        if(consultaUbi.moveToFirst())
        {
            floatingActionMenu.hideMenuButton(true);
            ubi.close();
            AlertDialog.Builder ventanita = new AlertDialog.Builder(this);
            ventanita.setTitle("Ingrese los datos del material");
            //// MOSTRAR SPINNER /////
            final Spinner sp = new Spinner(SesionRecicladora.this);
            String[] unidades = new String[] {"Unidad:", "Toneladas", "Kilogramo", "Gramo", "Libra","Litro"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unidades);
            sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            sp.setAdapter(adapter);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            campo_material = new EditText(this);
            campo_precio = new EditText(this);
            campo_material.setInputType(InputType.TYPE_CLASS_TEXT);
            campo_precio.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            campo_precio.setRawInputType(Configuration.KEYBOARD_QWERTY);
            campo_material.setHint("Material");
            campo_precio.setHint("Precio");

            linearLayout.addView(campo_material);
            linearLayout.addView(campo_precio);
            linearLayout.addView(sp);
            ventanita.setView(linearLayout);

            ventanita.setPositiveButton("Agregar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    String material = campo_material.getText().toString();
                    String validarPrecio = campo_precio.getText().toString();
                    //// VALIDAR QUE LOS CAMPOS NO ESTEN VACIOS /////
                    if(!material.isEmpty() && !validarPrecio.isEmpty())
                    {
                        boolean numeroValido = false;
                        double precio = 0;
                        try{
                            precio = Double.parseDouble(campo_precio.getText().toString());

                            numeroValido = true;
                        }catch (Exception e){
                            Toast.makeText(SesionRecicladora.this, "Numero no valido", Toast.LENGTH_SHORT).show();
                        }
                        if(numeroValido == true)
                        {
                            //// VALIDA QUE LOS DATOS A INGRESAR NO ESTEN YA DADOS DE ALTA /////
                            BaseDeDatos bd = new BaseDeDatos(getApplicationContext(), "Materiales", null , 1);
                            SQLiteDatabase dllMaterial = bd.getWritableDatabase();
                            Cursor consultaRecicla = dllMaterial.rawQuery("select material, precio from Materiales where material ='"+material+"' and  precio = '"+precio+"' and  usuario = '"+logeado+"'",null);
                            if(consultaRecicla.moveToFirst())
                            {
                                Toast.makeText(SesionRecicladora.this, "Material y precio ya registrado", Toast.LENGTH_SHORT).show();
                                dllMaterial.close();
                            }
                            else
                            {
                                dllMaterial.close();

                                String unidad = sp.getSelectedItem().toString();

                                if(unidad.equals("Unidad:") == true)
                                {
                                    Toast.makeText(SesionRecicladora.this, "Debes seleccionar una unidad", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    BaseDeDatos baseDatos = new BaseDeDatos(SesionRecicladora.this, "Materiales", null , 1);
                                    SQLiteDatabase flujoDatos = baseDatos.getWritableDatabase();

                                    ContentValues cv = new ContentValues();
                                    cv.put("usuario", logeado);
                                    cv.put("material", material);
                                    cv.put("precio", precio);
                                    cv.put("unidad", unidad);

                                    flujoDatos.insert("Materiales", null, cv);
                                    flujoDatos.close();
                                    Toast.makeText(getApplicationContext(),"Material agregado",Toast.LENGTH_LONG).show();

                                    Bundle bundle = new Bundle();
                                    InicioRecicladora inicio = new InicioRecicladora();
                                    bundle.putString("usuario", logeado);
                                    inicio.setArguments(bundle);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.mainSesionRecicladora, inicio).commit();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(SesionRecicladora.this, "Precio no valido", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SesionRecicladora.this, "Debes llenar los campos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ventanita.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    floatingActionMenu.showMenuButton(true);
                }});

            ventanita.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    floatingActionMenu.showMenuButton(true);
                }
            });

            ventanita.show();
        }
        else
        {
            Toast.makeText(this, "Primero debes agregar la ubicacion", Toast.LENGTH_SHORT).show();
            ubi.close();
        }
    }

    public void modificarMaterial(View v)
    {
        floatingActionMenu.hideMenuButton(true);
        drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().hide();

        Bundle bundle = new Bundle();
        ModificarEliminarMaterial mem = new ModificarEliminarMaterial();

        bundle.putString("usuario", logeado);
        bundle.putString("accion", "modificando");
        mem.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainSesionRecicladora,  mem, "fragment_modif");
        ft.setTransition(FragmentTransaction.TRANSIT_UNSET);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void eliminarMaterial(View v)
    {
        floatingActionMenu.hideMenuButton(true);
        drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().hide();

        Bundle bundle = new Bundle();
        ModificarEliminarMaterial mem = new ModificarEliminarMaterial();

        bundle.putString("usuario", logeado);
        bundle.putString("accion", "eliminando");
        mem.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainSesionRecicladora,  mem, "fragment_eliminar");
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            floatingActionMenu.showMenuButton(true);
            getSupportActionBar().show();
            drawer.setDrawerLockMode(drawer.LOCK_MODE_UNLOCKED);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.imagenes_SesionRe) {
            foto();

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
            getSupportActionBar().hide();
            floatingActionMenu.hideMenuButton(true);
            drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);

            HorarioRecicladora horario = new HorarioRecicladora();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainSesionRecicladora,  horario, "fragment_meters");
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.agregarRecicladora_re) {
            Toast.makeText(this, "Proximamente", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.cerrarSesion_re)
        {
            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SesionRecicladora.this, Inicio.class);
                            startActivity(intent);
                            finish();

                            SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                            preferences.edit().clear().commit();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_delete).show();

        } else if (id == R.id.infoDesarrollador)
        {
            floatingActionMenu.hideMenuButton(true);
            getSupportActionBar().hide();
            drawer.setDrawerLockMode(drawer.LOCK_MODE_LOCKED_CLOSED);

            Desarrollador d = new Desarrollador();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainSesionRecicladora, d, "fragments");
            ft.setTransition(FragmentTransaction.TRANSIT_UNSET);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void foto()
    {
        boolean permisoActivado;
        if(imagenSubida == true)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                permisoActivado = estadoPermiso();
                if (permisoActivado == false)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                }
                else
                {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    gallery.setType("image/");
                    startActivityForResult(gallery, 2);
                }
            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.setType("image/");
                startActivityForResult(gallery, 2);
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                permisoActivado = estadoPermiso();
                if (permisoActivado == false) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    gallery.setType("image/");
                    startActivityForResult(gallery, 1);
                }
            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.setType("image/");
                startActivityForResult(gallery, 1);
            }
        }
    }

    private boolean estadoPermiso()
    {
        int resultado = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(resultado == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                Uri path = data.getData();
                fotoPerfil.setImageURI(path);

                byte [] imagen = imageViewToByte(fotoPerfil);

                ContentValues cv = new ContentValues();
                cv.put("usuario", logeado);
                cv.put("imagen", imagen);

                BaseDeDatos bd = new BaseDeDatos(this, "FotoRecicladora", null, 1);
                SQLiteDatabase db = bd.getWritableDatabase();
                db.insert("FotoRecicladora", null, cv);
                db.close();

                Toast.makeText(this, "Imagen subida", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == 2)
        {
            Uri entrada = data.getData();
            fotoPerfil.setImageURI(entrada);
            byte [] imagen = imageViewToByte(fotoPerfil);

            ContentValues nuevo = new ContentValues();
            nuevo.put("usuario", logeado);
            nuevo.put("imagen", imagen);

            BaseDeDatos bd = new BaseDeDatos(this, "FotoRecicladora", null, 1);
            SQLiteDatabase flujo = bd.getWritableDatabase();
            flujo.update("FotoRecicladora", nuevo, "usuario="+ "'"+logeado+"'", null);
            flujo.close();
        }
    }

    private byte[] imageViewToByte(ImageView image) //// CASTEA UN IMAGEVIEW A UN ARREGLO DE BYTES ////
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    private Bitmap arreglobyteToBitmap(byte [] bytes)
    {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bmp;
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }
}