package reciclapp.reciclapp.Inicio;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import reciclapp.reciclapp.BaseDeDatos.BaseDeDatos;
import reciclapp.reciclapp.R;

public class DatosRecicladora extends Fragment
{
    private View vista;
    private String nombreRecicladora, usuarioRecicla, usuarioLogeado;
    private boolean yapuntuo = false;

    private ImageView foto;
    private TextView ratingRecicladora;
    private EditText campoNombre, campoCorreo, campoTelefono, campoCalle1, campoCalle2, campoColonia, campoNumeroInt;

    private RatingBar puntuar;
    private RecyclerView lista;
    protected ArrayList<String> listaDatos;
    private Button btnPuntuar;
    private DrawerLayout drawerLayout;

    public DatosRecicladora() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle extras = getArguments();
        nombreRecicladora = extras.getString("recicladora");
        usuarioLogeado = extras.getString("sesion");

        vista = inflater.inflate(R.layout.fragment_datos_recicladora, container, false);

        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

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
            String telefono = consultaRecicla.getString(2);
            String calle = consultaRecicla.getString(3);
            String calle2 = consultaRecicla.getString(4);
            String colonia = consultaRecicla.getString(5);
            String numeroInt = consultaRecicla.getString(6);

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
        listaDatos = new ArrayList<>();

        foto = vista.findViewById(R.id.fotoRecicladora_datosRecicladora);
        cargarFoto();
        mostrarMateriales();

        ratingRecicladora = vista.findViewById(R.id.tvPuntuacion_datosRecicla);

        puntuar = vista.findViewById(R.id.rbPuntuar_datosRecicladora);
        cargarPuntucacion();

        btnPuntuar = vista.findViewById(R.id.btnPuntuar_datosRecicladora);
        btnPuntuar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(usuarioLogeado != null)
                {
                    double puntuacion = puntuar.getRating();
                    if(puntuacion != 0.0)
                    {
                        if(yapuntuo == false)//// La primera vez que el usuario puntua ////
                        {
                            BaseDeDatos bd = new BaseDeDatos(getContext(), "Puntuador", null, 1);
                            SQLiteDatabase flujoPuntuador = bd.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put("usuario", usuarioLogeado);
                            cv.put("puntuaje", puntuacion);
                            cv.put("recicladora", usuarioRecicla);

                            flujoPuntuador.insert("Puntuador", null, cv);
                            flujoPuntuador.close();

                            BaseDeDatos bdPuntuacion = new BaseDeDatos(getContext(), "Puntuacion", null, 1);
                            SQLiteDatabase flujoPuntuacion = bdPuntuacion.getWritableDatabase();
                            Cursor consulta = flujoPuntuacion.rawQuery("select total, contador from Puntuacion where usuarioRecicladora ='"+usuarioRecicla+"'", null);
                            if(consulta.moveToFirst())
                            {
                                double total = consulta.getDouble(0);
                                int contadorbd = consulta.getInt(1);

                                contadorbd ++;
                                double nuevoTotal = total + puntuacion;

                                ContentValues modificar = new ContentValues();
                                modificar.put("total", nuevoTotal);
                                modificar.put("contador", contadorbd);

                                flujoPuntuacion.update("Puntuacion", modificar, "usuarioRecicladora="+"'"+usuarioRecicla +"'", null);
                                flujoPuntuacion.close();
                            }
                            else //// Si la recicladora no cuenta con un puntuaje aun ////
                            {
                                int contador = 1;
                                ContentValues cvR = new ContentValues();
                                cvR.put("usuarioRecicladora", usuarioRecicla);
                                cvR.put("total", puntuacion);
                                cvR.put("contador", contador);

                                flujoPuntuacion.insert("Puntuacion", null, cvR);
                                flujoPuntuacion.close();
                            }
                            cargarPuntucacion();
                            Snackbar.make(vista, "Gracias por dejar su puntuacion!", Snackbar.LENGTH_LONG).show();
                        }
                        else //// El usuario ya puntuo, se actualizara el puntuaje que dio ////
                        {
                            BaseDeDatos db = new BaseDeDatos(getContext(), "Puntuador", null, 1);
                            SQLiteDatabase flujo = db.getWritableDatabase();
                            Cursor c = flujo.rawQuery("select puntuaje from Puntuador where usuario ='"+usuarioLogeado+"'", null);
                            if (c.moveToFirst())
                            {
                                double puntuajeViejoUsuario = c.getDouble(0);
                                double diferencia;
                                boolean suma;

                                if(puntuajeViejoUsuario != puntuacion)
                                {
                                    if(puntuajeViejoUsuario < puntuacion)
                                    {
                                        diferencia = -(puntuajeViejoUsuario - puntuacion);
                                        suma = true;
                                    }
                                    else
                                    {
                                        diferencia = -(puntuacion - puntuajeViejoUsuario);
                                        suma = false;
                                    }
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("puntuaje", puntuacion);
                                    flujo.update("Puntuador", contentValues, "usuario="+"'"+usuarioLogeado +"'", null);


                                    BaseDeDatos baseDeDatos = new BaseDeDatos(getContext(), "Puntuacion", null, 1);
                                    SQLiteDatabase f = baseDeDatos.getWritableDatabase();
                                    Cursor a = f.rawQuery("select total from Puntuacion where usuarioRecicladora ='"+usuarioRecicla+"'", null);
                                    if (a.moveToFirst())
                                    {
                                        double total = a.getDouble(0);
                                        if(suma == true)
                                        {
                                            total = total + diferencia;
                                        }
                                        else
                                        {
                                            total = total - diferencia;
                                        }

                                        ContentValues cvPuntuacion = new ContentValues();
                                        cvPuntuacion.put("total", total);
                                        f.update("Puntuacion", cvPuntuacion, "usuarioRecicladora="+"'"+usuarioRecicla +"'", null);

                                        cargarPuntucacion();
                                        Snackbar.make(vista, "Puntuacion actualizada", Snackbar.LENGTH_LONG).show();
                                    }
                                    f.close();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "No hay cambios", Toast.LENGTH_SHORT).show();
                                }
                            }
                            flujo.close();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Debes seleccionar una puntuacion", Toast.LENGTH_SHORT).show();
                    }
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

        ImageButton back = vista.findViewById(R.id.btnAtras_datosRecicladora);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                drawerLayout.setDrawerLockMode(drawerLayout.LOCK_MODE_UNLOCKED);
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }
        });

        return vista;
    }

    private void cargarFoto()
    {
        BaseDeDatos baseDeDatos = new BaseDeDatos(getContext(), "FotoRecicladora", null, 1);
        SQLiteDatabase ft = baseDeDatos.getWritableDatabase();
        Cursor consultaFoto = ft.rawQuery("select imagen from FotoRecicladora where usuario ='" + usuarioRecicla + "'", null);
        if (consultaFoto.moveToFirst())
        {
            //// CASTEA EL ARREGLO DE BYTES A BITMAP Y LUEGO A IMAGEVIEW ////
            byte [] arregloImagen = consultaFoto.getBlob(0);
            Bitmap fotoBitmap = arreglobyteToBitmap(arregloImagen);
            foto.setImageBitmap(fotoBitmap);
        }
        ft.close();
    }
    private Bitmap arreglobyteToBitmap(byte [] bytes)
    {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bmp;
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
        Cursor consultaPuntos = bd.rawQuery("select total, contador from Puntuacion where usuarioRecicladora ='" +usuarioRecicla + "'", null);
        if (consultaPuntos.moveToFirst())
        {
            double total = consultaPuntos.getDouble(0);
            int contador = consultaPuntos.getInt(1);
            double promedio = total / contador;

            BigDecimal promedioCorto = new BigDecimal(promedio);
            promedioCorto = promedioCorto.setScale(1, RoundingMode.HALF_UP);
            promedioCorto.doubleValue();

            ratingRecicladora.setText(promedioCorto.toString());
        }
        else
        {
            ratingRecicladora.setText("0.0");
        }
        bd.close();

        BaseDeDatos db = new BaseDeDatos(getContext(), "Puntuador", null, 1);
        SQLiteDatabase flujo = db.getWritableDatabase();
        Cursor c = flujo.rawQuery("select puntuaje, recicladora from Puntuador where usuario ='"+usuarioLogeado+"'", null);
        if (c.moveToFirst())
        {
            double puntuaje = c.getDouble(0);
            String recicladoraPuntuada = c.getString(1);

            if(recicladoraPuntuada.equals(usuarioRecicla))
            {
                puntuar.setRating((float) puntuaje);
                yapuntuo = true;
            }
        }
        flujo.close();
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