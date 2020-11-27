package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String loginURL = "http://192.168.0.79/scripts/checkLogin.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonEntrar, buttonCriarConta;
    FloatingActionButton buttonPasswordVisibility;
    EditText editLogin, editSenha;

    boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();

        buttonEntrar = findViewById(R.id.buttonEntrar);
        editLogin = findViewById(R.id.editLogin);
        editSenha = findViewById(R.id.editSenha);
        buttonCriarConta = findViewById(R.id.buttonCriarConta);
        buttonPasswordVisibility = findViewById(R.id.buttonLoginPasswordVisibility);

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

        buttonCriarConta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                buttonCriarConta.setEnabled(false);
                Intent telaRegistro = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(telaRegistro);
                finish();
            }
        });

        buttonPasswordVisibility.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!passwordVisible){
                    editSenha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else{
                    editSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordVisible = !passwordVisible;
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
                    Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    if(!erro){
                        buttonEntrar.setEnabled(false);
                        int tipoUsuario = jsonObject.getInt("tipo");
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

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}