package reciclapp.reciclapp.Registro;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class RegistroUsuario extends Fragment {

    private View vista;
    private Button registrar;
    private ImageButton regresar;
    private EditText campoUsuario, campoCorreo, campoContra, campoConfContra;

    public RegistroUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista =  inflater.inflate(R.layout.fragment_registro_usuario, container, false);

        campoUsuario = vista.findViewById(R.id.etUsuario_registroUsuario);
        campoCorreo = vista.findViewById(R.id.etCorreo_registroUsuario);
        campoContra = vista.findViewById(R.id.etContra_registroUsuario);
        campoConfContra = vista.findViewById(R.id.etConfirContra_rUsuario);

        registrar = vista.findViewById(R.id.btnRegistrar_registroUsuario);
        registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registro();
            }
        });

        regresar = vista.findViewById(R.id.btnAtras_registroUsu);
        regresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return vista;
    }

    private void registro()
    {
        String usuario = campoUsuario.getText().toString();
        String correo = campoCorreo.getText().toString();
        String contra = campoContra.getText().toString();
        String confContra = campoConfContra.getText().toString();

        if(!usuario.isEmpty() && !correo.isEmpty() && !contra.isEmpty() && !confContra.isEmpty())
        {
            boolean existe = validaUsuario(usuario);
            boolean contraMal = validaContra(contra, confContra);

            switch (existe + "-" + contraMal)
            {
                case "false-false":
                    Snackbar.make(vista, "Registro completo", Snackbar.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    break;

                case "false-true":

                    break;
                case "true-false":

                case "true-true":

                default:

            }
        }
        else
        {
            Toast.makeText(getContext(), "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validaContra(String contra, String confContra)
    {
        boolean correcta = false;

        if(contra.equals(confContra))
        {
            correcta = true;
        }
        return correcta;
    }

    private boolean validaUsuario(String usuario)
    {
        boolean existe = false;
        BaseDeDatos basededatos = new BaseDeDatos(getContext(), "Usuarios", null , 1);
        SQLiteDatabase bd = basededatos.getWritableDatabase();

        Cursor consultaUsuario = bd.rawQuery("select usuario from Usuarios where usuario ='"+usuario+"'",null);
        if(consultaUsuario.moveToFirst())
        {
            existe = true;
        }
        bd.close();
        return existe;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}