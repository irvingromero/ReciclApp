package reciclapp.reciclapp.Reciclaje;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reciclapp.reciclapp.R;

public class GuiaReciclaje extends Fragment
{
    private View vista;

    public GuiaReciclaje() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_guia_reciclaje, container, false);

        return vista;
    }

}