package com.vetapp.ui.main_page.client.activities.vetdetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.databinding.ClientVetDetailActivityBinding;

public class VetDetailsActivity extends AppCompatActivity {

    private VetDetailsViewModel viewModel;
    private ClientVetDetailActivityBinding binding;
    private ImageView vetProfPic;
    private TextView tvName;
    private TextView tvClinicName;
    private TextView tvAddress;
    private RatingBar rbRating;
    private Vet vet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ClientVetDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button goBackbtn = binding.btnDetailsToVetList;
        viewModel = new ViewModelProvider(this, new VetDetailsViewModelFactory()).get(VetDetailsViewModel.class);
        vet = (Vet) getIntent().getExtras().get("model");


        vetProfPic = binding.clVetAvatar;
        tvName = binding.clVetFirstName;
        tvClinicName = binding.clVetClinic;
        tvAddress = binding.clVetAddress;

        rbRating = binding.clVetRating;
        rbRating.setRating(3.5f);

        updateFields();

        VetDataSource.getVetProfilePicture(vet.getDocid(), new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    byte[] im = task.getResult();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);

                    vetProfPic.setImageBitmap(bitmap);
                } else {

                }
            }
        });


        goBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateFields() {
        tvName.setText(vet.getName());
        tvClinicName.setText(vet.getClinicName());
        tvAddress.setText(vet.getAddress());
        rbRating.setRating(vet.getRating());
    }
}
