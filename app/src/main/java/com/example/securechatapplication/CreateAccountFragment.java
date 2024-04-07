package com.example.securechatapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.securechatapplication.MAP.MediatedAuthenticationProtocol;
import com.example.securechatapplication.databinding.FragmentAccountManagementBinding;
import com.example.securechatapplication.databinding.FragmentCallBinding;
import com.example.securechatapplication.databinding.FragmentCreateAccountBinding;
import com.example.securechatapplication.databinding.FragmentHomeBinding;

public class CreateAccountFragment extends Fragment {
    private FragmentCreateAccountBinding binding;
    private String authoritySetting = "CEO";
    private final String[] items = new String[]{"CEO", "Manager", "Employee"};

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

        //get the spinner from the xml.
        Spinner dropdown = binding.getRoot().findViewById(R.id.editTextSetAuthority);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View myView, Bundle savedInstance){
        super.onViewCreated(myView, savedInstance);

        //Previous button to navigate back to Account MGMT menu
        binding.CreateAccountPreviousButton.setOnClickListener(view1 -> NavHostFragment.findNavController(CreateAccountFragment.this)
                .navigate(R.id.action_CreateAccountFragment_to_accountManagementFragment));

        binding.editTextSetAuthority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                authoritySetting = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.CreateAccountButton.setOnClickListener(view1 -> {
            EditText usernameInput = binding.getRoot().findViewById(R.id.editTextCreateAccountEmail);
            String username = usernameInput.getText().toString();

            EditText passwordInput = binding.getRoot().findViewById(R.id.editTextCreateAccountPass);
            String password = passwordInput.getText().toString();

            if (! MediatedAuthenticationProtocol.createAccount(username, password, authoritySetting)) {
                throw new RuntimeException("Failed to create account");
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
