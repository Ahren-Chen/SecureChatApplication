package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentResponseSuccessBinding;


public class ResponseSuccessFragment extends Fragment{
    private FragmentResponseSuccessBinding binding;
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = com.example.securechatapplication.databinding.FragmentResponseSuccessBinding.inflate(inflater, container, false);

        //Set toolbar and bottom navigation to visible again if returning from calling
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View myView, Bundle savedInstance){
        super.onViewCreated(myView, savedInstance);
        binding.RequestResponseHomeButton.setOnClickListener(view1 -> NavHostFragment.findNavController(ResponseSuccessFragment.this)
                .navigate(R.id.action_ResponseSuccessFragment_to_HomeFragment));

    }
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}