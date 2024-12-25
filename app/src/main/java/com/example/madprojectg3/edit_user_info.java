package com.example.madprojectg3;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_user_info extends Fragment {

    private DatabaseReference userRef;

    private EditText usernameField;
    private EditText phoneField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;

    public edit_user_info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);

        Bundle args = getArguments();
        if (args != null) {
            String userId = args.getString("userId");
            if (userId != null) {
                userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                usernameField = view.findViewById(R.id.edit_username_sg);
                phoneField = view.findViewById(R.id.phonenum_sg);
                emailField = view.findViewById(R.id.edit_email_sg);
                passwordField = view.findViewById(R.id.edit_TextTextPassword);
                confirmPasswordField = view.findViewById(R.id.edit_TextTextPassword2);
                Button submitButton = view.findViewById(R.id.edit_submitBtn);

                submitButton.setOnClickListener(v -> updateUserDetails(userId));
            } else {
                Toast.makeText(getContext(), "Error: User ID is missing", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Error: No arguments received", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void updateUserDetails(String userId) {
        String updatedUsername = usernameField.getText().toString().trim();
        String updatedPhone = phoneField.getText().toString().trim();
        String updatedEmail = emailField.getText().toString().trim();

        if (!TextUtils.isEmpty(updatedUsername)) {
            userRef.child("username").setValue(updatedUsername);
        }
        if (!TextUtils.isEmpty(updatedPhone)) {
            userRef.child("phone").setValue(updatedPhone);
        }
        if (!TextUtils.isEmpty(updatedEmail)) {
            userRef.child("email").setValue(updatedEmail);
        }

        Toast.makeText(getContext(), "Details updated successfully", Toast.LENGTH_SHORT).show();
    }
}
