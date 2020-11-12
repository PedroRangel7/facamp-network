package com.pief.facampnetwork.ui.market;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.pief.facampnetwork.R;
import com.pief.facampnetwork.RecyclerAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MarketFragment extends Fragment {

    //String url = "http://192.168.0.79/scripts/getProdutos.php";

    //StringRequest stringRequest;
    //RequestQueue requestQueue;

    //RecyclerView recyclerView;
    //RecyclerAdapter recyclerAdapter;

    //TextView id, nome, preco, descricao, idUsuario, id2, nome2, preco2, descricao2, idUsuario2;

    private MarketViewModel marketViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        marketViewModel =
                new ViewModelProvider(this).get(MarketViewModel.class);
        View root = inflater.inflate(R.layout.fragment_market, container, false);
        final TextView textView = root.findViewById(R.id.text_market);
        marketViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //requestQueue = Volley.newRequestQueue(getActivity());
        //preencherProdutos(root);

        return root;
    }

    /*private void preencherProdutos(View view){
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.v("LogLogin", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(erro){
                        Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        id = (TextView)view.findViewById(R.id.idTextView);
                        nome = (TextView)view.findViewById(R.id.nomeTextView);
                        preco = (TextView)view.findViewById(R.id.precoTextView);
                        descricao = (TextView)view.findViewById(R.id.descricaoTextView);
                        idUsuario = (TextView)view.findViewById(R.id.idUsuarioTextView);
                        id2 = (TextView)view.findViewById(R.id.idTextView2);
                        nome2 = (TextView)view.findViewById(R.id.nomeTextView2);
                        preco2 = (TextView)view.findViewById(R.id.precoTextView2);
                        descricao2 = (TextView)view.findViewById(R.id.descricaoTextView2);
                        idUsuario2 = (TextView)view.findViewById(R.id.idUsuarioTextView2);



                        Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_LONG).show();

                        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.market_recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();

                        recyclerView.setAdapter(recyclerAdapter);



                        JSONObject produto1 = jsonObject.getJSONObject("0");

                        id.setText(produto1.getString("id"));
                        nome.setText(produto1.getString("nome"));
                        preco.setText(produto1.getString("preco"));
                        descricao.setText(produto1.getString("descricao"));
                        idUsuario.setText(produto1.getString("idUsuario"));

                        JSONObject produto2 = jsonObject.getJSONObject("1");

                        id2.setText(produto2.getString("id"));
                        nome2.setText(produto2.getString("nome"));
                        preco2.setText(produto2.getString("preco"));
                        descricao2.setText(produto2.getString("descricao"));
                        idUsuario2.setText(produto2.getString("idUsuario"));

                    }
                }catch(Exception e){
                    Log.v("LogLogin", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("LogLogin", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }*/
}