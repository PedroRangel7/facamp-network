package com.pief.facampnetwork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProdutoActivity extends AppCompatActivity {

    private boolean isOwner = false;
    private int id;
    private int idUsuario;

    private EditText nomeProduto, precoProduto, descricaoProduto;
    private ImageView fotoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        Button buttonPaginaVendedor = findViewById(R.id.buttonPaginaVendedor);
        FloatingActionButton buttonDeletar = findViewById(R.id.buttonDeleteProduto);

        id = getIntent().getIntExtra("ID", -1);
        String nome = getIntent().getStringExtra("NOME");
        String descricao = getIntent().getStringExtra("DESCRICAO");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        nomeProduto = findViewById(R.id.textNomeProduto);
        precoProduto = findViewById(R.id.textPrecoProduto);
        descricaoProduto = findViewById(R.id.textDescricaoProduto);
        fotoProduto = findViewById(R.id.fotoPaginaProduto);

        nomeProduto.setText(nome);
        precoProduto.setText(Utilities.formatPrice(getIntent().getDoubleExtra("PRECO", 0)));
        descricaoProduto.setText(descricao);
        fotoProduto.setImageBitmap(Utilities.getBitmap(getIntent().getByteArrayExtra("IMAGEM_BYTES")));

        if(idUsuario == MainActivity.getIDSessao())
            isOwner = true;

        if(isOwner){
            adicionarListeners();
            buttonPaginaVendedor.setVisibility(View.GONE);
        }
        else{
            View[] views = {nomeProduto, precoProduto, descricaoProduto, fotoProduto};
            Utilities.blockViewInputs(views);

            buttonPaginaVendedor.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    buttonPaginaVendedor.setEnabled(false);
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
                    Utilities.showConfirmacaoDelete("produto", id, ProdutoActivity.this);
                }
            });
        }
    }

    private void adicionarListeners(){
        Utilities.adicionarTextChangeListener(nomeProduto, "produto", "nome", id, this);
        Utilities.adicionarTextChangeListener(precoProduto, "produto", "preco", id, this);
        Utilities.adicionarTextChangeListener(descricaoProduto, "produto", "descricao", id, this);
        Utilities.adicionarImageViewChangeListener(fotoProduto, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utilities.onActivityResultImageChange(requestCode, resultCode, data, fotoProduto, "produto", "imagem", id);
    }
}