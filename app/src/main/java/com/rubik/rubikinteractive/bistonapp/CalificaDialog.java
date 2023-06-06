package com.rubik.rubikinteractive.bistonapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.rubik.rubikinteractive.bistonapp.R;

public class CalificaDialog extends AppCompatDialogFragment {

    private RatingBar rt_estrellas;
    private CalificaDialogListener listener;
    float rating = 0;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);


        builder.setView(view)
                .setTitle("Calificación Aplicador")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rating = rt_estrellas.getRating();
                        listener.applyCalification(rating);
                    }
                });

        rt_estrellas = view.findViewById(R.id.ratingBar);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            if(context instanceof  CalificaDialogListener)
                listener =(CalificaDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement CalificaDialogListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface CalificaDialogListener{
        void applyCalification(float estrellas);
    }
}
