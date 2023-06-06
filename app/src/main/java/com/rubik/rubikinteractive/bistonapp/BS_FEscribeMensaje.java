package com.rubik.rubikinteractive.bistonapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.rubik.rubikinteractive.bistonapp.Entidades.cl_mensaje;
import com.rubik.rubikinteractive.bistonapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BS_FEscribeMensaje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BS_FEscribeMensaje extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout ll;
    EditText edt_asunto, edt_mensaje;
    CardView btn_enviarmsj, btn_volver;
    String asunto = "", mensaje = "";
    ProgressDialog prgDialog;
    RequestQueue request;
    StringRequest strRequest;
    cl_mensaje valmsj;

    public BS_FEscribeMensaje() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ll = (LinearLayout)inflater.inflate(R.layout.fragment_escribemensaje, container, false);

        return ll;

    }


}