package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.securechatapplication.databinding.FragmentMessagesBinding;

public class MessagesFragment extends Fragment {
    private EditText editTextMessage;
    private Button buttonSend;
    private TextView textViewChat;
    private FragmentMessagesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        editTextMessage = view.findViewById(R.id.editTextMessage);
        textViewChat = view.findViewById(R.id.textViewChat);
        buttonSend = view.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            textViewChat.append(message + "\n");
            editTextMessage.setText("");
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}