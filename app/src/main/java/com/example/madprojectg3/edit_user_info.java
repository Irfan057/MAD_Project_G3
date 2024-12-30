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
        String updatedPassword = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        boolean isUpdated = false;

        // Update username if it's not empty
        if (!TextUtils.isEmpty(updatedUsername)) {
            userRef.child("username").setValue(updatedUsername);

            // Save username to SharedPreferences
            android.content.SharedPreferences sharedPreferences = requireContext()
                    .getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("username", updatedUsername).apply();

            isUpdated = true;
        }


        // Update phone if it's not empty
        if (!TextUtils.isEmpty(updatedPhone)) {
            userRef.child("phone").setValue(updatedPhone);
            isUpdated = true;
        }

        // Update email if it's not empty
        if (!TextUtils.isEmpty(updatedEmail)) {
            userRef.child("email").setValue(updatedEmail);
            isUpdated = true;
        }

        // Validate and update password if both fields are filled and passwords match
        if (!TextUtils.isEmpty(updatedPassword)) {
            if (updatedPassword.equals(confirmPassword)) {
                userRef.child("password").setValue(updatedPassword);
                isUpdated = true;
            } else {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Show appropriate message based on the update status
        if (isUpdated) {
            Toast.makeText(getContext(), "User Details Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "No changes made to the details", Toast.LENGTH_SHORT).show();
        }
    }

}
