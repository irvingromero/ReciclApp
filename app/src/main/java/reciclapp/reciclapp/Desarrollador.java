package reciclapp.reciclapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Desarrollador extends Fragment
{
    private View vista;

    public Desarrollador() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_desarrollador, container, false);


        return vista;
    }

}