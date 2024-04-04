package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Create the view
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        //When reaching the home page, set the toolbar and bottom navigation visible
        requireActivity().findViewById(R.id.bottom_navigation_bar).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //If the messages button is clicked, travel to messages
        binding.buttonMessages.setOnClickListener(view1 -> NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_HomeFragment_to_MessagesFragment));

        //If the calling button is clicked, travel to call selection
        binding.buttonCall.setOnClickListener(view1 -> NavHostFragment.findNavController(HomeFragment.this)
                .navigate(R.id.action_HomeFragment_to_CallingFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}