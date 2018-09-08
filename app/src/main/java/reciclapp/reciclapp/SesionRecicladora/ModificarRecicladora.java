package reciclapp.reciclapp.SesionRecicladora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import reciclapp.reciclapp.R;

public class ModificarRecicladora extends AppCompatActivity
{
    private ImageButton atras;
    private EditText campoCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_recicladora);

        atras = findViewById(R.id.btnAtras_modifRecicla);
        atras.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), SesionRecicladora.class);
                startActivity(intent);
                finish();
            }
        });
    }

}