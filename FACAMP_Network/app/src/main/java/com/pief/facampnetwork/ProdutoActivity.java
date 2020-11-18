package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProdutoActivity extends AppCompatActivity {

    private int idUsuario;
    private Button buttonPaginaVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        buttonPaginaVendedor = findViewById(R.id.buttonPaginaVendedor);

        String nome = getIntent().getStringExtra("NOME");
        String descricao = getIntent().getStringExtra("DESCRICAO");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        TextView nomeProduto = findViewById(R.id.textNomeProduto);
        TextView precoProduto = findViewById(R.id.textPrecoProduto);
        TextView descricaoProduto = findViewById(R.id.textDescricaoProduto);
        ImageView fotoProduto = findViewById(R.id.fotoPaginaProduto);

        nomeProduto.setText(nome);
        precoProduto.setText(Utilities.formatPrice(getIntent().getDoubleExtra("PRECO", 0)));
        descricaoProduto.setText(descricao);
        fotoProduto.setImageBitmap(Utilities.getBitmap(getIntent().getByteArrayExtra("IMAGEM_BYTES")));

        buttonPaginaVendedor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent telaUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                telaUsuario.putExtra("ID_USUARIO", idUsuario);
                startActivity(telaUsuario);
            }
        });
    }
}