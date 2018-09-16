package reciclapp.reciclapp.Interfaces;

import android.support.design.widget.NavigationView;

import reciclapp.reciclapp.SesionRecicladora.HorarioRecicladora;
import reciclapp.reciclapp.SesionRecicladora.InicioRecicladora;
import reciclapp.reciclapp.SesionRecicladora.ModificarRecicladora;
import reciclapp.reciclapp.SesionRecicladora.UbicacionRecicladora;

public interface InterRecicla extends
        NavigationView.OnNavigationItemSelectedListener,
        HorarioRecicladora.OnFragmentInteractionListener,
        InicioRecicladora.OnFragmentInteractionListener
{ }