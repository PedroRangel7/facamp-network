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

public class LoginActivity extends AppCompatActivity {

    String loginURL = "http://192.168.0.79/scripts/checkLogin.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonEntrar;
    EditText editLogin, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        buttonEntrar = findViewById(R.id.buttonEntrar);
        editLogin = findViewById(R.id.editLogin);
        editSenha = findViewById(R.id.editSenha);

        buttonEntrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean validado = true;

                if(editSenha.getText().length() == 0){
                    editSenha.setError("Campo obrigatório.");
                    editSenha.requestFocus();
                    validado = false;
                }

                if(editLogin.getText().length() == 0){
                    editLogin.setError("Campo obrigatório.");
                    editLogin.requestFocus();
                    validado = false;
                }

                if(validado){
                    //Toast.makeText(getApplicationContext(), "Validando dados...", Toast.LENGTH_SHORT).show();
                    validarLogin();
                }
            }
        });
    }

    private void validarLogin(){
        stringRequest = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Login", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(erro){
                        Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        int tipoUsuario = jsonObject.getInt("tipo");
                        switch (tipoUsuario){
                            case 1:
                                Toast.makeText(getApplicationContext(), "Logado como administrador.", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "Logado como moderador.", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Logado como usuário.", Toast.LENGTH_LONG).show();
                                break;
                        }
                        Intent telaPrincipal = new Intent(LoginActivity.this, MainActivity.class);
                        telaPrincipal.putExtra("ID_SESSAO", jsonObject.getInt("id"));
                        telaPrincipal.putExtra("TIPO_SESSAO", tipoUsuario);
                        startActivity(telaPrincipal);
                        finish();
                    }
                }catch(Exception e){
                    Log.e("Login", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Login", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("login", editLogin.getText().toString());
                params.put("senha", editSenha.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}