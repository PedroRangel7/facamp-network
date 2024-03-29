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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdicionarAulaActivity extends AppCompatActivity {

    String adicionarAulaURL = "http://192.168.0.79/scripts/addAula.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonAdicionar;
    EditText editMateria, editPreco, editData, editDescricao;
    String dataTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_aula);

        requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();

        buttonAdicionar = findViewById(R.id.buttonAdicionarAula);
        editMateria = findViewById(R.id.editMateria);
        editPreco = findViewById(R.id.editPrecoAula);
        editData = findViewById(R.id.editDataAula);
        editDescricao = findViewById(R.id.editDescricaoAula);

        Utilities.criarDateTimePicker(editData, AdicionarAulaActivity.this);

        buttonAdicionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText[] camposObrigatorios = {editData, editPreco, editMateria};
                boolean validado = Utilities.checarCamposObrigatorios(camposObrigatorios);
                if(validado){
                    validado = Utilities.checarData(editData);
                    if(validado){
                        buttonAdicionar.setEnabled(false);
                        adicionarAula();
                    }
                }
            }
        });
    }

    private void adicionarAula(){
        stringRequest = new StringRequest(Request.Method.POST, adicionarAulaURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.v("AddClass", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    if(!erro){
                        Intent telaPrincipal = new Intent(AdicionarAulaActivity.this, MainActivity.class);
                        startActivity(telaPrincipal);
                        finish();
                    }
                }catch(Exception e){
                    Log.v("AddClass", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("AddClass", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("materia", editMateria.getText().toString());
                params.put("preco", editPreco.getText().toString());
                params.put("dataAula", MainActivity.lastSelectedDateTime);
                params.put("descricao", editDescricao.getText().toString());
                params.put("idUsuario", String.valueOf(MainActivity.getIDSessao()));
                return params;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}