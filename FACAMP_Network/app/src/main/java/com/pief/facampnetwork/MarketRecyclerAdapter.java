package com.pief.facampnetwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        View view = layoutInflater.inflate(R.layout.market_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject produto = produtos.get(position);
        try {
            holder.nome.setText(produto.getString("nome"));
            holder.preco.setText("R$" + produto.getString("preco"));
            holder.descricao.setText(produto.getString("descricao"));
            byte[] decodedImageString = Base64.decode(produto.getString("imagem"), Base64.DEFAULT);
            Bitmap imagem = BitmapFactory.decodeByteArray(decodedImageString, 0, decodedImageString.length);
            holder.foto.setImageBitmap(imagem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView foto;
        public TextView nome, descricao, preco;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.foto);
            nome = itemView.findViewById(R.id.nome);
            descricao = itemView.findViewById(R.id.descricao);
            preco = itemView.findViewById(R.id.preco);
        }

        public TextView getNome(){
            return nome;
        }
    }
}
