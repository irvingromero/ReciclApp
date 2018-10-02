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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class MapaInicio extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private View vista, infowindow;
    private GoogleMap mapa;
    private MapView mapView;
    private String busqueda;

    public MapaInicio() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        busqueda = extras.getString("material");

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
        mapa.setOnMarkerClickListener(this);

        LatLng mexicali = new LatLng(32.6278100, -115.4544600);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(mexicali));

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


        if(busqueda != null)
        {
            getActivity().setTitle("Buscar: "+busqueda);

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Materiales", null, 1);
            SQLiteDatabase flujo = bd.getWritableDatabase();

            BaseDeDatos bdUbicacion = new BaseDeDatos(getContext(), "Ubicacion", null, 1);
            SQLiteDatabase flujoUbicacion = bdUbicacion.getWritableDatabase();

            Cursor consulta = flujo.rawQuery("select usuario from Materiales where material ='"+busqueda+"'", null);
            if(consulta.moveToFirst())
            {
                String usuario, nombreRecicladora = "";
                Double lat, lon;

                BaseDeDatos bdRecicladora = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
                SQLiteDatabase flujoRecicladora = bdRecicladora.getWritableDatabase();

                do{
                    usuario = consulta.getString(0);

                    Cursor consultaUbicacion = flujoUbicacion.rawQuery("select latitud, longitud from Ubicacion where usuario='"+usuario+"'", null);
                    if(consultaUbicacion.moveToFirst())
                    {
                        Cursor consultaRecicladora = flujoRecicladora.rawQuery("select nombre from Recicladoras where usuario ='"+usuario+"'", null);
                        if(consultaRecicladora.moveToFirst()){
                            nombreRecicladora = consultaRecicladora.getString(0);
                        }

                        lat = Double.parseDouble(consultaUbicacion.getString(0));
                        lon = Double.parseDouble(consultaUbicacion.getString(1));
                        mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(nombreRecicladora));
                    }
                }while(consulta.moveToNext());
                flujoRecicladora.close();
            }
            flujo.close();
            flujoUbicacion.close();
        }
        else
        {
            mostrarRecicladoras();
        }
    }


    private void mostrarRecicladoras()
    {
        BaseDeDatos bd = new BaseDeDatos(getContext(), "Ubicacion", null, 1);
        SQLiteDatabase flujo = bd.getWritableDatabase();

        BaseDeDatos bdMaterial = new BaseDeDatos(getContext(), "Materiales", null, 1);
        SQLiteDatabase mat = bdMaterial.getWritableDatabase();

        Cursor consulta = flujo.rawQuery("select usuario, latitud, longitud from Ubicacion", null);
        if(consulta.moveToFirst())
        {
            String usuario, nombreRecicladora = "";
            Double lat, lon;

            BaseDeDatos bdRecicladora = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
            SQLiteDatabase flujoRecicladora = bdRecicladora.getWritableDatabase();

            do{
                usuario = consulta.getString(0);

                Cursor consultaRecicladora = flujoRecicladora.rawQuery("select nombre from Recicladoras where usuario ='"+usuario+"'", null);
                if(consultaRecicladora.moveToFirst()){
                    nombreRecicladora = consultaRecicladora.getString(0);
                }

                lat = Double.parseDouble(consulta.getString(1));
                lon = Double.parseDouble(consulta.getString(2));
                    //// VALIDA QUE TENGA AGREGADO ALGUN MATERIAL PARA MOSTRARLA EN EL MAPA ////
                Cursor consultaMaterial = mat.rawQuery("select material from Materiales where usuario ='"+usuario+"'", null);
                if(consultaMaterial.moveToFirst())
                {
                    mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(nombreRecicladora));
                }
            }while(consulta.moveToNext());
            flujoRecicladora.close();
        }
        flujo.close();
        mat.close();
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

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        final String nombre = marker.getTitle().toString();

        mapa.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker)
            {
                infowindow = LayoutInflater.from(getContext()).inflate(R.layout.infowindow, null);
                ((TextView)infowindow.findViewById(R.id.tvMostrarNombre_mapaInicio)).setText(nombre);
                return infowindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                infowindow = LayoutInflater.from(getContext()).inflate(R.layout.infowindow, null);
                return infowindow;
            }
        });

        mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                BaseDeDatos bd = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
                SQLiteDatabase reci = bd.getWritableDatabase();

                Cursor consulta = reci.rawQuery("select usuario from Recicladoras where nombre='"+nombre+"'", null);
                if(consulta.moveToFirst())
                {
                    Bundle bundle = new Bundle();
                    DatosRecicladora dr = new DatosRecicladora();
                    bundle.putString("recicladora", nombre);
                    dr.setArguments(bundle);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainInicioPrincipal,  dr, "");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();

                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                }
                reci.close();
            }
        });
        return false;
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