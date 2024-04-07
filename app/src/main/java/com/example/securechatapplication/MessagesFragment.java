package com.example.securechatapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.MAP.MediatedAuthenticationProtocol;
import com.example.securechatapplication.databinding.FragmentMessagesBinding;
import com.example.server.TimeOutException;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
public class MessagesFragment extends Fragment {

    private EditText editTextMessage;
    private TextView textViewChat;
    private Button buttonSend;
    private Spinner spinnerChatWith;
    private FragmentMessagesBinding binding;
    private static final String FILE_NAME = "chat_history.txt";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater, container, false);

        editTextMessage = binding.getRoot().findViewById(R.id.editTextMessage);
        textViewChat = binding.getRoot().findViewById(R.id.textViewChat);
        buttonSend = binding.getRoot().findViewById(R.id.buttonSend);
        spinnerChatWith = binding.getRoot().findViewById(R.id.spinnerChatWith);

        // Set up the adapter for the spinner
        ArrayList<String> users = new ArrayList<>();
        try {
            users = MediatedAuthenticationProtocol.getAllUsers();
            if (users == null) {
                throw new RuntimeException("No users returned");
            }
        } catch (TimeOutException e) {
            requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
            NavHostFragment.findNavController(MessagesFragment.this)
                    .navigate(R.id.action_CallingFragment_to_LoginFragment);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                binding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, users);
        spinnerChatWith.setAdapter(adapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return binding.getRoot();
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        String selectedContact = spinnerChatWith.getSelectedItem().toString();
        if (!message.isEmpty()) {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            String formattedMessage = currentTime + " - Me to " + selectedContact + ": " + message + "\n";
            textViewChat.append(formattedMessage);

            appendToFile(formattedMessage);

            editTextMessage.setText("");
        }
    }

    private void appendToFile(String text) {
        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}