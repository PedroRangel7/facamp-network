package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pief.facampnetwork.ui.market.MarketFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdicionarProdutoActivity extends AppCompatActivity {

    String adicionarProdutoURL = "http://192.168.0.79/scripts/addProduto.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonAdicionar;
    EditText editNome, editPreco, editDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_produto);

        requestQueue = Volley.newRequestQueue(this);

        buttonAdicionar = findViewById(R.id.buttonAdicionar);
        editNome = findViewById(R.id.editNome);
        editPreco = findViewById(R.id.editPreco);
        editDescricao = findViewById(R.id.editDescricao);

        buttonAdicionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                adicionarProduto();
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
                    if(erro){
                        Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();
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
                params.put("idUsuario", String.valueOf(MainActivity.getIDSessao()));
                return params;
            }
        };

        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}