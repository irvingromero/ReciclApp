package reciclapp.reciclapp.SesionRecicladora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import reciclapp.reciclapp.R;

public class ModificarRecicladora extends Fragment {

    private View vista;
    private ImageButton atras;

    public ModificarRecicladora() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_modificar_recicladora, container, false);



        return vista;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}