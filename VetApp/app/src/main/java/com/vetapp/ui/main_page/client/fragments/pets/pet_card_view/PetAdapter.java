package com.vetapp.ui.main_page.client.fragments.pets.pet_card_view;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.Timestamp;
import com.vetapp.R;
import com.vetapp.data.datasource.pet.PetDataSource;
import com.vetapp.data.models.appointment.Appointment;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.ui.main_page.client.activities.addpet.AddPetActivity;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

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
        Appointment appointment = null;
        if (model.getAppointments() != null)
            appointment = model.getAppointments().stream().filter(new Predicate<Appointment>() {
                @Override
                public boolean test(Appointment appointment) {
                    if (appointment.getDate().compareTo(Timestamp.now()) > 0)
                        return true;
                    return false;
                }
            }).sorted(new Comparator<Appointment>() {
                @Override
                public int compare(Appointment o1, Appointment o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            }).findFirst().get();

        if (appointment != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, hh:mm");
            holder.tvAppTime.setText(dateFormat.format(appointment.getDate().toDate()));
            holder.tvAppType.setText(appointment.getVisitType().getName());
        } else {
            holder.tvAppType.setText("No appointment");
            holder.tvAppTime.setText("");
        }
        holder.tvName.setText(model.getName());
        holder.tvCategory.setText(model.getCategory());
        holder.tvRace.setText(model.getRace());
        PetDataSource.getPetImage(model, new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    byte[] im = task.getResult();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);

                    model.setImage(bitmap);
                    holder.ivImage.setImageBitmap(model.getImage());
                }
                else {
                    holder.ivImage.setImageBitmap(null);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetDataSource.deletePet(model);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getApplicationContext(), AddPetActivity.class));
            }
        });

    }



    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvName, tvCategory, tvRace, tvAppTime, tvAppType;
        private ImageView ivImage;
        private Button btnDelete;
        private Button btnEdit;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.card_pet_name);
            tvCategory = itemView.findViewById(R.id.card_pet_category);
            tvRace = itemView.findViewById(R.id.card_pet_race);
            ivImage = itemView.findViewById(R.id.card_pet_image);
            btnDelete = itemView.findViewById(R.id.card_pet_delete_btn);
            btnEdit = itemView.findViewById(R.id.card_pet_edit_btn);
            tvAppTime = itemView.findViewById(R.id.card_pet_appointment_time);
            tvAppType = itemView.findViewById(R.id.card_pet_appointment_type);
        }
    }
}
