package reciclapp.reciclapp.SesionRecicladora;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.InicioDeSesion.Inicio;
import reciclapp.reciclapp.Interfaces.InterRecicla;
import reciclapp.reciclapp.R;

public class SesionRecicladora extends AppCompatActivity implements InterRecicla {

    private TextView mostrarUsuario;
    private String logeado;
    private Bitmap bitmap;
    private ImageView imageView;
    private boolean imagenSubida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cargarFoto();
        mostrarUsuario = findViewById(R.id.mostrarUsuario_sesionReci);
//        mostrarUsuario.setText("Usuario: "+ logeado);
    }

    private void cargarFoto() {
        BaseDeDatos baseDeDatos = new BaseDeDatos(this, "FotoRecicladora", null, 1);
        SQLiteDatabase ft = baseDeDatos.getWritableDatabase();
        Cursor consultaFoto = ft.rawQuery("select imagen from FotoRecicladora where usuario ='" + logeado + "'", null);
        if (consultaFoto.moveToFirst()) {
            /*
            byte [] arregloImagen = consultaFoto.getBlob(0);
            Bitmap b = m.bm(arregloImagen);
            iv.setImageBitmap(b);

            */
            imagenSubida = true;
        }
        ft.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(SesionRecicladora.this, Inicio.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_delete).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.imagenes_SesionRe) {
            foto();

        } else if (id == R.id.modificarDatos_SesionRe) {
            Intent i = new Intent(this, ModificarRecicladora.class);
            i.putExtra("usuario", logeado);
            startActivity(i);
            finish();

        } else if (id == R.id.ubicacion_SesionRe) {
            Intent i = new Intent(SesionRecicladora.this, UbicacionRecicladora.class);
            i.putExtra("usuario", logeado);
            startActivity(i);
            finish();

        } else if (id == R.id.agregarHorario_re) {


        } else if (id == R.id.agregarRecicladora_re) {
            Toast.makeText(this, "Proximamente", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.cerrarSesion_re) {
            new AlertDialog.Builder(this).setTitle("¿Cerrar sesion?")
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SesionRecicladora.this, Inicio.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(android.R.drawable.ic_delete).show();

        } else if (id == R.id.infoDesarrollador) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void foto()
    {
        boolean permisoActivado;
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
                startActivityForResult(gallery, 1);
            }
        } else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            try
            {
                Uri entrada = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(entrada);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

                byte [] imagen = imageViewToByte(imageView);

                BaseDeDatos bd = new BaseDeDatos(this, "FotoRecicladora", null, 1);
                SQLiteDatabase db = bd.getWritableDatabase();

                ContentValues foto = new ContentValues();
                foto.put("usuario", logeado);
                foto.put("imagen", imagen);

                db.insert("FotoRecicladora", null, foto);
                Toast.makeText(this, "Imagen subida correctamente", Toast.LENGTH_LONG).show();
                db.close();
            } catch (Exception e) { }
        }

        if(requestCode == 2)
        {

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
/*
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
*/
}