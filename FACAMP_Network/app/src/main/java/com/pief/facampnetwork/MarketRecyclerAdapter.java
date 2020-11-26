package com.pief.facampnetwork;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarketRecyclerAdapter extends RecyclerView.Adapter<MarketRecyclerAdapter.ViewHolder>{

    List<JSONObject> produtos;

    public MarketRecyclerAdapter(List<JSONObject> produtos){
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_produto, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public void filtrar(ArrayList<JSONObject> produtosFiltrados){
        produtos = produtosFiltrados;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject produto = produtos.get(position);
        try {
            holder.nome.setText(produto.getString("nome"));
            holder.preco.setText(Utilities.formatPrice(produto.getDouble("preco")));
            holder.descricao.setText(produto.getString("descricao"));
            holder.foto.setImageBitmap(Utilities.getBitmap(produto.getString("imagem")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView foto;
        public TextView nome, descricao, preco;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.fotoProduto);
            nome = itemView.findViewById(R.id.nomeProduto);
            descricao = itemView.findViewById(R.id.descricaoProduto);
            preco = itemView.findViewById(R.id.precoProduto);

            Context context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject clickedProduto = produtos.get(getAdapterPosition());
                    final Intent telaProduto;
                    telaProduto = new Intent(context, ProdutoActivity.class);
                    try {
                        telaProduto.putExtra("ID", clickedProduto.getInt("id"));
                        telaProduto.putExtra("NOME", clickedProduto.getString("nome"));
                        telaProduto.putExtra("PRECO", clickedProduto.getDouble("preco"));
                        telaProduto.putExtra("DESCRICAO", clickedProduto.getString("descricao"));

                        byte[] decodedImageString = Base64.decode(clickedProduto.getString("imagem"), Base64.DEFAULT);
                        telaProduto.putExtra("IMAGEM_BYTES", decodedImageString);

                        telaProduto.putExtra("ID_USUARIO", clickedProduto.getInt("idUsuario"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(telaProduto);
                }
            });
        }
    }
}
