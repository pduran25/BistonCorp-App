package com.rubik.rubikinteractive.bistonapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.rubik.rubikinteractive.bistonapp.R;


public class BS_FReagenda extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Re-Agendamiento de Cita");
        return inflater.inflate(R.layout.fragment_freagenda, container, false);
    }
}
