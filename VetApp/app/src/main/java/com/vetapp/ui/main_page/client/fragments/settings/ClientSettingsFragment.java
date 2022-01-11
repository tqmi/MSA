package com.vetapp.ui.main_page.client.fragments.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vetapp.business.login.LoginHandler;
import com.vetapp.data.datasource.user.UserDataSource;
import com.vetapp.data.persistent.user.UserState;
import com.vetapp.databinding.ClientFragmentSettingsBinding;

import java.io.IOException;

public class ClientSettingsFragment extends Fragment {

    private ClientSettingsViewModel notificationsViewModel;
    private ClientFragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(ClientSettingsViewModel.class);

        binding = ClientFragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView tvName;
        tvName = binding.clientName;

        tvName.setText(UserState.getCurrentUser().getData().getFullName());

        ImageButton bName;
        bName = binding.clientNameEditBtn;
        ImageView profPic = binding.clientAvatar;

        Button chgPic = binding.btnClientChangeAvatar;


        UserDataSource.getProfilePicture(UserState.getUID(), new OnCompleteListener<byte[]>() {
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

        ActivityResultLauncher<String> getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    UserDataSource.setUserProfilePicture(UserState.getUID(), result, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), result);
                            try {
                                profPic.setImageBitmap(ImageDecoder.decodeBitmap(source));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        chgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentLauncher.launch("image/*");
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
                    UserDataSource.updateField(atr, input.getText().toString(), new OnCompleteListener() {
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

                    UserDataSource.updateField("firstName", fn, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            UserDataSource.updateField("lastName", ln, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    tv.setText(input.getText());
                                    UserState.getCurrentUser().getData().setFirstName(fn);
                                    UserState.getCurrentUser().getData().setLastName(ln);
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
