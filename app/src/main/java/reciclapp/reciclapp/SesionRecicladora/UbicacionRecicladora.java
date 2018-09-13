package reciclapp.reciclapp.SesionRecicladora;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class UbicacionRecicladora extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap mapa;
    private String logeado;
    private ImageButton atras;

    private double lat, lon;
    private boolean yaRegistrada, seleccinado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa_ubicacionRecicla);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.tb_ubicacionReci);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        atras = findViewById(R.id.btnAtras_ubicacionRecicla);
        atras.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                regresar();
            }
        });
        
        cargarUbicacion();
    }

    private void cargarUbicacion()
    {
        BaseDeDatos baseDeDatos = new BaseDeDatos(this, "Ubicacion", null, 1);
        SQLiteDatabase flujo = baseDeDatos.getWritableDatabase();
        Cursor ubicacionActual = flujo.rawQuery("select latitud, longitud from Ubicacion where usuario='" + logeado + "'", null);
        if (ubicacionActual.moveToFirst())
        {
            Toast.makeText(this, "Modificar ubicacion", Toast.LENGTH_LONG).show();

            //// PUEDE USARSE EL ARREGLO PARA GUARDAR LAS NUEVAS POSICIONES ////
            lat = Double.parseDouble(ubicacionActual.getString(0));
            lon = Double.parseDouble(ubicacionActual.getString(1));
            yaRegistrada = true;
        }
        else
        {
            Toast.makeText(this,"Selecciona la ubicacion del negocio",Toast.LENGTH_LONG).show();
        }
        flujo.close();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        boolean permisoActivado;
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setMinZoomPreference(11.0f);
        mapa.getUiSettings().setMapToolbarEnabled(false);
        mapa.getUiSettings().setCompassEnabled(true);

        LatLng mexicali = new LatLng(32.6278100,  -115.4544600);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(mexicali));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permisoActivado = estadoPermiso();
            if(permisoActivado == false)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
            else
            {
                mapa.setMyLocationEnabled(true);
            }
        }
        else
        {
            mapa.setMyLocationEnabled(true);
        }

        if(yaRegistrada == true)
        {
                //// CARGA LA UBIACION QUE YA ESTABA REGISTRADA ////
            mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Mi recicladora"));
            LatLng latlog = new LatLng(lat, lon);
            CameraUpdate camara = CameraUpdateFactory.newLatLngZoom(latlog, 15);
            mapa.animateCamera(camara);
        }

        final double latVieja = lat;
        final double lonVieja = lon;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {

                if(yaRegistrada == true)
                {
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude));
                    mapa.clear();
                    mapa.addMarker(new MarkerOptions().position(new LatLng(latVieja, lonVieja)).title("Tu negocio"));
                    mapa.addMarker(marker).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    lat = latLng.latitude;
                    lon = latLng.longitude;
                    seleccinado = true;
                }
                else
                {
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude));
                    mapa.clear();
                    mapa.addMarker(marker);
                    lat = latLng.latitude;
                    lon = latLng.longitude;
                    //// BANDERA VALIDA QUE SI SE HAYA SELECCIONADO UNA UBICACION ////
                    seleccinado = true;
                }
            }
        });
    }

    private boolean estadoPermiso()
    {
        int resultado = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(resultado == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(estadoPermiso())
        {
            mapa.setMyLocationEnabled(true);
        }
        else
        { }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.ubicacion_ubicacionrecicladora, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.seleccion_ubicacionReci:
                switch (seleccinado +"-"+ yaRegistrada)
                {
                    case "true-false":
                        /// insertar ////
                        BaseDeDatos bd = new BaseDeDatos(this, "Ubicacion", null , 1);
                        SQLiteDatabase basededatos = bd.getWritableDatabase();

                        ContentValues cv = new ContentValues();
                        cv.put("usuario", logeado);
                        cv.put("latitud", lat);
                        cv.put("longitud", lon);
                        basededatos.insert("Ubicacion", null, cv);
                        basededatos.close();
                        Toast.makeText(this, "Ubicacion agregada", Toast.LENGTH_LONG).show();

                        Intent ok = new Intent(UbicacionRecicladora.this, SesionRecicladora.class);
                        ok.putExtra("usuario", logeado);
                        startActivity(ok);
                        finish();
                    break;

                    case "true-true":
                        /// modificar
                        BaseDeDatos bdUpdate = new BaseDeDatos(this, "Ubicacion", null, 1);
                        SQLiteDatabase modificando = bdUpdate.getWritableDatabase();

                        ContentValues nuevo = new ContentValues();
                        nuevo.put("latitud", lat);
                        nuevo.put("longitud", lon);
                        modificando.update("Ubicacion", nuevo, "usuario="+ "'" +logeado +"'", null);
                        modificando.close();
                        Toast.makeText(this, "Ubicacion modificada", Toast.LENGTH_LONG).show();

                        Intent okok = new Intent(UbicacionRecicladora.this, SesionRecicladora.class);
                        okok.putExtra("usuario", logeado);
                        startActivity(okok);
                        finish();
                    break;

                    default:
                        Toast.makeText(this, "Debes colocar una ubicacion", Toast.LENGTH_SHORT).show();
                    break;
                }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        regresar();
    }

    private void regresar()
    {
        Intent i = new Intent(UbicacionRecicladora.this, SesionRecicladora.class);
        i.putExtra("usuario", logeado);
        startActivity(i);
        finish();
    }
}