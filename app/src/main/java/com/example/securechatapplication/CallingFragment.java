package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentCallBinding;

public class CallingFragment extends Fragment {

    private FragmentCallBinding binding;
    private Button btn;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCallBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FILL IN AN API CALL TO GET ALL USERS
        int buttons = 5;
        for (int i = 0; i < buttons; i++) {
            addButton();
        }
    }

    private void addButton() {
        LinearLayout layout = binding.getRoot().findViewById(R.id.select_call_layout);
        btn = (Button)getLayoutInflater().inflate(R.layout.fragment_button_default, null);
        btn.setText(R.string.next);
        btn.setOnClickListener(view -> NavHostFragment.findNavController(CallingFragment.this)
                .navigate(R.id.action_CallingFragment_to_CallingSomeoneFragment));

        layout.addView(btn);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}