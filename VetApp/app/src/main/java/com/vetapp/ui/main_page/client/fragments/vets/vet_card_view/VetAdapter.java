package com.vetapp.ui.main_page.client.fragments.vets.vet_card_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vetapp.R;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.ui.main_page.client.activities.appointment.NewAppointmentActivity;
import com.vetapp.ui.main_page.client.activities.chat.ChatActivity;
import com.vetapp.ui.main_page.client.activities.vetdetails.VetDetailsActivity;

import java.util.List;

public class VetAdapter extends RecyclerView.Adapter<com.vetapp.ui.main_page.client.fragments.vets.vet_card_view.VetAdapter.Viewholder> {

    private Context context;
    private List<Vet> vetList;

    public VetAdapter(Context context, List<Vet> petList) {
        this.context = context;
        this.vetList = petList;
    }

    @NonNull
    @Override
    public com.vetapp.ui.main_page.client.fragments.vets.vet_card_view.VetAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_cardview_layout, parent, false);
        return new com.vetapp.ui.main_page.client.fragments.vets.vet_card_view.VetAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.vetapp.ui.main_page.client.fragments.vets.vet_card_view.VetAdapter.Viewholder holder, int position) {
        Vet model = vetList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvClinicName.setText(model.getClinicName());
        holder.tvAddress.setText(model.getAddress());
        holder.rlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, holder.rlLayout);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_vet_list, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.vet_card_pm_details:
                                intent = new Intent(context, VetDetailsActivity.class);

                                intent.putExtra("model", model);
                                context.startActivity(intent);
                                break;
                            case R.id.vet_card_pm_schedule_appointment:
                                intent = new Intent(context, NewAppointmentActivity.class);
                                intent.putExtra("model", model);
                                context.startActivity(intent);
                                break;
                            case R.id.vet_card_pm_chat:
                                intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("model", model);
                                context.startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        VetDataSource.getVetProfilePicture(model.getDocid(), new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    byte[] im = task.getResult();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);

                    holder.ivImage.setImageBitmap(bitmap);
                } else {
                    holder.ivImage.setImageBitmap(null);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return vetList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView tvName, tvClinicName, tvAddress;
        private ImageView ivImage;
        private RelativeLayout rlLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.card_vet_name);
            tvClinicName = itemView.findViewById(R.id.card_vet_clinic_name);
            tvAddress = itemView.findViewById(R.id.card_vet_address);
            ivImage = itemView.findViewById(R.id.card_vet_image);
            rlLayout = itemView.findViewById(R.id.card_vet_layout);
        }
    }
}