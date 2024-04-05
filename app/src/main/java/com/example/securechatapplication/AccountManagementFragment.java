package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentAccountManagementBinding;

public class AccountManagementFragment extends Fragment {

    private FragmentAccountManagementBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = FragmentAccountManagementBinding.inflate(inflater, container, false);

        //Set toolbar and bottom navigation to visible again if returning from calling
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View myView, Bundle savedInstance){
        super.onViewCreated(myView, savedInstance);

        //Navigation to Create Acct Menu
        binding.buttonCreateAccount.setOnClickListener(view1 -> NavHostFragment.findNavController(AccountManagementFragment.this)
                .navigate(R.id.action_accountManagementFragment_to_CreateAccountFragment));

        //Navigation to Edit Acct
        binding.buttonEditAccount.setOnClickListener(view1 -> NavHostFragment.findNavController(AccountManagementFragment.this)
                .navigate(R.id.action_accountManagementFragment_to_EditAccountFragment));

        binding.buttonDeleteAccount.setOnClickListener(view1 -> NavHostFragment.findNavController(AccountManagementFragment.this)
                .navigate(R.id.action_accountManagementFragment_to_DeleteAccountFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
