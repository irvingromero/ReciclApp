package reciclapp.reciclapp.Inicio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import reciclapp.reciclapp.R;

public class DatosRecicladora extends Fragment
{
    private View vista;
    private String recicladora;
    private ImageButton back;

    public DatosRecicladora() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        recicladora = extras.getString("recicladora");

        vista = inflater.inflate(R.layout.fragment_datos_recicladora, container, false);
        back = vista.findViewById(R.id.btnAtras_datosRecicladora);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return vista;
    }

}