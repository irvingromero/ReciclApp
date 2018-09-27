package reciclapp.reciclapp.Interfaces;

import android.support.design.widget.NavigationView;
import com.google.android.gms.maps.OnMapReadyCallback;
import reciclapp.reciclapp.Desarrollador;
import reciclapp.reciclapp.Inicio.MapaInicio;

public interface Inicio_InicioPrincipal extends
        NavigationView.OnNavigationItemSelectedListener,
        Desarrollador.OnFragmentInteractionListener,
        MapaInicio.OnFragmentInteractionListener{
}