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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_user_info extends Fragment {

    private FirebaseAuth auth;
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

        String userId = getArguments().getString("userId");

        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Link UI components
        usernameField = view.findViewById(R.id.edit_username_sg);
        phoneField = view.findViewById(R.id.phonenum_sg);
        emailField = view.findViewById(R.id.edit_email_sg);
        passwordField = view.findViewById(R.id.edit_TextTextPassword);
        confirmPasswordField = view.findViewById(R.id.edit_TextTextPassword2);
        Button submitButton = view.findViewById(R.id.edit_submitBtn);

        loadUserDetails();

        submitButton.setOnClickListener(v -> updateUserDetails(userId));

        return view;
    }

    private void loadUserDetails() {
        // Fetch user details from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    // Populate the EditText fields with the retrieved data
                    usernameField.setText(username);
                    phoneField.setText(phone);
                    emailField.setText(email);
                } else {
                    Toast.makeText(getContext(), "User details not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load user details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserDetails(String userId) {
        String updatedUsername = usernameField.getText().toString().trim();
        String updatedPhone = phoneField.getText().toString().trim();
        String updatedEmail = emailField.getText().toString().trim();
        String updatedPassword = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(updatedPassword) && updatedPassword.equals(confirmPassword)) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                user.updatePassword(updatedPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to update password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        // Update user details in the database
        userRef.child("username").setValue(updatedUsername);
        userRef.child("phone").setValue(updatedPhone);
        userRef.child("email").setValue(updatedEmail)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Details updated successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
