package com.example.madprojectg3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class userFragment extends Fragment {

    public userFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Bind views
        TextView usernameTextView = view.findViewById(R.id.time2);
        TextView skintypeTextView = view.findViewById(R.id.skintype);
        ImageButton editBtn = view.findViewById(R.id.editBtn);

        // Get arguments passed to this fragment
        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            String skintype = args.getString("skintype");

            usernameTextView.setText(username);
            skintypeTextView.setText(skintype);
        }

        // Set onClickListener for the edit button
        editBtn.setOnClickListener(v -> {
            // Create a new instance of edit_user_info fragment
            Fragment editUserInfoFragment = new edit_user_info();

            // Pass data to the new fragment
            Bundle editArgs = new Bundle();
            if (args != null) {
                editArgs.putString("userId", args.getString("userId")); // Ensure userId is passed
                editArgs.putString("username", args.getString("username"));
                editArgs.putString("email", args.getString("email"));
                editArgs.putString("phone", args.getString("phone"));
            }
            editUserInfoFragment.setArguments(editArgs);

            // Perform fragment transaction
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, editUserInfoFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
