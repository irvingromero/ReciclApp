package reciclapp.reciclapp.Inicio;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class DatosRecicladora extends Fragment
{
    private View vista;
    private boolean usuarioRegistrado = false;
    private String nombreRecicladora, usuarioRecicla;
    private ImageButton back;

    private ImageView foto;
    private TextView ratingRecicladora;
    private EditText campoNombre, campoCorreo, campoTelefono, campoCalle1, campoCalle2, campoColonia, campoNumeroInt;

    private RatingBar puntuar;
    private RecyclerView lista;
    protected ArrayList<String> listaDatos;
    private Button btnPuntuar;

    public DatosRecicladora() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        nombreRecicladora = extras.getString("recicladora");

        vista = inflater.inflate(R.layout.fragment_datos_recicladora, container, false);

        campoNombre = vista.findViewById(R.id.etNombre_datosRecicladora);
        campoCorreo = vista.findViewById(R.id.etMostrarCorreo_datosRecicladora);
        campoTelefono = vista.findViewById(R.id.etMostrarTelefono_datosRecicladora);
        campoCalle1 = vista.findViewById(R.id.etMostrarCalle1_datosRecicladora);
        campoCalle2 = vista.findViewById(R.id.etMostrarCalle2_datosRecicladora);
        campoColonia = vista.findViewById(R.id.etMostrarColonia_datosRecicla);
        campoNumeroInt = vista.findViewById(R.id.etMostrarNumueroInt_datosRecicla);

        BaseDeDatos baseDeDatos = new BaseDeDatos(getContext(), "Recicladoras", null, 1);
        SQLiteDatabase bdRecicladora = baseDeDatos.getWritableDatabase();
        Cursor consultaRecicla = bdRecicladora.rawQuery("select usuario, correo, telefono, calle, calle2, colonia, numeroInt from Recicladoras where nombre='" +nombreRecicladora + "'", null);
        if (consultaRecicla.moveToFirst())
        {
            usuarioRecicla = consultaRecicla.getString(0);
            String correo = consultaRecicla.getString(1);
            String telefono = consultaRecicla.getString(2).toString();
            String calle = consultaRecicla.getString(3);
            String calle2 = consultaRecicla.getString(4);
            String colonia = consultaRecicla.getString(5);
            String numeroInt = consultaRecicla.getString(6).toString();

            campoNombre.setText(nombreRecicladora);
            campoCorreo.setText(correo);
            campoTelefono.setText(telefono);
            campoCalle1.setText(calle);
            campoCalle2.setText(calle2);
            campoColonia.setText(colonia);
            campoNumeroInt.setText(numeroInt);
        }
        bdRecicladora.close();

        lista = vista.findViewById(R.id.rvMateriales_datosRecicla);
        lista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listaDatos = new ArrayList<String>();

        mostrarMateriales();

        ratingRecicladora = vista.findViewById(R.id.tvPuntuacion_datosRecicla);
        cargarPuntucacion();

        puntuar = vista.findViewById(R.id.rbPuntuar_datosRecicladora);
        btnPuntuar = vista.findViewById(R.id.btnPuntuar_datosRecicladora);
        btnPuntuar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(usuarioRegistrado == true)
                {
                }
                else
                {
                    new AlertDialog.Builder(getContext()).setMessage("Debes registrarte o iniciar sesion para puntuar.")
                            .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) { }
                            }).show();

                    puntuar.setRating(0);
                }
            }
        });

        back = vista.findViewById(R.id.btnAtras_datosRecicladora);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return vista;
    }

    private void mostrarMateriales()
    {
        ////  LLENAR EL ARRAY CON LOS MATERIALES DE LA RECICLADORA SELECCIONADA  ////
        BaseDeDatos baseDeDatos = new BaseDeDatos(getContext(), "Materiales", null, 1);
        SQLiteDatabase bdMateirlaes = baseDeDatos.getWritableDatabase();
        Cursor consultaMateriales = bdMateirlaes.rawQuery("select material, precio, unidad from Materiales where usuario ='" +usuarioRecicla + "'", null);
        if (consultaMateriales.moveToFirst())
        {
            String material, precio, unidad;
            do{
                material = consultaMateriales.getString(0);
                precio = consultaMateriales.getString(1);
                unidad = consultaMateriales.getString(2);
                listaDatos.add("Material:"+material+"\nPrecio:"+precio+"\nUnidad:"+unidad);
            }while(consultaMateriales.moveToNext());
        }
        else
        {
            listaDatos.add("Sin materiales agregados");
        }
        DatosRecicladora.AdapterDatos adapter = new DatosRecicladora.AdapterDatos(listaDatos);
        lista.setAdapter(adapter);
        bdMateirlaes.close();
    }

    private void cargarPuntucacion()
    {
        BaseDeDatos baseDeDatos = new BaseDeDatos(getContext(), "Puntuacion", null, 1);
        SQLiteDatabase bd = baseDeDatos.getWritableDatabase();
        Cursor consultaPuntos = bd.rawQuery("select total from Puntuacion where usuario ='" +usuarioRecicla + "'", null);
        if (consultaPuntos.moveToFirst())
        {
            String total = String.valueOf(consultaPuntos.getDouble(0));
            ratingRecicladora.setText(total);
        }
        else
        {
            ratingRecicladora.setText("0.0");
        }
        bd.close();
    }

    public class AdapterDatos extends RecyclerView.Adapter<DatosRecicladora.AdapterDatos.ViewHolderDatos> {
        ArrayList<String> listaDatos;

        public AdapterDatos(ArrayList<String> listaDatos) {
            this.listaDatos = listaDatos;
        }

        @NonNull
        @Override
        public DatosRecicladora.AdapterDatos.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_material, null, false);
            return new DatosRecicladora.AdapterDatos.ViewHolderDatos(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DatosRecicladora.AdapterDatos.ViewHolderDatos holder, int position) {
            holder.asignarDatos(listaDatos.get(position));
        }

        @Override
        public int getItemCount() {
            return listaDatos.size();
        }




        public class ViewHolderDatos extends RecyclerView.ViewHolder
        {
            TextView dato;
            String material;
            double precio;
            String unidad;

            public ViewHolderDatos(final View itemView) {
                super(itemView);
                dato = itemView.findViewById(R.id.tvMaterial_sesionReci);
            }

            public void asignarDatos(String s){
                dato.setText(s);
            }
        }
    }
}