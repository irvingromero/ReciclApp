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

public class RegistroRecicladora extends Fragment {

    private View vista;
    private Button registrar;
    private ImageButton back;
    private EditText campoUsuario, campoCorreo, campoContra, campoConfirContra,
            campoNombre, campoTelefono, campoCalle, campoCalle2, campoColonia, campoNumeroInt;

    public RegistroRecicladora() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_registro_recicladora, container, false);

        campoUsuario = vista.findViewById(R.id.etUsuario_registroRecicla);
        campoCorreo = vista.findViewById(R.id.etCorreo_registroRecicla);
        campoContra = vista.findViewById(R.id.etContra_registroRecicla);
        campoConfirContra = vista.findViewById(R.id.etConfirContra_registroRecicla);
        campoNombre = vista.findViewById(R.id.etNombreNegocio_registroRecicla);
        campoTelefono = vista.findViewById(R.id.etTelefono_registroRecicla);
        campoCalle = vista.findViewById(R.id.etCalle_registroRecicla);
        campoCalle2 = vista.findViewById(R.id.etCalle2_registroRecicla);
        campoColonia = vista.findViewById(R.id.etColonia_registroRecicla);
        campoNumeroInt = vista.findViewById(R.id.etNumeroInt_registroRecicla);

        registrar = vista.findViewById(R.id.btnRegistrar_registroRecicla);
        registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registro();
            }
        });

        back = vista.findViewById(R.id.btnAtras_registroRecicla);
        back.setOnClickListener(new View.OnClickListener()
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
        String confirContra = campoConfirContra.getText().toString();
        String nombre = campoNombre.getText().toString();
        String telefono = campoTelefono.getText().toString();
        String calle = campoCalle.getText().toString();
        String calle2 = campoCalle2.getText().toString();
        String colonia = campoColonia.getText().toString();
        String numeroInt = campoNumeroInt.getText().toString();

        if(!usuario.isEmpty() && !correo.isEmpty() && !contra.isEmpty() && !confirContra.isEmpty() && !nombre.isEmpty() &&
           !telefono.isEmpty() && !calle.isEmpty() && !colonia.isEmpty() && !numeroInt.isEmpty())
        {
            boolean noExiste = validaUsuario(usuario);
            boolean contraCorrecta = validaContra(contra, confirContra);

            switch (noExiste + "-" + contraCorrecta)
            {
                case "true-true":
                    if(calle2.isEmpty())
                    {
                        calle2 = "";
                    }
                    ContentValues cv = new ContentValues();
                    cv.put("usuario", usuario);
                    cv.put("correo", correo);
                    cv.put("contra", contra);
                    cv.put("telefono", telefono);
                    cv.put("calle", calle);
                    cv.put("calle2", calle2);
                    cv.put("colonia", colonia);
                    cv.put("numeroInt", numeroInt);

                    BaseDeDatos bd = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
                    SQLiteDatabase basededatos = bd.getWritableDatabase();
                    basededatos.insert("Recicladoras", null, cv);
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
            Toast.makeText(getContext(), "Debes llenar todos los campos requeridos", Toast.LENGTH_LONG).show();
        }
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

    private boolean validaContra(String contra, String confContra)
    {
        boolean correcta = false;

        if(contra.equals(confContra))
        {
            correcta = true;
        }
        return correcta;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}