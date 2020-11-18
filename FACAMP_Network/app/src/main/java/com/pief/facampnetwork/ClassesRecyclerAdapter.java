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
        View view = layoutInflater.inflate(R.layout.item_aula, parent, false);
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
            holder.preco.setText(Utilities.formatPrice(aula.getDouble("preco")));
            holder.dataAula.setText(aula.getString("dataAula"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView materia, descricao, dataAula, preco;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            materia = itemView.findViewById(R.id.valorMateria);
            descricao = itemView.findViewById(R.id.descricaoAula);
            dataAula = itemView.findViewById(R.id.dataAula);
            preco = itemView.findViewById(R.id.precoAula);

            Context context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject clickedAula = aulas.get(getAdapterPosition());
                    final Intent telaAula;
                    telaAula = new Intent(context, AulaActivity.class);
                    try {
                        telaAula.putExtra("ID", clickedAula.getInt("id"));
                        telaAula.putExtra("MATERIA", clickedAula.getString("materia"));
                        telaAula.putExtra("DESCRICAO", clickedAula.getString("descricao"));
                        telaAula.putExtra("DATA", clickedAula.getString("dataAula"));
                        telaAula.putExtra("PRECO", clickedAula.getDouble("preco"));
                        telaAula.putExtra("ID_USUARIO", clickedAula.getInt("idUsuario"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(telaAula);
                }
            });
        }
    }
}