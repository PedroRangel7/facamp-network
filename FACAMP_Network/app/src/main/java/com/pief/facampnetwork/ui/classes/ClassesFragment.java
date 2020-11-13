package com.pief.facampnetwork.ui.classes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pief.facampnetwork.AdicionarAulaActivity;
import com.pief.facampnetwork.ClassesRecyclerAdapter;
import com.pief.facampnetwork.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassesFragment extends Fragment {

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String getAulasURL = "http://192.168.0.79/scripts/getAulas.php";
    RecyclerView recyclerView;
    ClassesRecyclerAdapter classesRecyclerAdapter;
    List<JSONObject> aulas;

    FloatingActionButton buttonAdicionarAula;

    private ClassesViewModel classesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        classesViewModel =
                new ViewModelProvider(this).get(ClassesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_classes, container, false);
        classesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        preencherAulas(root);

        buttonAdicionarAula = root.findViewById(R.id.classes_add_button);

        buttonAdicionarAula.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent telaAdicionarAula = new Intent(getActivity(), AdicionarAulaActivity.class);
                startActivity(telaAdicionarAula);
            }
        });

        return root;
    }

    private void preencherAulas(View view){
        stringRequest = new StringRequest(Request.Method.GET, getAulasURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Classes", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    if(!erro){
                        recyclerView = (RecyclerView)view.findViewById(R.id.classes_recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        aulas = new ArrayList<>();

                        for (int i = 0; i < jsonObject.getInt("numeroAulas"); i++){
                            JSONObject aula = jsonObject.getJSONObject(String.valueOf(i));
                            aulas.add(aula);
                        }

                        classesRecyclerAdapter = new ClassesRecyclerAdapter(aulas);
                        recyclerView.setAdapter(classesRecyclerAdapter);
                    }
                }catch(Exception e){
                    Log.e("Classes", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Classes", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}