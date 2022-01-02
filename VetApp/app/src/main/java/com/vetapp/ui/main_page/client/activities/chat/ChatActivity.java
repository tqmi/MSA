package com.vetapp.ui.main_page.client.activities.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vetapp.databinding.ChatActivityBinding;

public class ChatActivity extends AppCompatActivity {

    private ChatActivityBinding binding;
    private ChatViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, new ChatViewModelFactory()).get(ChatViewModel.class);


    }
}
