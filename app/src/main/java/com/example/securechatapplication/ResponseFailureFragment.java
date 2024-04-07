package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentResponseFailureBinding;

public class ResponseFailureFragment extends Fragment{
    private FragmentResponseFailureBinding binding;
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create view
        binding = FragmentResponseFailureBinding.inflate(inflater, container, false);

        //Set toolbar and bottom navigation to visible again if returning from calling
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View myView, Bundle savedInstance){
        super.onViewCreated(myView, savedInstance);
        binding.RequestResponseHomeButton.setOnClickListener(view1 -> NavHostFragment.findNavController(ResponseFailureFragment.this)
                .navigate(R.id.action_ResponseFailureFragment_to_HomeFragment));

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
