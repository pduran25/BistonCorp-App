package com.rubik.rubikinteractive.bistonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.rubik.rubikinteractive.bistonapp.R;

public class BS_seltipousuario extends AppCompatActivity {

    CardView btn_cliente;
    CardView btn_aplicador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs_seltipousuario);

        btn_cliente = (CardView) findViewById(R.id.btn_cliente);
        btn_cliente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(BS_seltipousuario.this, BS_LoginUsuario.class);
                startActivity(intent);
                finish();
            }
        });

        btn_aplicador = (CardView) findViewById(R.id.btn_aplicador);
        btn_aplicador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(BS_seltipousuario.this, BS_LoginAplicador.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
