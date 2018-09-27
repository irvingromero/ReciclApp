package reciclapp.reciclapp.Inicio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class MapaInicio extends Fragment implements OnMapReadyCallback
{
    private View vista;
    private GoogleMap mapa;
    private MapView mapView;

    public MapaInicio() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_mapa_inicio, container, false);


        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = vista.findViewById(R.id.mapita);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(getContext());

        boolean permisoActivado;
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setMinZoomPreference(11.0f);

        LatLng mexicali = new LatLng(32.6278100, -115.4544600);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(mexicali));

        mostrarRecicladoras();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            permisoActivado = estadoPermiso();
            if(permisoActivado == false)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else
                {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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



    }

    private void mostrarRecicladoras()
    {
        BaseDeDatos bd = new BaseDeDatos(getContext(), "Ubicacion", null, 1);
        SQLiteDatabase flujo = bd.getWritableDatabase();
        Cursor consulta = flujo.rawQuery("select latitud, longitud from Ubicacion", null);
        if(consulta.moveToFirst())
        {
            Double lat, lon;
            do{
                lat = Double.parseDouble(consulta.getString(0));
                lon = Double.parseDouble(consulta.getString(1));
                mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
            }while(consulta.moveToNext());
        }
        flujo.close();
    }

    private boolean estadoPermiso()
    {
        int resultado = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}