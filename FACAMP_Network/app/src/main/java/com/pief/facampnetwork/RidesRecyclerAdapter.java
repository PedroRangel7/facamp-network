package com.pief.facampnetwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        View view = layoutInflater.inflate(R.layout.item_carona, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return caronas.size();
    }

    public void filtrar(ArrayList<JSONObject> caronasFiltradas){
        caronas = caronasFiltradas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject carona = caronas.get(position);
        try {
            holder.saida.setText(carona.getString("saida"));
            holder.destino.setText(carona.getString("destino"));
            holder.preco.setText(Utilities.formatPrice(carona.getDouble("preco")));
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

            Context context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject clickedCarona = caronas.get(getAdapterPosition());
                    final Intent telaCarona;
                    telaCarona = new Intent(context, CaronaActivity.class);
                    try {
                        telaCarona.putExtra("ID", clickedCarona.getInt("id"));
                        telaCarona.putExtra("SAIDA", clickedCarona.getString("saida"));
                        telaCarona.putExtra("DESTINO", clickedCarona.getString("destino"));
                        telaCarona.putExtra("DATA", clickedCarona.getString("dataCarona"));
                        telaCarona.putExtra("PRECO", clickedCarona.getDouble("preco"));
                        telaCarona.putExtra("PLACA", clickedCarona.getString("placa"));
                        telaCarona.putExtra("ID_USUARIO", clickedCarona.getInt("idUsuario"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(telaCarona);
                }
            });
        }
    }
}