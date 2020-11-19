package com.pief.facampnetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class UsuarioActivity extends AppCompatActivity {


    String getUsuarioURL = "http://192.168.0.79/scripts/getUsuario.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    int idUsuario;
    ImageView foto;
    TextView nomeSobrenome, tipoUsuario, telefone, biografia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        FloatingActionButton buttonDeletar = findViewById(R.id.buttonDeleteUser);

        requestQueue = Volley.newRequestQueue(this);

        foto = findViewById(R.id.fotoUser);
        nomeSobrenome = findViewById(R.id.textNomeUser);
        tipoUsuario = findViewById(R.id.textTipoUser);
        telefone = findViewById(R.id.textTelefoneUser);
        biografia = findViewById(R.id.textBiografiaUser);

        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        preencherPerfil();

        if(MainActivity.getTipoSessao() == 1){
            buttonDeletar.setVisibility(View.VISIBLE);
            buttonDeletar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Utilities.showConfirmacaoDelete("usuario", idUsuario, UsuarioActivity.this);
                }
            });
        }
    }

    private void preencherPerfil(){
        stringRequest = new StringRequest(Request.Method.POST, getUsuarioURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.v("User", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(!erro){
                        nomeSobrenome.setText(jsonObject.getString("nome"));
                        tipoUsuario.setText(jsonObject.getString("tipoString"));
                        telefone.setText(jsonObject.getString("telefone"));
                        biografia.setText("\"" + jsonObject.getString("biografia") + "\"");
                        foto.setImageBitmap(Utilities.getBitmap(jsonObject.getString("foto")));

                        switch(jsonObject.getInt("tipo")){
                            case 1:
                                tipoUsuario.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.administrator));
                                break;
                            case 2:
                                tipoUsuario.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.moderator));
                                break;
                            default:
                                tipoUsuario.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.user));
                                break;
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.v("User", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("User", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", String.valueOf(idUsuario));
                return params;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}