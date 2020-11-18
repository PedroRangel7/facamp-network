package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaronaActivity extends AppCompatActivity {

    private int idUsuario;
    private Button buttonPaginaMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carona);

        buttonPaginaMotorista = findViewById(R.id.buttonPaginaMotorista);

        String saida = getIntent().getStringExtra("SAIDA");
        String destino = getIntent().getStringExtra("DESTINO");
        String data = getIntent().getStringExtra("DATA");
        String placa = getIntent().getStringExtra("PLACA");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        TextView saidaCarona = findViewById(R.id.textValorSaida);
        TextView destinoCarona = findViewById(R.id.textValorDestino);
        TextView dataCarona = findViewById(R.id.textDataCarona);
        TextView precoCarona = findViewById(R.id.textPrecoCarona);
        TextView placaCarona = findViewById(R.id.textPlaca);

        saidaCarona.setText(saida);
        destinoCarona.setText(destino);
        dataCarona.setText(data);
        precoCarona.setText(Utilities.formatPrice(getIntent().getDoubleExtra("PRECO", 0)));
        placaCarona.setText(placa);

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