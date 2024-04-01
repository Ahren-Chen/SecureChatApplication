package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.databinding.FragmentCallBinding;

public class CallingFragment extends Fragment {

    private FragmentCallBinding binding;
    private Button btn;
    private int test = 5;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCallBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });
    }

    private void addButton() {
        ConstraintLayout layout = binding.getRoot().findViewById(R.id.constraint_layout);
        btn = new Button(binding.constraintLayout.getContext());
        btn.setText(R.string.next);
        btn.setX(test);
        btn.setY(test);
        test += 50;
        layout.addView(btn);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}