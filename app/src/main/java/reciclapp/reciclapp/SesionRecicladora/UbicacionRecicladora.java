package reciclapp.reciclapp.SesionRecicladora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import reciclapp.reciclapp.R;

public class UbicacionRecicladora extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mapa;
    private String logeado;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa_ubicacionRecicla);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_ubicacionReci);
        setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.ubicacion_ubicacionrecicladora, menu);
        return true;
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
    public void onMapReady(GoogleMap googleMap)
    {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(UbicacionRecicladora.this, SesionRecicladora.class);
        i.putExtra("usuario", logeado);
        startActivity(i);
        finish();
    }
}