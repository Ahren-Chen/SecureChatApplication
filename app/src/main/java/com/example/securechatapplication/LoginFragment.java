package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.MAP.MediatedAuthenticationProtocol;
import com.example.securechatapplication.databinding.FragmentLoginBinding;

import kotlin.NotImplementedError;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create the view
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLoginSubmit.setOnClickListener(v -> {
            EditText usernameInput = binding.getRoot().findViewById(R.id.login_enter_username);
            String username = usernameInput.getText().toString();

            EditText passwordInput = binding.getRoot().findViewById(R.id.login_enter_password);
            String password = passwordInput.getText().toString();

            if (MediatedAuthenticationProtocol.authenticateLogin(username, password)) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_HomeFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}