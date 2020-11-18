package com.pief.facampnetwork.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pief.facampnetwork.MainActivity;
import com.pief.facampnetwork.R;
import com.pief.facampnetwork.RidesRecyclerAdapter;
import com.pief.facampnetwork.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String getUsuarioURL = "http://192.168.0.79/scripts/getUsuario.php";

    ImageView foto;
    TextView nomeSobrenome, tipoUsuario, telefone, biografia;

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

        foto = root.findViewById(R.id.fotoUsuario);
        nomeSobrenome = root.findViewById(R.id.textNomeSobrenome);
        tipoUsuario = root.findViewById(R.id.textTipoUsuario);
        telefone = root.findViewById(R.id.textTelefone);
        biografia = root.findViewById(R.id.textBiografia);
        requestQueue = Volley.newRequestQueue(getActivity());
        preencherPerfil(root);

        return root;
    }

    private void preencherPerfil(View view){
        stringRequest = new StringRequest(Request.Method.POST, getUsuarioURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Profile", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(!erro){
                        nomeSobrenome.setText(jsonObject.getString("nome"));
                        tipoUsuario.setText(jsonObject.getString("tipoString"));
                        telefone.setText(jsonObject.getString("telefone"));
                        biografia.setText("\"" + jsonObject.getString("biografia") + "\"");
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
                params.put("idUsuario", String.valueOf(MainActivity.getIDSessao()));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}