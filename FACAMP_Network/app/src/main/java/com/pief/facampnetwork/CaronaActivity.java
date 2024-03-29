package com.pief.facampnetwork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CaronaActivity extends AppCompatActivity {

    private boolean isOwner = false;
    private int id;
    private int idUsuario;

    private EditText saidaCarona, destinoCarona, dataCarona, precoCarona, placaCarona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carona);

        Button buttonPaginaMotorista = findViewById(R.id.buttonPaginaMotorista);
        FloatingActionButton buttonDeletar = findViewById(R.id.buttonDeleteCarona);

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

        data = Utilities.formatarDataString(data);
        Utilities.criarDateTimePicker(dataCarona, CaronaActivity.this);

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
                    buttonPaginaMotorista.setEnabled(false);
                    Intent telaUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                    telaUsuario.putExtra("ID_USUARIO", idUsuario);
                    startActivity(telaUsuario);
                }
            });
        }

        if(MainActivity.getTipoSessao() <= 2 || isOwner){
            buttonDeletar.setVisibility(View.VISIBLE);
            buttonDeletar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Utilities.showConfirmacaoDelete("carona", id, CaronaActivity.this);
                }
            });
        }
    }

    private void adicionarListeners(){
        Utilities.adicionarTextChangeListener(saidaCarona, "carona", "saida", id, this);
        Utilities.adicionarTextChangeListener(destinoCarona, "carona", "destino", id, this);
        Utilities.adicionarDateChangeListener(dataCarona, "carona", "dataCarona", id, this);
        Utilities.adicionarTextChangeListener(precoCarona, "carona", "preco", id, this);
        Utilities.adicionarTextChangeListener(placaCarona, "carona", "placa", id, this);
    }
}