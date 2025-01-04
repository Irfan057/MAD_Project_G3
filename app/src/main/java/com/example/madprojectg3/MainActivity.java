package com.example.madprojectg3;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment userFragment;
    private Fragment infositeFragment;
    private Fragment donationFragment;
    private Fragment actionHubFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        String skintype = getIntent().getStringExtra("skintype");
        int frag_destination = getIntent().getIntExtra("transition_id",0);

        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("username", username);
        args.putString("skintype", skintype);

        userFragment = new userFragment();
        userFragment.setArguments(args);

        homeFragment = new homeFragment();
        infositeFragment = new infositeFragment();
        donationFragment = new donationFragment();
        actionHubFragment = new actionHubFragment();

        ImageButton userBtn = findViewById(R.id.userBtn);
        ImageButton homeBtn = findViewById(R.id.homeBtn);
        ImageButton searchBtn = findViewById(R.id.searchBtn);
        ImageButton donationBtn = findViewById(R.id.donationBtn);
        ImageButton actionHubBtn = findViewById(R.id.actionhubBtn);

        if (savedInstanceState == null) {
            if(frag_destination == 0) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, homeFragment)
                        .commit();
            }else if(frag_destination == 1){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, infositeFragment)
                        .commit();
            }
        }

        userBtn.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, userFragment)
                .addToBackStack(null)
                .commit());

        homeBtn.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, homeFragment)
                .commit());

        searchBtn.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, infositeFragment)
                .commit());

        donationBtn.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, donationFragment)
                .commit());

        actionHubBtn.setOnClickListener(v -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, actionHubFragment)
                .commit());
    }
}
