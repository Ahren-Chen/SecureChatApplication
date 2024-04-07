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
import com.example.securechatapplication.databinding.FragmentMessagesBinding;

public class MessagesFragment extends Fragment {

    private EditText editTextMessage;
    private TextView textViewChat;
    private Button buttonSend;
    private Spinner spinnerChatWith;
    private FragmentMessagesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        editTextMessage = view.findViewById(R.id.editTextMessage);
        textViewChat = view.findViewById(R.id.textViewChat);
        buttonSend = view.findViewById(R.id.buttonSend);
        spinnerChatWith = view.findViewById(R.id.spinnerChatWith);

        // Set up the adapter for the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.chat_contacts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChatWith.setAdapter(adapter);

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
        String selectedContact = spinnerChatWith.getSelectedItem().toString();
        if (!message.isEmpty()) {
            // Format and display the message
            String formattedMessage = "Me to " + selectedContact + ": " + message + "\n";
            textViewChat.append(formattedMessage);

            // Clear the input field for the next message
            editTextMessage.setText("");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}