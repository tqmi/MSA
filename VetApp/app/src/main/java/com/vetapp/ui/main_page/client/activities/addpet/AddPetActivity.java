package com.vetapp.ui.main_page.client.activities.addpet;

import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.vetapp.data.datasource.pet.PetDataSource;
import com.vetapp.data.models.pet.Pet;
import com.vetapp.data.models.register.RegisterResult;
import com.vetapp.databinding.ActivityAddPetBinding;

import java.io.IOException;

public class AddPetActivity extends AppCompatActivity {

    private AddPetViewModel addPetViewModel;
    private ActivityAddPetBinding binding;
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private Pet petData;
    private Uri imUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddPetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addPetViewModel = new ViewModelProvider(this, new AddPetViewModelFactory()).get(AddPetViewModel.class);
        petData = new Pet();

        EditText name = binding.etName;
        EditText category = binding.etCategory;
        EditText race = binding.etRace;

        ImageView imgProfile = binding.imgProfile;

        Button addImg = binding.btnAddImage;
        Button submit = binding.btnSubmit;

        ActivityResultLauncher<String> getContentLauncher =  registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                try {
                    if(result != null){
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), result);
                        imgProfile.setImageBitmap(ImageDecoder.decodeBitmap(source));
                    }
                    imUri = result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        addPetViewModel.getAddPetFormState().observe(this, new Observer<AddPetFormState>() {
            @Override
            public void onChanged(AddPetFormState addPetFormState) {
                if(addPetFormState == null) return;
                submit.setEnabled(addPetFormState.isDataValid());
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                petData.setName(name.getText().toString());
                petData.setCategory(category.getText().toString());
                petData.setRace(race.getText().toString());
                addPetViewModel.registerDataChanged(petData);
            }
        };
        name.addTextChangedListener(afterTextChangedListener);
        category.addTextChangedListener(afterTextChangedListener);
        race.addTextChangedListener(afterTextChangedListener);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetDataSource.writePet(petData, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        DocumentReference petDocRef = (DocumentReference) task.getResult();
                        if(imUri != null)
                            PetDataSource.writePetImage(imUri, petDocRef.getId(), new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        PetDataSource.setPetImageTrue(petDocRef.getId());
                                    }
                                }
                            });
                        finish();
                    }
                });
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentLauncher.launch("image/*");
            }
        });

    }
}