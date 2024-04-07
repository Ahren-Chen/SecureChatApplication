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

import com.example.securechatapplication.databinding.FragmentEditAccountBinding;
import com.example.server.AccountManagement;

public class EditAccountFragment extends Fragment{

    private FragmentEditAccountBinding binding;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = FragmentEditAccountBinding.inflate(inflater, container, false);

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
        binding.CreateAccountPreviousButton.setOnClickListener(view1 -> NavHostFragment.findNavController(EditAccountFragment.this)
                .navigate(R.id.action_EditAccountFragment_to_accountManagementFragment));


        binding.EditConfirmButton.setOnClickListener(go ->{
            EditText oldusernameText = binding.getRoot().findViewById(R.id.editTextCurrentAccountEmail);
            String oldName = oldusernameText.getText().toString();

            EditText usernameText = binding.getRoot().findViewById(R.id.editTextEditAccountEmail);
            String username = usernameText.getText().toString();

            EditText passText = binding.getRoot().findViewById(R.id.editTextEditAccountPass);
            String password = passText.getText().toString();

            EditText authText = binding.getRoot().findViewById(R.id.editTextEditSetAuthority);
            String authority = authText.getText().toString();

            //Fail if inputs left blank
            if((TextUtils.isEmpty(oldName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(authority)) ||TextUtils.isEmpty(username)){
                NavHostFragment.findNavController(EditAccountFragment.this)
                        .navigate(R.id.action_EditAccountFragment_to_ResponseFailureFragment);
            }
            else{
                accounts.editAccount(oldName,username,password,authority);
                NavHostFragment.findNavController(EditAccountFragment.this)
                        .navigate(R.id.action_EditAccountFragment_to_ResponseSuccessFragment);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
