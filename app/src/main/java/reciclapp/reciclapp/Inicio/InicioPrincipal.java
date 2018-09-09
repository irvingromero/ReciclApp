package reciclapp.reciclapp.Inicio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import reciclapp.reciclapp.InicioDeSesion.Inicio;
import reciclapp.reciclapp.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class InicioPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapita);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sesionRegistro_inicioPrin) {
            Intent intent = new Intent(this, Inicio.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.buscarMaterial_inicioPrin) {

        } else if (id == R.id.mejorPrecio_inicioPrin) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.programador_inicioPrin) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setMinZoomPreference(11.0f);

        LatLng mexicali = new LatLng(32.6278100, -115.4544600);
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

    }
}