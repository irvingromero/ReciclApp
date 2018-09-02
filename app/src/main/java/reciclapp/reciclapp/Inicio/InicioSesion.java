package reciclapp.reciclapp.Inicio;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;
import reciclapp.reciclapp.Registro.RegistroRecicladora;
import reciclapp.reciclapp.Registro.RegistroUsuario;
import reciclapp.reciclapp.SesionRecicladora.SesionRecicladora;

public class InicioSesion extends Fragment {

    private View vista;
    private Button entrar, registrarse;
    private EditText campoUsuario, campoContra;

    public InicioSesion() { }

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
                iniciarSesion();
            }
        });

        registrarse = vista.findViewById(R.id.btnRegistro_inicio);
        registrarse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CharSequence[] values = {"Usuario", "Recicladora"};
                AlertDialog.Builder ventana = new AlertDialog.Builder(getActivity());
                ventana.setTitle("¿Como quieres registrarte?");
                ventana.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item)
                    {
                        switch (item)
                        {
                            case 0:
                                RegistroUsuario registroUsuario = new RegistroUsuario();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.inicioActivity,  registroUsuario, "fragment_meters");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft.addToBackStack(null);
                                ft.commit();
                                break;

                                case 1:
                                    RegistroRecicladora registroReci = new RegistroRecicladora();
                                    FragmentTransaction fragtrac = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragtrac.replace(R.id.inicioActivity,  registroReci, "fragment_meters");
                                    fragtrac.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    fragtrac.addToBackStack(null);
                                    fragtrac.commit();
                                    break;
                        }
                        dialogInterface.dismiss();
                    }
                });
                ventana.create().show();
            }
        });
        return vista;
    }

    private void iniciarSesion()
    {
        String usuario = campoUsuario.getText().toString();
        String contra = campoContra.getText().toString();

        if (!usuario.isEmpty() && !contra.isEmpty())
        {
            boolean bandera = false;

            BaseDeDatos bdReci = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
            SQLiteDatabase dllReci = bdReci.getWritableDatabase();
            Cursor consultaRecicla = dllReci.rawQuery("select usuario, contra from Recicladoras where usuario ='" + usuario + "' and contra ='" + contra + "'", null);
            if (consultaRecicla.moveToFirst())
            {
                bandera = true;
                Intent i = new Intent(getActivity(), SesionRecicladora.class);
                i.putExtra("usuario", usuario);
                getActivity().startActivity(i);
                getActivity().finish();
            }
            dllReci.close();

            BaseDeDatos bd = new BaseDeDatos(getContext(), "Usuarios", null, 1);
            SQLiteDatabase dllUsu = bd.getWritableDatabase();
            Cursor consultaUsuario = dllUsu.rawQuery("select usuario, contra from Usuarios where usuario ='" + usuario + "' and contra ='" + contra + "'", null);
            if (consultaUsuario.moveToFirst())
            {
                bandera = true;
//                Intent i = new Intent(getActivity(), ActivitySesion.class);
//                i.putExtra("usuario", usuario);
//                getActivity().startActivity(i);
//                getActivity().finish();
            }
            dllUsu.close();

            if(bandera == false)
            {
                Toast.makeText(getContext(), "El usuario o contraseña son incorrectos", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Debes llenar los campos", Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}