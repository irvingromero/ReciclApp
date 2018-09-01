package reciclapp.reciclapp.Registro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reciclapp.reciclapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroRecicladora extends Fragment {


    public RegistroRecicladora() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro_recicladora, container, false);
    }

}
