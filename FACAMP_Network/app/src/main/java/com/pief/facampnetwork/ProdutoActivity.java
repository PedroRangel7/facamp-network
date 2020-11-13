package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProdutoActivity extends AppCompatActivity {

    private int idUsuario;
    private Button buttonPaginaVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        buttonPaginaVendedor = findViewById(R.id.buttonVendedorProduto);

        String nome = getIntent().getStringExtra("NOME");
        double preco = getIntent().getDoubleExtra("PRECO", 0);
        String descricao = getIntent().getStringExtra("DESCRICAO");
        byte[] imagemBytes = getIntent().getByteArrayExtra("IMAGEM_BYTES");
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        TextView nomeProduto = findViewById(R.id.textNomeProduto);
        TextView precoProduto = findViewById(R.id.textPrecoProduto);
        TextView descricaoProduto = findViewById(R.id.textDescricaoProduto);
        ImageView fotoProduto = findViewById(R.id.fotoPaginaProduto);

        String precoString = "R$" + String.format("%.2f", preco);
        precoString = precoString.replace(".", ",");
        Bitmap imagem = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);

        nomeProduto.setText(nome);
        precoProduto.setText(precoString);
        descricaoProduto.setText(descricao);
        fotoProduto.setImageBitmap(imagem);

        buttonPaginaVendedor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // ENTRAR NA TELA DO VENDEDOR
            }
        });
    }
}