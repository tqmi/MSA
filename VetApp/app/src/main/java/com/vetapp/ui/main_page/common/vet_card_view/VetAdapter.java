package com.vetapp.ui.main_page.common.vet_card_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vetapp.R;
import com.vetapp.data.models.vet.Vet;

import java.util.List;

public class VetAdapter extends RecyclerView.Adapter<com.vetapp.ui.main_page.common.vet_card_view.VetAdapter.Viewholder> {

    private Context context;
    private List<Vet> vetList;

    public VetAdapter(Context context, List<Vet> petList) {
        this.context = context;
        this.vetList = petList;
    }

    @NonNull
    @Override
    public com.vetapp.ui.main_page.common.vet_card_view.VetAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_cardview_layout, parent, false);
        return new com.vetapp.ui.main_page.common.vet_card_view.VetAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.vetapp.ui.main_page.common.vet_card_view.VetAdapter.Viewholder holder, int position) {
        Vet model = vetList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvClinicName.setText(model.getClinicName());
        holder.tvAddress.setText(model.getAddress());
//      TODO: add image

//        VetDataSource.getVetImage(model, new OnCompleteListener<byte[]>() {
//            @Override
//            public void onComplete(@NonNull Task<byte[]> task) {
//                if(task.isSuccessful()) {
//                    byte[] im = task.getResult();
//
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);
//
//                    model.setImage(bitmap);
//                    holder.ivImage.setImageBitmap(model.getImage());
//                }
//                else {
//                    holder.ivImage.setImageBitmap(null);
//                }
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return vetList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvName, tvClinicName, tvAddress;
        private ImageView ivImage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.card_vet_name);
            tvClinicName = itemView.findViewById(R.id.card_vet_clinic_name);
            tvAddress = itemView.findViewById(R.id.card_vet_address);
            ivImage = itemView.findViewById(R.id.card_vet_image);
        }
    }
}