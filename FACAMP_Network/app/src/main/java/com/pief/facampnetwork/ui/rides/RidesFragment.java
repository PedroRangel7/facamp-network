package com.pief.facampnetwork.ui.rides;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pief.facampnetwork.AdicionarCaronaActivity;
import com.pief.facampnetwork.R;
import com.pief.facampnetwork.RidesRecyclerAdapter;
import com.pief.facampnetwork.Singleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RidesFragment extends Fragment {

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String getCaronasURL = "http://192.168.0.79/scripts/getCaronas.php";
    RecyclerView recyclerView;
    RidesRecyclerAdapter ridesRecyclerAdapter;
    List<JSONObject> caronas;

    FloatingActionButton buttonAdicionarCarona;

    private RidesViewModel ridesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ridesViewModel =
                new ViewModelProvider(this).get(RidesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rides, container, false);
        ridesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        preencherCaronas(root);

        buttonAdicionarCarona = root.findViewById(R.id.rides_add_button);

        buttonAdicionarCarona.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent telaAdicionarCarona = new Intent(getActivity(), AdicionarCaronaActivity.class);
                startActivity(telaAdicionarCarona);
            }
        });

        return root;
    }

    private void preencherCaronas(View view){
        stringRequest = new StringRequest(Request.Method.GET, getCaronasURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Rides", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(!erro){
                        recyclerView = (RecyclerView)view.findViewById(R.id.rides_recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        caronas = new ArrayList<>();

                        for (int i = 0; i < jsonObject.getInt("numeroCaronas"); i++){
                            JSONObject carona = jsonObject.getJSONObject(String.valueOf(i));
                            caronas.add(carona);
                        }

                        ridesRecyclerAdapter = new RidesRecyclerAdapter(caronas);
                        recyclerView.setAdapter(ridesRecyclerAdapter);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
                        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider));
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("Rides", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Rides", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        Singleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}