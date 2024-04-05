package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentAccountManagementBinding;
import com.example.securechatapplication.databinding.FragmentCallBinding;
import com.example.securechatapplication.databinding.FragmentCreateAccountBinding;
import com.example.securechatapplication.databinding.FragmentDeleteAccountBinding;
import com.example.securechatapplication.databinding.FragmentEditAccountBinding;
import com.example.securechatapplication.databinding.FragmentHomeBinding;

public class DeleteAccountFragment extends Fragment{
    private FragmentDeleteAccountBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = FragmentDeleteAccountBinding.inflate(inflater, container, false);

        //Set toolbar and bottom navigation to visible again if returning from calling
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View myView, Bundle savedInstance){
        super.onViewCreated(myView, savedInstance);

        //Previous button to navigate back to Account MGMT menu
        binding.DeleteAccountPreviousButton.setOnClickListener(view1 -> NavHostFragment.findNavController(DeleteAccountFragment.this)
                .navigate(R.id.action_DeleteAccountFragment_to_accountManagementFragment));

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
