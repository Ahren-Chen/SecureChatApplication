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

import com.example.securechatapplication.MAP.MediatedAuthenticationProtocol;
import com.example.securechatapplication.databinding.FragmentCallBinding;
import com.example.server.TimeOutException;

import java.util.ArrayList;

public class CallingFragment extends Fragment {

    private FragmentCallBinding binding;
    private Button btn;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = FragmentCallBinding.inflate(inflater, container, false);

        //Set toolbar and bottom navigation to visible again if returning from calling
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ArrayList<String> users = MediatedAuthenticationProtocol.getAllUsers();
            if (users == null) {
                throw new RuntimeException("No users returned");
            }
            for (String username : users) {
                addButton(username);
            }
        } catch (TimeOutException e) {
            requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.GONE);
            requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
            NavHostFragment.findNavController(CallingFragment.this)
                    .navigate(R.id.action_CallingFragment_to_LoginFragment);
        }
    }

    private void addButton(String name) {
        LinearLayout layout = binding.getRoot().findViewById(R.id.select_call_layout);
        btn = (Button)getLayoutInflater().inflate(R.layout.fragment_button_default, null);
        btn.setText(name);
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