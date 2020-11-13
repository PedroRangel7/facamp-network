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

public class ClassesRecyclerAdapter extends RecyclerView.Adapter<ClassesRecyclerAdapter.ViewHolder>{

    List<JSONObject> aulas;

    public ClassesRecyclerAdapter(List<JSONObject> aulas){
        this.aulas = aulas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.class_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return aulas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject aula = aulas.get(position);
        try {
            holder.materia.setText(aula.getString("materia"));
            holder.descricao.setText(aula.getString("descricao"));
            holder.preco.setText("R$" + aula.getString("preco"));
            holder.dataAula.setText(aula.getString("dataAula"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView materia, descricao, preco, dataAula;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            materia = itemView.findViewById(R.id.valorMateria);
            descricao = itemView.findViewById(R.id.descricaoAula);
            preco = itemView.findViewById(R.id.precoAula);
            dataAula = itemView.findViewById(R.id.dataAula);
        }
    }
}