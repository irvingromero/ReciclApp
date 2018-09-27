package reciclapp.reciclapp.Interfaces;


import reciclapp.reciclapp.InicioDeSesion.InicioSesion;
import reciclapp.reciclapp.Registro.RegistroRecicladora;
import reciclapp.reciclapp.Registro.RegistroUsuario;

public interface InicioDeSesion_Inicio extends
        InicioSesion.OnFragmentInteractionListener,
        RegistroUsuario.OnFragmentInteractionListener,
        RegistroRecicladora.OnFragmentInteractionListener
        {
}