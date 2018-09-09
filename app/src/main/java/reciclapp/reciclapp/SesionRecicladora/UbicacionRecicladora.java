package reciclapp.reciclapp.SesionRecicladora;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class UbicacionRecicladora extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap mapa;
    private String logeado;
    private ImageButton atras;

    private double lat, lon;
    private double latYlon[] = new double[2];
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

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setMinZoomPreference(11.0f);

        LatLng mexicali = new LatLng(32.6278100,  -115.4544600);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(mexicali));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //// SOLICITA PERMISO PARA LA UBICACION  ////
            if ((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            {
                mapa.setMyLocationEnabled(true);
            }
            //// SI EL PERMISO NO FUE DADO, VUELVE A PREGUNTAR ////
            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))
            {
                requestPermissions(new String[] {ACCESS_FINE_LOCATION}, 1);
//                mapa.setMyLocationEnabled(true); error
            }
        }
        else{
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

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                if(yaRegistrada == true)
                {

                }
                else
                {
                    MarkerOptions mo = new MarkerOptions().position(new LatLng(lat, lon));
//                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude));
                    mapa.clear();
                    mapa.addMarker(mo);
                    //// SE GUARDAN LAS COORDENADAS DE DONDE SE COLOCÓ EL MARKER DE LA UBICACION DE LA RECI ////

                    //// BANDERA VALIDA QUE SI SE HAYA SELECCIONADO UNA UBICACION ////
                    seleccinado = true;
                }
            }
        });
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