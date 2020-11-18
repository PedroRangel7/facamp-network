package com.pief.facampnetwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    Bitmap bitmap;
    String registerURL = "http://192.168.0.79/scripts/registerUsuario.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button buttonRegistrar;
    ImageView foto;
    EditText editLogin, editNome, editSenha, editTelefone, editBiografia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestQueue = Singleton.getInstance(this.getApplicationContext()).getRequestQueue();

        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        foto = findViewById(R.id.imageViewFoto);
        editNome = findViewById(R.id.editNomeRegister);
        editLogin = findViewById(R.id.editLoginRegister);
        editSenha = findViewById(R.id.editSenhaRegister);
        editTelefone = findViewById(R.id.editTelefoneRegister);
        editBiografia = findViewById(R.id.editBiografiaRegister);

        buttonRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean validado = true;

                if(editNome.getText().length() == 0){
                    editNome.setError("Campo obrigatório.");
                    editNome.requestFocus();
                    validado = false;
                }

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
                    registrarUsuario();
                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else
                        escolherFoto();
                }
                else
                    escolherFoto();
            }
        });
    }

    private void registrarUsuario(){
        stringRequest = new StringRequest(Request.Method.POST, registerURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Register", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    Toast.makeText(getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    if(!erro){
                        Intent telaLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(telaLogin);
                        finish();
                    }
                }catch(Exception e){
                    Log.e("Register", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Register", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("nome", editNome.getText().toString());
                params.put("login", editLogin.getText().toString());
                params.put("senha", editSenha.getText().toString());
                params.put("telefone", editTelefone.getText().toString());
                params.put("foto", Utilities.bitmapToString(bitmap));
                params.put("biografia", editBiografia.getText().toString());
                return params;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void escolherFoto(){
        Intent escolherImg = new Intent(Intent.ACTION_PICK);
        escolherImg.setType("image/*");
        startActivityForResult(escolherImg, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                escolherFoto();
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
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}