package com.vetapp.ui.home.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vetapp.R;
import com.vetapp.data.models.pet.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.Viewholder> {

    private Context context;
    private List<Pet> petList;

    public PetAdapter(Context context, List<Pet> petList){
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_cardview_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetAdapter.Viewholder holder, int position) {
        Pet model = petList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvCategory.setText(model.getCategory());
        holder.tvRace.setText(model.getRace());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvName,tvCategory,tvRace;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.card_pet_name);
            tvCategory = itemView.findViewById(R.id.card_pet_category);
            tvRace = itemView.findViewById(R.id.card_pet_race);
        }
    }
}
