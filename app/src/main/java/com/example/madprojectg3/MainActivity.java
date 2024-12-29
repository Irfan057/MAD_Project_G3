package com.example.madprojectg3;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {




    private Fragment homeFragment;
    private Fragment userFragment;

    private Fragment infositeFragment;

    private Fragment donationFragment;
    private Fragment actionHubFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestNotificationPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments
        homeFragment = new homeFragment();
        userFragment = new userFragment();
        infositeFragment = new infositeFragment();
        donationFragment = new donationFragment();
        actionHubFragment = new actionHubFragment();

        // Find the buttons
        ImageButton userBtn = findViewById(R.id.userBtn);
        ImageButton homeBtn = findViewById(R.id.homeBtn);
        ImageButton searchBtn = findViewById(R.id.searchBtn);
        ImageButton donationBtn = findViewById(R.id.donationBtn);
        ImageButton actionHubBtn = findViewById(R.id.actionhubBtn);

        // Show the home fragment by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, homeFragment)
                    .commit();
        }

        // Set listeners for buttons
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show UserFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, userFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, homeFragment)
                        .commit();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, infositeFragment)
                        .commit();
            }
        });


        donationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, donationFragment)
                        .commit();
            }
        });

        actionHubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show HomeFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, actionHubFragment)
                        .commit();
            }
        });

    }
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1
                );
            }
        }
    }

}
