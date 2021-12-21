package com.vetapp.ui.home.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vetapp.R;
import com.vetapp.data.datasource.pet.PetDataSource;
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
        PetDataSource.getPetImage(model, new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if(task.isSuccessful()) {
                    byte[] im = task.getResult();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);

                    model.setImage(bitmap);
                    holder.ivImage.setImageBitmap(model.getImage());
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetDataSource.deletePet(model);
            }
        });

    }



    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvName,tvCategory,tvRace;
        private ImageView ivImage;
        private Button btnDelete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.card_pet_name);
            tvCategory = itemView.findViewById(R.id.card_pet_category);
            tvRace = itemView.findViewById(R.id.card_pet_race);
            ivImage = itemView.findViewById(R.id.card_pet_image);
            btnDelete = itemView.findViewById(R.id.card_pet_delete_btn);
        }
    }
}
