package reciclapp.reciclapp.SesionRecicladora;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class InicioRecicladora extends Fragment
{
    private String logeado;
    private View vista;
    private RecyclerView lista;
    ArrayList<String> listaDatos;
    private RatingBar estrellas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        logeado = args.getString("usuario");

        vista = inflater.inflate(R.layout.fragment_inicio_recicladora, container, false);
        estrellas = vista.findViewById(R.id.rbPuntuacion_sesionRecicla);
        estrellas.setEnabled(false);

        lista = vista.findViewById(R.id.listaMateriales_sesionRecicla);
        lista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listaDatos = new ArrayList<String>();

        mostrarMateriales();
        return vista;
    }

    private void mostrarMateriales()
    {
        ////  LLENAR EL ARRAY CON LOS MATERIALES QUE REGISTRO ////
        BaseDeDatos baseDeDatos = new BaseDeDatos(getContext(), "Materiales", null, 1);
        SQLiteDatabase bdMateirlaes = baseDeDatos.getWritableDatabase();
        Cursor consultaMateriales = bdMateirlaes.rawQuery("select material, precio, unidad from Materiales where usuario ='" + logeado + "'", null);
        if (consultaMateriales.moveToFirst())
        {
            String material, precio, unidad;
            do{
                material = consultaMateriales.getString(0);
                precio = consultaMateriales.getString(1);
                unidad = consultaMateriales.getString(2);
                listaDatos.add("Material: "+material+"\nPrecio: "+precio+"\nUnidad: "+unidad);
            }while(consultaMateriales.moveToNext());
        }
        else
        {
            listaDatos.add("Sin materiales agregados");
        }
        AdapterDatos adapter = new AdapterDatos(listaDatos);
        lista.setAdapter(adapter);
        bdMateirlaes.close();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



    public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {
        ArrayList<String> listaDatos;

        public AdapterDatos(ArrayList<String> listaDatos) {
            this.listaDatos = listaDatos;
        }

        @NonNull
        @Override
        public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_material, null, false);
            return new ViewHolderDatos(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
            holder.asignarDatos(listaDatos.get(position));
        }

        @Override
        public int getItemCount() {
            return listaDatos.size();
        }

        public class ViewHolderDatos extends RecyclerView.ViewHolder {
            TextView dato;
            public ViewHolderDatos(View itemView) {
                super(itemView);
                dato = itemView.findViewById(R.id.tvMaterial_sesionReci);
            }

            public void asignarDatos(String s){
                dato.setText(s);
            }
        }
    }
}