package com.example.madprojectg3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class userFragment extends Fragment {

    public userFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        TextView usernameTextView = view.findViewById(R.id.time2);
        TextView skintypeTextView = view.findViewById(R.id.skintype);
        ImageButton editBtn = view.findViewById(R.id.editBtn);

        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            String skintype = args.getString("skintype");

            usernameTextView.setText(username != null ? username : "N/A");
            skintypeTextView.setText(skintype != null ? skintype : "N/A");

            editBtn.setOnClickListener(v -> {
                Fragment editUserInfoFragment = new edit_user_info();
                editUserInfoFragment.setArguments(args);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, editUserInfoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
        } else {
            Toast.makeText(getContext(), "Error: Missing user data", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
