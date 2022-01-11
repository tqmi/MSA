package com.vetapp.ui.main_page.vet.fragments.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vetapp.business.login.LoginHandler;
import com.vetapp.data.datasource.user.VetDataSource;
import com.vetapp.data.models.vet.Vet;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.VetFragmentSettingsBinding;

public class VetSettingsFragment extends Fragment {

    private VetsettingsViewModel notificationsViewModel;
    private VetFragmentSettingsBinding binding;
    private Vet vet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(VetsettingsViewModel.class);

        binding = VetFragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView tvName, tvClinic, tvAddress, tvPhone;
        tvName = binding.vetName;
        tvClinic = binding.vetClinicName;
        tvAddress = binding.vetAddress;
        tvPhone = binding.vetPhone;

        ImageButton bName, bClinic, bAddress, bPhone;
        bName = binding.vetNameEditBtn;
        bClinic = binding.vetClinicEditBtn;
        bAddress = binding.vetAddressEditBtn;
        bPhone = binding.vetPhoneEditBtn;

        ImageView profPic = binding.vetAvatar;

        VetDataSource.getVetProfile(UserState.getUID(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    vet = task.getResult().toObject(Vet.class);

                    tvName.setText(vet.getName());
                    tvClinic.setText(vet.getClinicName());
                    tvAddress.setText(vet.getAddress());
                    tvPhone.setText(vet.getPhone());

                } else {
                    Log.d(getTag(), "Error loading pet:" + task.getException());
                }
            }
        });

        VetDataSource.getVetProfilePicture(UserState.getUID(), new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    byte[] im = task.getResult();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(im, 0, im.length);

                    profPic.setImageBitmap(bitmap);
                } else {

                }
            }
        });

        bName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput(tvName, "name");
            }
        });

        bClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput(tvClinic, "clinicName");
            }
        });

        bAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput(tvAddress, "address");
            }
        });

        bPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput(tvPhone, "phone");
            }
        });


        final Button signoutButton = binding.btnSignout;
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHandler.logout();
            }
        });
        return root;
    }

    private void getInput(TextView tv, String atr) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Change " + atr);

// Set up the input
        final EditText input = new EditText(this.getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setText(tv.getText());
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!atr.equals("name"))
                    VetDataSource.updateField(atr, input.getText().toString(), new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                                tv.setText(input.getText().toString());
                        }
                    });
                else {
                    String fn, ln, n;

                    n = input.getText().toString();

                    n = n.trim();

                    if (n.split(" ").length < 2)
                        dialog.cancel();
                    fn = n.split(" ")[0];
                    ln = n.split(" ")[1];

                    VetDataSource.updateField("firstName", fn, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            VetDataSource.updateField("lastName", ln, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    tv.setText(input.getText());
                                }
                            });
                        }
                    });


                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
