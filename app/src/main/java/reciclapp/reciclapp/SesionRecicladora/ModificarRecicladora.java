package reciclapp.reciclapp.SesionRecicladora;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class ModificarRecicladora extends AppCompatActivity
{
    private String logeado;
    private Button actualizar;
    private ImageButton atras;
    private EditText campoCorreo, campoContra, campoConfirContra, campoNombre, campoTelefono, campoCalle, campoCalle2, campoColonia, campoNumeroInt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");

        campoCorreo = findViewById(R.id.etCorreo_modifRecicla);
        campoContra = findViewById(R.id.etContra_modifRecicla);
        campoConfirContra = findViewById(R.id.etConfirContra_modifRecicla);
        campoNombre = findViewById(R.id.etNombreNegocio_modifRecicla);
        campoTelefono = findViewById(R.id.etTelefono_modifRecicla);
        campoCalle = findViewById(R.id.etCalle_modifRecicla);
        campoCalle2 = findViewById(R.id.etCalle2_modifRecicla);
        campoColonia = findViewById(R.id.etColonia_modifRecicla);
        campoNumeroInt = findViewById(R.id.etNumeroInt_modifRecicla);

        actualizar = findViewById(R.id.btnActualizar_modifRecicla);
        actualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                actualizarDatos();
            }
        });

        atras = findViewById(R.id.btnAtras_modifRecicla);
        atras.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                back();
            }
        });

        cargarDatos();
    }

    private void back()
    {
        Intent intent = new Intent(getApplicationContext(), SesionRecicladora.class);
        intent.putExtra("usuario", logeado);
        startActivity(intent);
        finish();
    }

    private void cargarDatos()
    {
        BaseDeDatos bd = new BaseDeDatos(this, "Recicladoras", null, 1);
        SQLiteDatabase dllReci = bd.getWritableDatabase();
        Cursor consultaRecicla = dllReci.rawQuery
        ("select correo, contra, nombre, telefono, calle, calle2, colonia, numeroInt from Recicladoras where usuario ='" + logeado + "'", null);
        if (consultaRecicla.moveToFirst())
        {
            campoCorreo.setText(consultaRecicla.getString(0));
            campoContra.setText(consultaRecicla.getString(1));

            campoConfirContra.setText(campoContra.getText().toString());

            campoNombre.setText(consultaRecicla.getString(2));
            campoTelefono.setText(consultaRecicla.getString(3));
            campoCalle.setText(consultaRecicla.getString(4));
            campoCalle2.setText(consultaRecicla.getString(5));
            campoColonia.setText(consultaRecicla.getString(6));
            campoNumeroInt.setText(consultaRecicla.getString(7));
        }
        dllReci.close();
    }

    private void actualizarDatos()
    {
        String correo = campoCorreo.getText().toString();
        String contra = campoContra.getText().toString();
        String confirContra = campoConfirContra.getText().toString();
        String nombre = campoNombre.getText().toString();
        String telefono = campoTelefono.getText().toString();
        String calle = campoCalle.getText().toString();
        String calle2 = campoCalle2.getText().toString();
        String colonia = campoColonia.getText().toString();
        String numeroInt = campoNumeroInt.getText().toString();

        if(!correo.isEmpty() && !contra.isEmpty() && !nombre.isEmpty())
        {
            if(contra.equals(confirContra))
            {
                BaseDeDatos bd = new BaseDeDatos(this, "Recicladoras", null, 1);
                SQLiteDatabase modif = bd.getWritableDatabase();

                int numeroint = Integer.parseInt(numeroInt);

                ContentValues modificacion = new ContentValues();
                modificacion.put("correo", correo);
                modificacion.put("contra", contra);
                modificacion.put("nombre", nombre);
                modificacion.put("telefono", telefono);
                modificacion.put("calle", calle);
                modificacion.put("calle2", calle2);
                modificacion.put("colonia", colonia);
                modificacion.put("numeroInt", numeroint);

                modif.update("Recicladoras", modificacion, "usuario="+"'"+ logeado +"'", null);
                modif.close();

                Toast.makeText(this, "Datos actualizados", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ModificarRecicladora.this, SesionRecicladora.class);
                i.putExtra("usuario", logeado);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this, "Campos requeridos\ncorreo*\ncontraseña*\nnombre*", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }
}