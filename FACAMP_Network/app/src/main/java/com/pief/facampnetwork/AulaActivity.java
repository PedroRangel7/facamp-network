package com.pief.facampnetwork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AulaActivity extends AppCompatActivity {

    private boolean isOwner = false;
    private int id;
    private int idUsuario;

    private Button buttonPaginaTutor;
    private EditText materiaAula, descricaoAula, dataAula, precoAula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula);

        buttonPaginaTutor = findViewById(R.id.buttonPaginaTutor);

        id = getIntent().getIntExtra("ID", -1);
        String materia = getIntent().getStringExtra("MATERIA");
        String descricao = getIntent().getStringExtra("DESCRICAO");
        String data = getIntent().getStringExtra("DATA");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        materiaAula = findViewById(R.id.textMateria);
        descricaoAula = findViewById(R.id.textDescricaoAula);
        dataAula = findViewById(R.id.textDataAula);
        precoAula = findViewById(R.id.textPrecoAula);

        materiaAula.setText(materia);
        descricaoAula.setText(descricao);
        dataAula.setText(data);
        precoAula.setText(Utilities.formatPrice(getIntent().getDoubleExtra("PRECO", 0)));

        if(idUsuario == MainActivity.getIDSessao())
            isOwner = true;

        if(isOwner){
            adicionarListeners();
            buttonPaginaTutor.setVisibility(View.GONE);
        }
        else{
            View[] views = {materiaAula, descricaoAula, dataAula, precoAula};
            Utilities.blockViewInputs(views);

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

    private void adicionarListeners(){
        Utilities.adicionarTextChangeListener(materiaAula, "aula", "materia", id, this);
        Utilities.adicionarTextChangeListener(descricaoAula, "aula", "descricao", id, this);
        Utilities.adicionarTextChangeListener(dataAula, "aula", "dataAula", id, this);
        Utilities.adicionarTextChangeListener(precoAula, "aula", "preco", id, this);
    }
}