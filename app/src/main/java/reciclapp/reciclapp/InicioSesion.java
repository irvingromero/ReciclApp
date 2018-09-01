package reciclapp.reciclapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                String usuario = campoUsuario.getText().toString();
                Toast.makeText(getContext(), usuario, Toast.LENGTH_SHORT).show();
            }
        });

        registrarse = vista.findViewById(R.id.btnRegistro_inicio);
        registrarse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RegistroUsuario registro = new RegistroUsuario();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.inicioActivity,  registro, "fragment_meters");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                getActivity().setTitle("Registro");
            }
        });



        return vista;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}