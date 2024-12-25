package com.example.madprojectg3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginForm extends AppCompatActivity {

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Initialize Firebase Realtime Database
        db = FirebaseDatabase.getInstance().getReference().child("users");

        // Find views by ID
        EditText usernameEditText = findViewById(R.id.columnUser);
        EditText passwordEditText = findViewById(R.id.TextPassword);
        Button loginButton = findViewById(R.id.LoginBtnF);
        CheckBox rememberMeCheckbox = findViewById(R.id.checkBoxRM);
        Button signUpButton = findViewById(R.id.signupbtn); // Find the sign-up button

        // Set click listener for the login button
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input fields
            if (username.isEmpty()) {
                usernameEditText.setError("Username is required");
                usernameEditText.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                return;
            }

            // Authenticate the user
            authenticateUser(username, password, usernameEditText, passwordEditText);
        });

        // Set click listener for the sign-up button
        signUpButton.setOnClickListener(v -> {
            // Navigate to the RegisterForm activity
            Intent intent = new Intent(LoginForm.this, RegisterForm.class);
            startActivity(intent);
        });
    }

    private void authenticateUser(String username, String password, EditText usernameEditText, EditText passwordEditText) {
        db.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String storedPassword = userSnapshot.child("password").getValue(String.class);
                        String skintype = userSnapshot.child("skinType").getValue(String.class);
                        String userId = userSnapshot.getKey(); // Retrieve user ID

                        if (storedPassword != null && storedPassword.equals(password)) {
                            Toast.makeText(LoginForm.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginForm.this, MainActivity.class);
                            intent.putExtra("userId", userId); // Pass the user ID
                            intent.putExtra("username", username);
                            intent.putExtra("skintype", skintype);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    passwordEditText.setError("Incorrect password");
                    passwordEditText.requestFocus();
                } else {
                    usernameEditText.setError("Username not found");
                    usernameEditText.requestFocus();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginForm.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


