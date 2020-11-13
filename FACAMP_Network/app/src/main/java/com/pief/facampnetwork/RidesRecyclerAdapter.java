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

public class RidesRecyclerAdapter extends RecyclerView.Adapter<RidesRecyclerAdapter.ViewHolder>{

    List<JSONObject> caronas;

    public RidesRecyclerAdapter(List<JSONObject> caronas){
        this.caronas = caronas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ride_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return caronas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject carona = caronas.get(position);
        try {
            holder.saida.setText(carona.getString("saida"));
            holder.destino.setText(carona.getString("destino"));
            holder.preco.setText("R$" + carona.getString("preco"));
            holder.dataCarona.setText(carona.getString("dataCarona"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView saida, destino, preco, dataCarona;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            saida = itemView.findViewById(R.id.valorSaida);
            destino = itemView.findViewById(R.id.valorDestino);
            preco = itemView.findViewById(R.id.precoCarona);
            dataCarona = itemView.findViewById(R.id.dataCarona);
        }
    }
}