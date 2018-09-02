package reciclapp.reciclapp.Registro;

import android.content.ContentValues;
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
            boolean noExiste = validaUsuario(usuario);
            boolean contraCorrecta = validaContra(contra, confContra);

            switch (noExiste + "-" + contraCorrecta)
            {
                case "true-true":
                    ContentValues cv = new ContentValues();
                    cv.put("usuario", usuario);
                    cv.put("correo", correo);
                    cv.put("contra", contra);

                    BaseDeDatos bd = new BaseDeDatos(getContext(), "Usuarios", null , 1);
                    SQLiteDatabase basededatos = bd.getWritableDatabase();
                    basededatos.insert("Usuarios", null, cv);
                    basededatos.close();

                    getActivity().getSupportFragmentManager().popBackStack();
                    Snackbar.make(vista, "Registro completo", Snackbar.LENGTH_LONG).show();
                    break;

                case "false-true":
                    Toast.makeText(getContext(), "*Usuario ya registrado, porfavor elige otro", Toast.LENGTH_LONG).show();
                    break;
                case "true-false":
                    Toast.makeText(getContext(), "*La contraseña no coinside", Toast.LENGTH_LONG).show();
                    break;
                case "false-false":
                    Toast.makeText(getContext(), "*Usuario ya registrado, porfavor elige otro\n*La contraseña no coinside", Toast.LENGTH_LONG).show();
                    break;
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
        boolean existe = true;
        BaseDeDatos basededatos = new BaseDeDatos(getContext(), "Usuarios", null , 1);
        SQLiteDatabase bd = basededatos.getWritableDatabase();

        Cursor consultaUsuario = bd.rawQuery("select usuario from Usuarios where usuario ='"+usuario+"'",null);
        if(consultaUsuario.moveToFirst())
        {
            existe = false;
        }
        bd.close();

        BaseDeDatos basededatos2 = new BaseDeDatos(getContext(), "Recicladoras", null , 1);
        SQLiteDatabase bd2 = basededatos2.getWritableDatabase();
        Cursor consultaRecicla = bd2.rawQuery("select usuario from Recicladoras where usuario ='"+usuario+"'",null);
        if(consultaRecicla.moveToFirst())
        {
            existe = false;
        }
        bd2.close();
        return existe;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}