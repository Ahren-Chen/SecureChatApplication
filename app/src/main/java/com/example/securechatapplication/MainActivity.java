package com.example.securechatapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.securechatapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Building the main activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setting the toolbar and make it initially invisible for login page
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setVisibility(View.GONE);

        //Create the navigation controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //Bind the bottom navigation bar to the navigation controller
        NavigationUI.setupWithNavController(binding.bottomNavigationBar, navController);

        //If the navigation is clicked, an action is performed
        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {

            //If I clicked messages, then travel to messages
            if (item.getItemId() == R.id.MessagesFragment) {

                int currentFragmentId = Objects.requireNonNull(navController.getCurrentDestination()).getId();

                if (currentFragmentId == R.id.CallingFragment) {
                    navController.navigate(R.id.action_CallingFragment_to_MessagesFragment);
                }

                else if (currentFragmentId == R.id.HomeFragment) {
                    navController.navigate(R.id.action_HomeFragment_to_MessagesFragment);
                }

                return true;

            }

            //If I clicked on home, travel to homepage
            else if (item.getItemId() == R.id.HomeFragment) {

                int currentFragmentId = Objects.requireNonNull(navController.getCurrentDestination()).getId();

                if (currentFragmentId == R.id.CallingFragment) {
                    navController.navigate(R.id.action_CallingFragment_to_HomeFragment);
                }

                else if (currentFragmentId == R.id.MessagesFragment) {
                    navController.navigate(R.id.action_MessagesFragment_to_HomeFragment);
                }

                return true;

            }

            //If I clicked on calling, travel to call selection page
            else if (item.getItemId() == R.id.CallingFragment) {

                int currentFragmentId = Objects.requireNonNull(navController.getCurrentDestination()).getId();
                if (currentFragmentId == R.id.HomeFragment) {
                    navController.navigate(R.id.action_HomeFragment_to_CallingFragment);
                }

                else if (currentFragmentId == R.id.MessagesFragment) {
                    navController.navigate(R.id.action_MessagesFragment_to_CallingFragment);
                }

                return true;
            }

            return false;
        });

        //Set the bottom navigation bar settings. Initial is home page and invisible
        binding.bottomNavigationBar.setItemIconTintList(null);
        binding.bottomNavigationBar.setSelectedItemId(R.id.HomeFragment);
        binding.bottomNavigationBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}