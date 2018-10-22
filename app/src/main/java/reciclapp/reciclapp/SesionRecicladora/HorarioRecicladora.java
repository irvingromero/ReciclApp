package reciclapp.reciclapp.SesionRecicladora;

import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import reciclapp.reciclapp.R;

public class HorarioRecicladora extends Fragment
{
    private View vista;
    private ImageButton atras;
    private String horaCompleta;

    private Switch swLunes, swMartes, swMier, swJue, swVier, swSab, swDom;
    private ImageButton ibLunAbre, ibLunCierra, ibMar, ibMier, ibJue, ibVier, ibSab, ibDom;
    private TextView e, mm;

    private static final String CERO = "0";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    public HorarioRecicladora() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vista = inflater.inflate(R.layout.fragment_horario_recicladora, container, false);

        atras = vista.findViewById(R.id.btnAtras_horarioRecicla);
        e = vista.findViewById(R.id.tvLunesMostrarAbre);
        mm = vista.findViewById(R.id.tvLunesMostrarCierra);

        ibLunCierra = vista.findViewById(R.id.ibLunes_horarioCerrado);

        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibLunAbre = (ImageButton) vista.findViewById(R.id.ibLunes_horarioAbierto);
        ibLunAbre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora();
            }
        });

        return vista;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        atras.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ibLunCierra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora();
                mm.setText(horaCompleta);
            }
        });
    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
/*
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
*/
                //Muestro la hora con el formato deseado
//                e.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                horaCompleta = horaFormateada+":"+minutoFormateado;
                Toast.makeText(getContext(), ""+horaCompleta, Toast.LENGTH_SHORT).show();
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}