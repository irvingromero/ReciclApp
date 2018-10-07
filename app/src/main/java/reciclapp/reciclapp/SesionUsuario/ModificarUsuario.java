package reciclapp.reciclapp.SesionUsuario;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class ModificarUsuario extends Fragment
{
    private View vista;
    private ImageButton back;
    private EditText campoCorreo, campoContra, campoConfirmaContra;
    private Button actualizar;
    private String usuario;

    public ModificarUsuario() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        usuario = extras.getString("usuario");

        vista = inflater.inflate(R.layout.fragment_modificar_usuario, container, false);

        campoCorreo = vista.findViewById(R.id.etCorreo_modificarUsuario);
        campoContra = vista.findViewById(R.id.etContra_modificarUsuario);
        campoConfirmaContra = vista.findViewById(R.id.etConfirmaContra_modificarUsuario);

        actualizar = vista.findViewById(R.id.btnActualizar_modificarUsuario);
        back = vista.findViewById(R.id.btnAtras_modificarUsuario);

        cargarDatosUsuario();
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void cargarDatosUsuario()
    {
        BaseDeDatos bd = new BaseDeDatos(getContext(), "Usuarios", null, 1);
        SQLiteDatabase flujo = bd.getReadableDatabase();
        Cursor cursor = flujo.rawQuery("select correo, contra from Usuarios where usuario ='" +usuario +"'", null);
        if(cursor.moveToFirst())
        {
            String correo = cursor.getString(0);
            String contra = cursor.getString(1);

            campoCorreo.setText(correo);
            campoContra.setText(contra);
            campoConfirmaContra.setText(contra);
        }
        flujo.close();
    }

    private void actualizarDatos()
    {
            //// Valida que todos los campos esten llenos ////
        if(!campoCorreo.getText().toString().isEmpty() && !campoContra.getText().toString().isEmpty()
           && !campoConfirmaContra.getText().toString().isEmpty())
        {
                //// Valida que las contrasenas sean las mismas ////
            if(campoContra.getText().toString().equals(campoConfirmaContra.getText().toString()))
            {
                String correo = campoCorreo.getText().toString();
                String contra = campoContra.getText().toString();

                BaseDeDatos bd = new BaseDeDatos(getContext(), "Usuarios", null, 1);
                SQLiteDatabase modif = bd.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put("correo", correo);
                cv.put("contra", contra);

                modif.update("Usuarios", cv, "usuario="+"'"+ usuario +"'", null);
                modif.close();
                Snackbar.make(vista, "Datos actualizados", Snackbar.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
            else
            {
                Toast.makeText(getContext(), "*Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "*Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}