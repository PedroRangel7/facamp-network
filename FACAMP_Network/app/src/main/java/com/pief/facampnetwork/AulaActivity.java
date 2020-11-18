package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AulaActivity extends AppCompatActivity {

    private int idUsuario;
    private Button buttonPaginaTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula);

        buttonPaginaTutor = findViewById(R.id.buttonPaginaTutor);

        String materia = getIntent().getStringExtra("MATERIA");
        String descricao = getIntent().getStringExtra("DESCRICAO");
        String data = getIntent().getStringExtra("DATA");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        TextView materiaAula = findViewById(R.id.textMateria);
        TextView descricaoAula = findViewById(R.id.textDescricaoAula);
        TextView dataAula = findViewById(R.id.textDataAula);
        TextView precoAula = findViewById(R.id.textPrecoAula);

        materiaAula.setText(materia);
        descricaoAula.setText(descricao);
        dataAula.setText(data);
        precoAula.setText(Utilities.formatPrice(getIntent().getDoubleExtra("PRECO", 0)));

        buttonPaginaTutor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent telaUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                telaUsuario.putExtra("ID_USUARIO", idUsuario);
                startActivity(telaUsuario);
            }
        });
    }
}