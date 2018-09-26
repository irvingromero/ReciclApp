package reciclapp.reciclapp.SesionRecicladora;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class ModificarEliminarMaterial extends Fragment
{
    private String logeado, accion;
    private View vista;
    private ImageButton back;
    private TextView titulo;
    private EditText campo_material, campo_precio;

    private RecyclerView lista;
    ArrayList<String> listaDatos;

    public ModificarEliminarMaterial() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        logeado = args.getString("usuario");
        accion = args.getString("accion");

        vista = inflater.inflate(R.layout.fragment_modificar_eliminar_material, container, false);

        titulo = vista.findViewById(R.id.tvNombre_modificarEliminar);
        if(accion.equals("modificando"))
        {
            titulo.setText("Modificar material");
        }
        else
        {
            titulo.setText("Eliminar material");
        }

        lista = vista.findViewById(R.id.rvMateriales_modificarEliminar);
        lista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listaDatos = new ArrayList<String>();
        mostrarMateriales();

        back = vista.findViewById(R.id.btnAtras_modificarEliminar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().onBackPressed();
            }
        });

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
                listaDatos.add("Material:"+material+"\nPrecio:"+precio+"\nUnidad:"+unidad);
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

    private void Modificar(final String m, final double p)
    {
        final String precioViejo = String.valueOf(Double.valueOf(p));

        AlertDialog.Builder ventanita = new AlertDialog.Builder(getContext());
        ventanita.setTitle("Actualize los datos del material");
        //// MOSTRAR SPINNER /////
        final Spinner sp = new Spinner(getContext());
        String[] unidades = new String[] {"Unidad:", "Toneladas", "Kilogramo", "Gramo", "Libra","Litro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, unidades);
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adapter);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        campo_material = new EditText(getContext());
        campo_precio = new EditText(getContext());
        campo_material.setInputType(InputType.TYPE_CLASS_TEXT);
        campo_precio.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        campo_precio.setRawInputType(Configuration.KEYBOARD_QWERTY);
        campo_material.setHint("Material");
        campo_precio.setHint("Precio");

        campo_material.setText(m);
        campo_precio.setText(Double.toString(p));

        linearLayout.addView(campo_material);
        linearLayout.addView(campo_precio);
        linearLayout.addView(sp);
        ventanita.setView(linearLayout);

        ventanita.setPositiveButton("Modificar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String material = campo_material.getText().toString();
                String validarPrecio = campo_precio.getText().toString();
                //// VALIDAR QUE LOS CAMPOS NO ESTEN VACIOS /////
                if(!material.isEmpty() && !validarPrecio.isEmpty())
                {
                    boolean numeroValido = false;
                    double precio = 0;
                    try{
                        precio = Double.parseDouble(campo_precio.getText().toString());

                        numeroValido = true;
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Numero no valido", Toast.LENGTH_SHORT).show();
                    }
                    if(numeroValido == true)
                    {
                        //// VALIDA QUE LOS DATOS A INGRESAR NO ESTEN YA DADOS DE ALTA /////
                        BaseDeDatos bd = new BaseDeDatos(getContext(), "Materiales", null , 1);
                        SQLiteDatabase dllMaterial = bd.getWritableDatabase();
                        Cursor consultaRecicla = dllMaterial.rawQuery("select material, precio from Materiales where material ='"+material+"' and  precio = '"+precio+"'",null);
                        if(consultaRecicla.moveToFirst())
                        {
                            Toast.makeText(getContext(), "Material y precio ya registrado", Toast.LENGTH_SHORT).show();
                            dllMaterial.close();
                        }
                        else
                        {
                            dllMaterial.close();

                            String unidad = sp.getSelectedItem().toString();

                            if(unidad.equals("Unidad:") == true)
                            {
                                Toast.makeText(getContext(), "Debes seleccionar una unidad", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                BaseDeDatos baseDatos = new BaseDeDatos(getContext(), "Materiales", null , 1);
                                SQLiteDatabase flujoDatos = baseDatos.getWritableDatabase();

                                ContentValues cv = new ContentValues();
                                cv.put("material", material);
                                cv.put("precio", precio);
                                cv.put("unidad", unidad);

                                String[] condiciones = new String[]{m, precioViejo};
                                flujoDatos.update("Materiales", cv,  "material=? AND precio=?", condiciones);
                                flujoDatos.close();
                                Snackbar.make(vista, "Material modificado", Snackbar.LENGTH_LONG).show();

                                getActivity().onBackPressed();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Precio no valido", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Debes llenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ventanita.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }});

        ventanita.show();
    }

    private void eliminar(String material, String precioViejo)
    {
        final String materiaEliminar= material, precioEliminar = precioViejo;

        new AlertDialog.Builder(getContext()).setTitle("¿Desea eliminar "+material+"?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        BaseDeDatos baseDatos = new BaseDeDatos(getContext(), "Materiales", null, 1);
                        SQLiteDatabase eliminar = baseDatos.getWritableDatabase();

                        String[] condiciones = new String[]{materiaEliminar, precioEliminar};
                        eliminar.delete("Materiales", "material=? AND precio=?", condiciones);
                        eliminar.close();

                        Snackbar.make(vista, "Material eliminado", Snackbar.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_delete).show();
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



        public class ViewHolderDatos extends RecyclerView.ViewHolder
        {
            TextView dato;
            String material;
            double precio;
            String unidad;

            public ViewHolderDatos(final View itemView) {
                super(itemView);
                dato = itemView.findViewById(R.id.tvMaterial_sesionReci);

                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //int posicion = getAdapterPosition();
                        String texto = dato.getText().toString();

                        if(!texto.equals("Sin materiales agregados"))
                        {
                            String [] textoPartes = texto.split("\n");
                            //// OBTENER EL MATERIAL ////
                            String [] arregloMaterial = textoPartes[0].split(":");
                            material = arregloMaterial[1].trim();
                            //// OBTENER EL PRECIO ////
                            String [] arregloPrecio = textoPartes[1].split(":");
                            arregloPrecio[1].trim();
                            precio = Double.parseDouble(arregloPrecio[1]);

                            if(accion.equals("modificando"))
                            {
                                Modificar(material, precio);
                            }
                            else
                            {
                                String precioViejo = String.valueOf(Double.valueOf(precio));
                                eliminar(material, precioViejo);
                            }
                        }
                    }
                });
            }

            public void asignarDatos(String s){
                dato.setText(s);
            }
        }
    }

}