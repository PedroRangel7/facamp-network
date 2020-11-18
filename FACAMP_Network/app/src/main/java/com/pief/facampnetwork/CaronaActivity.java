package com.pief.facampnetwork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CaronaActivity extends AppCompatActivity {

    private boolean isOwner = false;
    private int id;
    private int idUsuario;

    private Button buttonPaginaMotorista;
    private EditText saidaCarona, destinoCarona, dataCarona, precoCarona, placaCarona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carona);

        buttonPaginaMotorista = findViewById(R.id.buttonPaginaMotorista);

        id = getIntent().getIntExtra("ID", -1);
        String saida = getIntent().getStringExtra("SAIDA");
        String destino = getIntent().getStringExtra("DESTINO");
        String data = getIntent().getStringExtra("DATA");
        String placa = getIntent().getStringExtra("PLACA");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        saidaCarona = findViewById(R.id.textValorSaida);
        destinoCarona = findViewById(R.id.textValorDestino);
        dataCarona = findViewById(R.id.textDataCarona);
        precoCarona = findViewById(R.id.textPrecoCarona);
        placaCarona = findViewById(R.id.textPlaca);

        saidaCarona.setText(saida);
        destinoCarona.setText(destino);
        dataCarona.setText(data);
        precoCarona.setText(Utilities.formatPrice(getIntent().getDoubleExtra("PRECO", 0)));
        placaCarona.setText(placa);

        if(idUsuario == MainActivity.getIDSessao())
            isOwner = true;

        if(isOwner){
            adicionarListeners();
            buttonPaginaMotorista.setVisibility(View.GONE);
        }
        else{
            View[] views = {saidaCarona, destinoCarona, dataCarona, precoCarona, placaCarona};
            Utilities.blockViewInputs(views);

            buttonPaginaMotorista.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent telaUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                    telaUsuario.putExtra("ID_USUARIO", idUsuario);
                    startActivity(telaUsuario);
                }
            });
        }
    }

    private void adicionarListeners(){
        Utilities.adicionarTextChangeListener(saidaCarona, "carona", "saida", id, this);
        Utilities.adicionarTextChangeListener(destinoCarona, "carona", "destino", id, this);
        Utilities.adicionarTextChangeListener(dataCarona, "carona", "dataCarona", id, this);
        Utilities.adicionarTextChangeListener(precoCarona, "carona", "preco", id, this);
        Utilities.adicionarTextChangeListener(placaCarona, "carona", "placa", id, this);
    }
}