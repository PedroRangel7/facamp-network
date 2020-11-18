package com.pief.facampnetwork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ProdutoActivity extends AppCompatActivity {

    private boolean isOwner = false;
    private int id;
    private int idUsuario;
    private Button buttonPaginaVendedor;

    private EditText nomeProduto, precoProduto, descricaoProduto;
    private ImageView fotoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        buttonPaginaVendedor = findViewById(R.id.buttonPaginaVendedor);

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

        buttonPaginaVendedor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent telaUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                telaUsuario.putExtra("ID_USUARIO", idUsuario);
                startActivity(telaUsuario);
            }
        });

        if(isOwner){
            adicionarListeners();
            buttonPaginaVendedor.setVisibility(View.GONE);
        }
        else{
            View[] views = {nomeProduto, precoProduto, descricaoProduto, fotoProduto};
            Utilities.blockViewInputs(views);
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