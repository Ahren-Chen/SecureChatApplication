package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentCallingSomeoneBinding;

public class CallingPersonFragment extends Fragment {

    private FragmentCallingSomeoneBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCallingSomeoneBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonEndCall.setOnClickListener(view1 -> NavHostFragment.findNavController(CallingPersonFragment.this)
                .navigate(R.id.action_CallingSomeoneFragment_to_CallingFragment));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}