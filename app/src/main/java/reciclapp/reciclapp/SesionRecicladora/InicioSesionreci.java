package reciclapp.reciclapp.SesionRecicladora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reciclapp.reciclapp.R;

public class InicioSesionreci extends Fragment {

    private View vista;

    public InicioSesionreci() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_inicio_sesioneci, container, false);


        return vista;
    }

}