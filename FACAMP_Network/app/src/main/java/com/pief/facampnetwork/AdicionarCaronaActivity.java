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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdicionarCaronaActivity extends AppCompatActivity {

    String adicionarCaronaURL = "http://192.168.0.79/scripts/addCarona.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonAdicionar;
    EditText editSaida, editDestino, editPreco, editPlaca, editData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_carona);

        requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();

        buttonAdicionar = findViewById(R.id.buttonAdicionarCarona);
        editSaida = findViewById(R.id.editSaida);
        editDestino = findViewById(R.id.editDestino);
        editPreco = findViewById(R.id.editPrecoCarona);
        editPlaca = findViewById(R.id.editPlaca);
        editData = findViewById(R.id.editDataCarona);

        Utilities.criarDateTimePicker(editData, AdicionarCaronaActivity.this);

        buttonAdicionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText[] camposObrigatorios = {editData, editPlaca, editPreco, editDestino, editSaida};
                boolean validado = Utilities.checarCamposAleatorios(camposObrigatorios);
                if(validado){
                    buttonAdicionar.setEnabled(false);
                    adicionarCarona();
                }
            }
        });
    }

    private void adicionarCarona(){
        stringRequest = new StringRequest(Request.Method.POST, adicionarCaronaURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.v("AddRide", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    if(!erro){
                        Intent telaPrincipal = new Intent(AdicionarCaronaActivity.this, MainActivity.class);
                        startActivity(telaPrincipal);
                        finish();
                    }
                }catch(Exception e){
                    Log.v("AddRide", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("AddRide", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("saida", editSaida.getText().toString());
                params.put("destino", editDestino.getText().toString());
                params.put("preco", editPreco.getText().toString());
                params.put("placa", editPlaca.getText().toString());
                params.put("dataCarona", MainActivity.lastSelectedDateTime);
                params.put("idUsuario", String.valueOf(MainActivity.getIDSessao()));
                return params;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}