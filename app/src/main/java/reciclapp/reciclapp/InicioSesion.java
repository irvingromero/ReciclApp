package reciclapp.reciclapp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import reciclapp.reciclapp.Registro.RegistroRecicladora;
import reciclapp.reciclapp.Registro.RegistroUsuario;

public class InicioSesion extends Fragment {

    private View vista;
    private Button entrar, registrarse;
    private EditText campoUsuario, campoContra;

    public InicioSesion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        campoUsuario = vista.findViewById(R.id.etUsuario_inicio);
        campoContra = vista.findViewById(R.id.etContra_inicio);

        entrar = vista.findViewById(R.id.btnLogin_inicio);
        entrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
/*
                new AlertDialog.Builder(getActivity())
                        .setTitle("Eliminar item")
                        .setMessage("¿stas seguro de eliminar item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
*/
            }

        });

        registrarse = vista.findViewById(R.id.btnRegistro_inicio);
        registrarse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*
                RegistroUsuario registro = new RegistroUsuario();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.inicioActivity,  registro, "fragment_meters");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                */
                RegistroRecicladora registro = new RegistroRecicladora();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.inicioActivity,  registro, "fragment_meters");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return vista;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}