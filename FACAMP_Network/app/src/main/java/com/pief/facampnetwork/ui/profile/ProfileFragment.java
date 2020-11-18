package com.pief.facampnetwork.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pief.facampnetwork.MainActivity;
import com.pief.facampnetwork.R;
import com.pief.facampnetwork.Singleton;
import com.pief.facampnetwork.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String getUsuarioURL = "http://192.168.0.79/scripts/getUsuario.php";
    String editUsuarioURL = "http://192.168.0.79/scripts/editUsuario.php";

    int id;
    Bitmap bitmap;
    ImageView foto;
    EditText nomeSobrenome, telefone, biografia;
    TextView tipoUsuario;

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        id = MainActivity.getIDSessao();
        foto = root.findViewById(R.id.fotoUsuario);
        nomeSobrenome = root.findViewById(R.id.textNomeSobrenome);
        tipoUsuario = root.findViewById(R.id.textTipoUsuario);
        telefone = root.findViewById(R.id.textTelefone);
        biografia = root.findViewById(R.id.textBiografia);
        requestQueue = Volley.newRequestQueue(getActivity());
        preencherPerfil(root);
        adicionarListeners();

        return root;
    }

    private void preencherPerfil(View view){
        stringRequest = new StringRequest(Request.Method.POST, getUsuarioURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Profile", response);
                Log.i("Profile", "Session ID: " + id);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(!erro){
                        nomeSobrenome.setText(jsonObject.getString("nome"));
                        tipoUsuario.setText(jsonObject.getString("tipoString"));
                        telefone.setText(jsonObject.getString("telefone"));
                        biografia.setText(jsonObject.getString("biografia"));
                        foto.setImageBitmap(Utilities.getBitmap(jsonObject.getString("foto")));

                        switch(MainActivity.getTipoSessao()){
                            case 1:
                                tipoUsuario.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                                break;
                            case 2:
                                tipoUsuario.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                break;
                            default:
                                tipoUsuario.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                break;
                        }
                    }
                }catch(Exception e){
                    Log.e("Profile", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Profile", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("idUsuario", String.valueOf(id));
                return params;
            }
        };

        Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void adicionarListeners(){
        adicionarTextChangeListener(nomeSobrenome, "nome");
        adicionarTextChangeListener(biografia, "biografia");
        adicionarTextChangeListener(telefone, "telefone");

        foto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
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

    private void adicionarTextChangeListener(EditText editText, String campo){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                criarStringRequest(campo, editText.getText().toString());
            }
        });
    }

    private void criarStringRequest(String campo, String valor){
        stringRequest = new StringRequest(Request.Method.POST, editUsuarioURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Profile", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(erro){
                        Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("Profile", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Profile", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("campo", campo);
                params.put("valor", valor);
                return params;
            }
        };

        Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                foto.setImageBitmap(bitmap);
                criarStringRequest("foto", Utilities.bitmapToString(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}