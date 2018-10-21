package reciclapp.reciclapp.Inicio;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private DrawerLayout drawerLayout;
    private String busqueda, usuarioLogeado, materialPrecio, distancia;
    private Location miUbicacion;

    public MapaInicio() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        busqueda = extras.getString("material");
        usuarioLogeado = extras.getString("sesionUsuario");
        materialPrecio = extras.getString("materialPrecio");
        distancia = extras.getString("distancia");

        vista = inflater.inflate(R.layout.fragment_mapa_inicio, container, false);

        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

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

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(getContext());

        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setMinZoomPreference(11.0f);
        mapa.setOnMarkerClickListener(this);

        permiso();

        LatLng mexicali = new LatLng(32.6278100, -115.4544600);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(mexicali));

        if(busqueda != null)
        {
            busquedaMaterial();

        } else if(materialPrecio != null)
        {
            mejorPrecio();
        }
        else if(distancia != null)
        {
            cernano();
        }else
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

    private void busquedaMaterial()
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

    private void mejorPrecio()
    {
        BaseDeDatos bd = new BaseDeDatos(getContext(), "Materiales", null, 1);
        SQLiteDatabase flujoBdMateriales = bd.getWritableDatabase();

        Cursor consultaMateriales = flujoBdMateriales.rawQuery("select precio from Materiales where material='" + materialPrecio + "'", null);
        if(consultaMateriales.moveToFirst())
        {
            double[] precios = new double[consultaMateriales.getCount()];
            int indicePrecios = 0;

            do{
                double valorPrecio = consultaMateriales.getDouble(0);
                precios[indicePrecios] = valorPrecio;
                indicePrecios++;
            }while(consultaMateriales.moveToNext());
                //// Calcula el valor mayor de los precios ////
            double mayor = 0.0;
            double valor;
            int indice = 0;
            for (int i = 0; i < consultaMateriales.getCount(); i++)
            {
                mayor = precios[indice];

                for (int j = indice + 1; j < consultaMateriales.getCount(); j++)
                {
                    valor = precios[j];

                    if (valor > mayor)
                    {
                        mayor = valor;
                    }
                }
            }
                //// Se saca el usuario de las recicladoras con el material buscado y con el mismo precio ////
            Cursor consultaMaterialesUsuario = flujoBdMateriales.rawQuery("select usuario from Materiales where precio='" + mayor + "' and material='"+ materialPrecio + "'", null);
            if(consultaMaterialesUsuario.moveToFirst())
            {
                BaseDeDatos db = new BaseDeDatos(getContext(), "Ubicacion", null, 1);
                SQLiteDatabase flujo = db.getWritableDatabase();

                BaseDeDatos b = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
                SQLiteDatabase flujoRecicladora = b.getWritableDatabase();

                do {
                    Cursor consultaUbicacion = flujo.rawQuery("select latitud, longitud from Ubicacion where usuario='" + consultaMaterialesUsuario.getString(0) + "'", null);
                    if (consultaUbicacion.moveToFirst())
                    {
                        Cursor consultaRecicladora = flujoRecicladora.rawQuery("select nombre from Recicladoras where usuario='" + consultaMaterialesUsuario.getString(0) + "'", null);
                        if (consultaRecicladora.moveToFirst())
                        {
                            mapa.addMarker(new MarkerOptions().position(new LatLng(consultaUbicacion.getDouble(0), consultaUbicacion.getDouble(1)))
                                    .title(consultaRecicladora.getString(0)))
                                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        }
                    }
                }while (consultaMaterialesUsuario.moveToNext());
                flujo.close();
                flujoRecicladora.close();
            }
        }
        flujoBdMateriales.close();
    }

    private void cernano()
    {
        if(miUbicacion != null)
        {
            BaseDeDatos bd = new BaseDeDatos(getContext(), "Ubicacion", null, 1);
            SQLiteDatabase flujo = bd.getWritableDatabase();

            BaseDeDatos bdMaterial = new BaseDeDatos(getContext(), "Materiales", null, 1);
            SQLiteDatabase mat = bdMaterial.getWritableDatabase();

            int indice = 0;
            LatLng ll;
            LatLng[] ubicaciones = new LatLng[0];

            Cursor consulta = flujo.rawQuery("select usuario, latitud, longitud from Ubicacion", null);
            if (consulta.moveToFirst())
            {
                ubicaciones = new LatLng[consulta.getCount()];
                String usuario, nombreRecicladora = "";
                Double lat, lon;

                BaseDeDatos bdRecicladora = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
                SQLiteDatabase flujoRecicladora = bdRecicladora.getWritableDatabase();

                do {
                    usuario = consulta.getString(0);

                    Cursor consultaRecicladora = flujoRecicladora.rawQuery("select nombre from Recicladoras where usuario ='" + usuario + "'", null);
                    if (consultaRecicladora.moveToFirst()) {
                        nombreRecicladora = consultaRecicladora.getString(0);
                    }

                    lat = Double.parseDouble(consulta.getString(1));
                    lon = Double.parseDouble(consulta.getString(2));

                    ll = new LatLng(lat, lon);
                    ubicaciones[indice] = ll;
                    indice++;

                    //// VALIDA QUE TENGA AGREGADO ALGUN MATERIAL PARA MOSTRARLA EN EL MAPA ////
                    Cursor consultaMaterial = mat.rawQuery("select material from Materiales where usuario ='" + usuario + "'", null);
                    if (consultaMaterial.moveToFirst()) {
                        mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(nombreRecicladora));
                    }

                } while (consulta.moveToNext());
                flujoRecicladora.close();
            }
            flujo.close();
            mat.close();

            LatLng menor = null;
            LatLng valor;
            Location locat = new Location("");
            float distanciaValor;
            float distanciaMenor;
            //// CALCULA CUAL ES LA MENOR LONGITUD DSDE LA UBICACION ////
            menor = ubicaciones[0];
            for (int i = 0; i < ubicaciones.length; i++)
            {
                locat.setLatitude(menor.latitude);
                locat.setLongitude(menor.longitude);
                distanciaMenor = miUbicacion.distanceTo(locat);

                for (int j = i + 1; j < ubicaciones.length; j++)
                {
                    valor = ubicaciones[j];
                    locat.setLatitude(valor.latitude);
                    locat.setLongitude(valor.longitude);
                    distanciaValor = miUbicacion.distanceTo(locat);

                    if (distanciaValor < distanciaMenor) {
                        menor = valor;
                    }
                }
            }
            CameraUpdate camara = CameraUpdateFactory.newLatLngZoom(menor, 14);
            mapa.animateCamera(camara);
        }
        else
        {
            Toast.makeText(getContext(), "Problemas con su ubicacion", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void permiso()
    {
        boolean permisoActivado;

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
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            }
            else
            {
                mapa.setMyLocationEnabled(true);

                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                miUbicacion = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                try {
                    double latitude = miUbicacion.getLatitude();
                    double longitud = miUbicacion.getLongitude();

                LatLng latlog = new LatLng(latitude, longitud);
                mapa.moveCamera(CameraUpdateFactory.newLatLng(latlog));
                }catch (Exception e){}
            }
        }
        else
        {
            mapa.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            miUbicacion = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            try {
                double latitude = miUbicacion.getLatitude();
                double longitud = miUbicacion.getLongitude();

                LatLng latlog = new LatLng(latitude, longitud);
                mapa.moveCamera(CameraUpdateFactory.newLatLng(latlog));
            }catch (Exception e){ }
        }
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
        final String nombre = marker.getTitle();

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
                    bundle.putString("sesion", usuarioLogeado);
                    dr.setArguments(bundle);

                    if(usuarioLogeado == null)
                    {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainInicioPrincipal, dr, "");
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                        drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    }
                    else
                    {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainSesionUsuario, dr, "");
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                        drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    }
                }
                reci.close();
            }
        });
        return false;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mapa.setMyLocationEnabled(true);
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}