package com.pief.facampnetwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AdicionarProdutoActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    Bitmap bitmap;
    String adicionarProdutoURL = "http://192.168.0.79/scripts/addProduto.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonAdicionar;
    ImageView imagem;
    EditText editNome, editPreco, editDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_produto);

        requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();

        buttonAdicionar = findViewById(R.id.buttonAdicionarProduto);
        imagem = findViewById(R.id.imageViewProduto);
        editNome = findViewById(R.id.editNomeProduto);
        editPreco = findViewById(R.id.editPrecoProduto);
        editDescricao = findViewById(R.id.editDescricaoProduto);

        bitmap = ((BitmapDrawable)imagem.getDrawable()).getBitmap();

        buttonAdicionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                buttonAdicionar.setEnabled(false);
                adicionarProduto();
            }
        });

        imagem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else
                        escolherImagem();
                }
                else
                    escolherImagem();
            }
        });
    }

    private void adicionarProduto(){
        stringRequest = new StringRequest(Request.Method.POST, adicionarProdutoURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.v("AddProduct", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    if(!erro){
                        Intent telaPrincipal = new Intent(AdicionarProdutoActivity.this, MainActivity.class);
                        startActivity(telaPrincipal);
                        finish();
                    }
                }catch(Exception e){
                    Log.v("AddProduct", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("AddProduct", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("nome", editNome.getText().toString());
                params.put("preco", editPreco.getText().toString());
                params.put("descricao", editDescricao.getText().toString());
                params.put("imagem", Utilities.bitmapToString(bitmap));
                params.put("idUsuario", String.valueOf(MainActivity.getIDSessao()));
                return params;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void escolherImagem(){
        Intent escolherImg = new Intent(Intent.ACTION_PICK);
        escolherImg.setType("image/*");
        startActivityForResult(escolherImg, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                escolherImagem();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                imagem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}