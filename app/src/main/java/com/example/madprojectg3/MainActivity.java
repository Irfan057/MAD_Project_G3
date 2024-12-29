package com.example.madprojectg3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {



    int transition_id;
    private Fragment homeFragment;
    private Fragment userFragment;

    private Fragment infositeFragment;

    private Fragment donationFragment;
    private Fragment actionHubFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Intent intent = getIntent();
        transition_id = intent.getIntExtra("transition_id",0);
        // Show the home fragment by default
        if (savedInstanceState == null) {
            if(transition_id == 0) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, homeFragment)
                        .commit();
            }else if(transition_id == 1){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, infositeFragment)
                        .commit();
            }
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
}
