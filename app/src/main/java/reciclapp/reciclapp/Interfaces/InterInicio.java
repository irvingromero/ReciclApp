package reciclapp.reciclapp.Interfaces;


import reciclapp.reciclapp.Inicio.InicioSesion;
import reciclapp.reciclapp.Registro.RegistroRecicladora;
import reciclapp.reciclapp.Registro.RegistroUsuario;

public interface InterInicio extends
        InicioSesion.OnFragmentInteractionListener,
        RegistroUsuario.OnFragmentInteractionListener,
        RegistroRecicladora.OnFragmentInteractionListener
        {
}