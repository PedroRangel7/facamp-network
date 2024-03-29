package com.pief.facampnetwork.ui.market;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.pief.facampnetwork.AdicionarProdutoActivity;
import com.pief.facampnetwork.R;
import com.pief.facampnetwork.MarketRecyclerAdapter;
import com.pief.facampnetwork.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketFragment extends Fragment {

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String getProdutosURL = "http://192.168.0.79/scripts/getProdutos.php";
    RecyclerView recyclerView;
    MarketRecyclerAdapter marketRecyclerAdapter;
    List<JSONObject> produtos;

    FloatingActionButton buttonAdicionarProduto, buttonAtualizarProdutos;

    private MarketViewModel marketViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        marketViewModel =
                new ViewModelProvider(this).get(MarketViewModel.class);
        View root = inflater.inflate(R.layout.fragment_market, container, false);
        marketViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        preencherProdutos(root);

        buttonAdicionarProduto = root.findViewById(R.id.market_add_button);
        buttonAtualizarProdutos = root.findViewById(R.id.buttonRefreshMarket);

        buttonAdicionarProduto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent telaAdicionarProduto = new Intent(getActivity(), AdicionarProdutoActivity.class);
                startActivity(telaAdicionarProduto);
            }
        });

        buttonAtualizarProdutos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                preencherProdutos(root);
            }
        });

        EditText buscar = root.findViewById(R.id.editSearchMarket);
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String texto = editable.toString().toLowerCase();
                ArrayList<JSONObject> produtosFiltrados = new ArrayList<>();
                for(JSONObject produto : produtos){
                    try {
                        if(produto.getString("nome").toLowerCase().startsWith(texto)){
                            produtosFiltrados.add(produto);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                marketRecyclerAdapter.filtrar(produtosFiltrados);
            }
        });

        return root;
    }

    private void preencherProdutos(View view){
        stringRequest = new StringRequest(Request.Method.GET, getProdutosURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Market", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(!erro){
                        recyclerView = (RecyclerView)view.findViewById(R.id.market_recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        produtos = new ArrayList<>();

                        for (int i = 0; i < jsonObject.getInt("numeroProdutos"); i++){
                            JSONObject produto = jsonObject.getJSONObject(String.valueOf(i));
                            produtos.add(produto);
                        }

                        Collections.reverse(produtos);

                        marketRecyclerAdapter = new MarketRecyclerAdapter(produtos);
                        recyclerView.setAdapter(marketRecyclerAdapter);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
                        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider));
                        recyclerView.addItemDecoration(dividerItemDecoration);
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("Market", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Market", error.getMessage());
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