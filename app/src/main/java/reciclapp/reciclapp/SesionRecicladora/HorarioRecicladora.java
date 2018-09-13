package reciclapp.reciclapp.SesionRecicladora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import reciclapp.reciclapp.R;

public class HorarioRecicladora extends AppCompatActivity
{
    private String logeado;
    private ImageButton atras;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_recicladora);

        Bundle extras = getIntent().getExtras();
        logeado = extras.getString("usuario");

        atras = findViewById(R.id.btnAtras_horarioRecicla);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}