package reciclapp.reciclapp.SesionRecicladora;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class HorarioRecicladora extends Fragment
{
    private String usuario;
    private ImageButton atras;
    private String horas[] = new String[50];
    private boolean cerradoLunes, cerradoMartes, cerradoMier, cerradoJue, cerradoVier, cerradoSabado, cerradoDom;
    private Switch swLunes, swMartes, swMier, swJue, swVier, swSab, swDom;
    private TextView lunes, martes, mier, jueves, viernes, sabado, domingo;
    private Spinner spLunesAbre, spLunesCierra, spMartesAbre, spMartesCierra, spMierAbre, spMierCierra,
    spJueAbre, spJueCierra, spVierAbre, spVierCierra, spSabAbre, spSabCierra, spDomAbre, spDomCierra;
    private Button guardar;

    public HorarioRecicladora() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        usuario = extras.getString("usuario");

        View vista = inflater.inflate(R.layout.fragment_horario_recicladora, container, false);
        atras = vista.findViewById(R.id.btnAtras_horarioRecicla);

        arregloHoras();
        swLunes = vista.findViewById(R.id.swiLunes_horarioReci);
        lunes = vista.findViewById(R.id.tvEstadoLun_horario);
        spLunesAbre = vista.findViewById(R.id.spLunAbre_horario);
        spLunesCierra = vista.findViewById(R.id.spLunCierra_horario);
        ///
        swMartes = vista.findViewById(R.id.swiMartes_horarioReci);
        martes = vista.findViewById(R.id.tvEstadoMartes_horario);
        spMartesAbre = vista.findViewById(R.id.spMartesAbre_horario);
        spMartesCierra = vista.findViewById(R.id.spMartesCierra_horario);
        ///
        swMier = vista.findViewById(R.id.swiMiercoles_horarioReci);
        mier = vista.findViewById(R.id.tvEstadoMiercoles_horario);
        spMierAbre = vista.findViewById(R.id.spMiercolesAbre_horario);
        spMierCierra = vista.findViewById(R.id.spMiercolesCierra_horario);
        ///
        swJue = vista.findViewById(R.id.swiJueves_horarioReci);
        jueves = vista.findViewById(R.id.tvEstadoJueves_horario);
        spJueAbre = vista.findViewById(R.id.spJuevesAbre_horario);
        spJueCierra = vista.findViewById(R.id.spJuevesCierra_horario);
        ///
        swVier = vista.findViewById(R.id.swiViernes_horarioReci);
        viernes = vista.findViewById(R.id.tvEstadoViernes_horario);
        spVierAbre = vista.findViewById(R.id.spViernesAbre_horario);
        spVierCierra = vista.findViewById(R.id.spViernesCierra_horario);
        ///
        swSab = vista.findViewById(R.id.swiSabado_horarioReci);
        sabado = vista.findViewById(R.id.tvEstadoSabado_horario);
        spSabAbre = vista.findViewById(R.id.spSabadoAbre_horario);
        spSabCierra = vista.findViewById(R.id.spSabadoCierra_horario);
        //
        swDom = vista.findViewById(R.id.swiDomingo_horarioReci);
        domingo = vista.findViewById(R.id.tvEstadoDomingo_horario);
        spDomAbre = vista.findViewById(R.id.spDomingoAbre_horario);
        spDomCierra = vista.findViewById(R.id.spDomingoCierra_horario);

        guardar = vista.findViewById(R.id.btnGuardar_horarioReci);
        llenarSpinner();
        cargarHorario();
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

        swLunes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    lunes.setText("Cerrado");
                    spLunesAbre.setEnabled(false);
                    spLunesCierra.setEnabled(false);

                    spLunesAbre.setSelection(0);
                    spLunesCierra.setSelection(0);
                    cerradoLunes = true;
                }else{
                    lunes.setText("Abierto");
                    spLunesAbre.setEnabled(true);
                    spLunesCierra.setEnabled(true);
                    cerradoLunes = false;
                }
            }
        });

        swMartes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    martes.setText("Cerrado");
                    spMartesAbre.setEnabled(false);
                    spMartesCierra.setEnabled(false);
                    spMartesAbre.setSelection(0);
                    spMartesCierra.setSelection(0);
                    cerradoMartes = true;
                }else{
                    martes.setText("Abierto");
                    spMartesAbre.setEnabled(true);
                    spMartesCierra.setEnabled(true);
                    cerradoMartes = false;
                }
            }
        });

        swMier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mier.setText("Cerrado");
                    spMierAbre.setEnabled(false);
                    spMierCierra.setEnabled(false);
                    spMierAbre.setSelection(0);
                    spMierCierra.setSelection(0);
                    cerradoMier = true;
                }else{
                    mier.setText("Abierto");
                    spMierAbre.setEnabled(true);
                    spMierCierra.setEnabled(true);
                    cerradoMier = false;
                }
            }
        });

        swJue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    jueves.setText("Cerrado");
                    spJueAbre.setEnabled(false);
                    spJueCierra.setEnabled(false);
                    spJueAbre.setSelection(0);
                    spJueCierra.setSelection(0);
                    cerradoJue = true;
                }else{
                    jueves.setText("Abierto");
                    spJueAbre.setEnabled(true);
                    spJueCierra.setEnabled(true);
                    cerradoJue = false;
                }
            }
        });

        swVier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    viernes.setText("Cerrado");
                    spVierAbre.setEnabled(false);
                    spVierCierra.setEnabled(false);
                    spVierAbre.setSelection(0);
                    spVierCierra.setSelection(0);
                    cerradoVier = true;
                }else{
                    viernes.setText("Abierto");
                    spVierAbre.setEnabled(true);
                    spVierCierra.setEnabled(true);
                    cerradoVier = false;
                }
            }
        });

        swSab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    sabado.setText("Cerrado");
                    spSabAbre.setEnabled(false);
                    spSabCierra.setEnabled(false);
                    spSabAbre.setSelection(0);
                    spSabCierra.setSelection(0);
                    cerradoSabado = true;
                }else{
                    sabado.setText("Abierto");
                    spSabAbre.setEnabled(true);
                    spSabCierra.setEnabled(true);
                    cerradoSabado = false;
                }
            }
        });

        swDom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    domingo.setText("Cerrado");
                    spDomAbre.setEnabled(false);
                    spDomCierra.setEnabled(false);
                    spDomAbre.setSelection(0);
                    spDomCierra.setSelection(0);
                    cerradoDom = true;
                }else{
                    domingo.setText("Abierto");
                    spDomAbre.setEnabled(true);
                    spDomCierra.setEnabled(true);
                    cerradoDom = false;
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarHorario();
            }
        });
    }

    private void guardarHorario()
    {
        if(cerradoLunes)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Lunes", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Lunes", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spLunesAbre.getSelectedItem().toString());
            cv.put("cierra", spLunesCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Lunes", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Lunes", null, cv);
            basededatos.close();
        }

        if(cerradoMartes)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Martes", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Martes", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spMartesAbre.getSelectedItem().toString());
            cv.put("cierra", spMartesCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Martes", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Martes", null, cv);
            basededatos.close();
        }

        if(cerradoMier)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Miercoles", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Miercoles", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spMierAbre.getSelectedItem().toString());
            cv.put("cierra", spMierCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Miercoles", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Miercoles", null, cv);
            basededatos.close();
        }

        if(cerradoJue)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Jueves", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Jueves", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spJueAbre.getSelectedItem().toString());
            cv.put("cierra", spJueCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Jueves", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Jueves", null, cv);
            basededatos.close();
        }

        if(cerradoVier)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Viernes", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Viernes", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spVierAbre.getSelectedItem().toString());
            cv.put("cierra", spVierCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Viernes", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Viernes", null, cv);
            basededatos.close();
        }

        if(cerradoSabado)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Sabado", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Sabado", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spSabAbre.getSelectedItem().toString());
            cv.put("cierra", spSabCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Sabado", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Sabado", null, cv);
            basededatos.close();
        }

        if(cerradoDom)
        {
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", "Cerrado");
            cv.put("cierra", "Cerrado");

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Domingo", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Domingo", null, cv);
            basededatos.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("usuario", usuario);
            cv.put("abre", spDomAbre.getSelectedItem().toString());
            cv.put("cierra", spDomCierra.getSelectedItem().toString());

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Domingo", null , 1);
            SQLiteDatabase basededatos = bd.getWritableDatabase();
            basededatos.insert("Domingo", null, cv);
            basededatos.close();
        }

        getActivity().onBackPressed();
        Toast.makeText(getContext(), "Horario guardado", Toast.LENGTH_LONG).show();
    }

    private void cargarHorario()
    {
        BaseDeDatos bd = new BaseDeDatos(getContext(), "Lunes", null , 1);
        SQLiteDatabase basededatos = bd.getWritableDatabase();
    }

    private void arregloHoras()
    {
        horas = new String[] {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30",
        "03:00", "03:30", "04:00", "04:30", "05:00", "05:30",
        "06:00", "06:30", "07:00", "07:30", "08:00", "08:30",
        "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30",
        "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
        "22:00", "22:30", "23:00", "23:30"};
    }

    private void llenarSpinner()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, horas);
        spLunesAbre.setAdapter(adapter);
        spLunesCierra.setAdapter(adapter);

        spMartesAbre.setAdapter(adapter);
        spMartesCierra.setAdapter(adapter);

        spMierAbre.setAdapter(adapter);
        spMierCierra.setAdapter(adapter);

        spJueAbre.setAdapter(adapter);
        spJueCierra.setAdapter(adapter);

        spVierAbre.setAdapter(adapter);
        spVierCierra.setAdapter(adapter);

        spSabAbre.setAdapter(adapter);
        spSabCierra.setAdapter(adapter);

        spDomAbre.setAdapter(adapter);
        spDomCierra.setAdapter(adapter);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}