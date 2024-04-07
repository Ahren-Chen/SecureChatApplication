package com.example.securechatapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentCreateAccountBinding;
import com.example.server.AccountManagement;


public class CreateAccountFragment extends Fragment {
    private FragmentCreateAccountBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);

        //Set toolbar and bottom navigation to visible again if returning from calling
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View myView, Bundle savedInstance){
        super.onViewCreated(myView, savedInstance);
        AccountManagement accounts = new AccountManagement();
        accounts.start();

        //Previous button to navigate back to Account MGMT menu
        binding.CreateAccountPreviousButton.setOnClickListener(view1 -> NavHostFragment.findNavController(CreateAccountFragment.this)
                .navigate(R.id.action_CreateAccountFragment_to_accountManagementFragment));

        binding.CreateAccountButton.setOnClickListener(go ->{
            EditText usernameText = binding.getRoot().findViewById(R.id.CreateUsername);
            String username = usernameText.getText().toString();



            EditText passText = binding.getRoot().findViewById(R.id.editTextCreateAccountPass);
            String password = passText.getText().toString();

            EditText authText = binding.getRoot().findViewById(R.id.editTextNewSetAuthority);
            String authority = authText.getText().toString();

            if((TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(authority))){
                NavHostFragment.findNavController(CreateAccountFragment.this)
                        .navigate(R.id.action_CreateAccountFragment_to_ResponseFailureFragment);
            }

            else{
                accounts.createAccount(username,password,authority);
                NavHostFragment.findNavController(CreateAccountFragment.this)
                        .navigate(R.id.action_CreateAccountFragment_to_ResponseSuccessFragment);
            }

        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
