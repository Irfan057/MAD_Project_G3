package com.example.madprojectg3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterForm extends AppCompatActivity {

    private DatabaseReference db;
    private static final String TAG = "RegisterForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        db = FirebaseDatabase.getInstance().getReference();

        EditText usernameEditText = findViewById(R.id.username_sg);
        EditText phoneEditText = findViewById(R.id.phonenum_sg);
        EditText emailEditText = findViewById(R.id.email_sg);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        EditText confirmPasswordEditText = findViewById(R.id.editTextTextPassword2);
        EditText dobEditText = findViewById(R.id.dob_text_view);
        RadioButton oilyRadioButton = findViewById(R.id.oilyOpt);
        RadioButton dryRadioButton = findViewById(R.id.dryOpt);
        RadioButton normalRadioButton = findViewById(R.id.normalOpt);
        RadioButton sensitiveRadioButton = findViewById(R.id.sensitiveOpt2);
        Button signUpButton = findViewById(R.id.SgnUpBtn);

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();
            String skinType = "";

            if (oilyRadioButton.isChecked()) {
                skinType = "Oily";
            } else if (dryRadioButton.isChecked()) {
                skinType = "Dry";
            } else if (normalRadioButton.isChecked()) {
                skinType = "Normal";
            } else if (sensitiveRadioButton.isChecked()) {
                skinType = "Sensitive";
            }

            // Check for empty fields
            if (username.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || dob.isEmpty() || skinType.isEmpty()) {
                Toast.makeText(RegisterForm.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match!");
                confirmPasswordEditText.requestFocus();
                return;
            }

            // Validate password length and special character
            if (!isValidPassword(password)) {
                passwordEditText.setError("Password must be at least 6 characters long and contain at least one special character.");
                passwordEditText.requestFocus();
                return;
            }

            checkIfDetailsExist(username, email, phone, password, usernameEditText, phoneEditText, emailEditText, dob, skinType);
        });
    }

    private boolean isValidPassword(String password) {
        // Check if password is at least 6 characters long and contains at least one special character
        String regex = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void checkIfDetailsExist(String username, String email, String phone, String password, EditText usernameEditText, EditText phoneEditText, EditText emailEditText, String dob, String skinType) {
        db.child("users").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usernameEditText.setError("Username already taken!");
                    usernameEditText.requestFocus();
                } else {
                    db.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                emailEditText.setError("Email already taken!");
                                emailEditText.requestFocus();
                            } else {
                                db.child("users").orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            phoneEditText.setError("Phone number already taken!");
                                            phoneEditText.requestFocus();
                                        } else {
                                            String userId = generateUniqueUserId();
                                            User user = new User(username, phone, email, dob, skinType, password);

                                            db.child("users").child(userId).setValue(user)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegisterForm.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(RegisterForm.this, MainActivity.class);
                                                            intent.putExtra("userId", userId); // Pass the user ID
                                                            intent.putExtra("username", username);
                                                            intent.putExtra("skintype", skinType);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(RegisterForm.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(RegisterForm.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(RegisterForm.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegisterForm.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateUniqueUserId() {
        Random random = new Random();
        int userId = random.nextInt(90000) + 10000;
        return String.valueOf(userId);
    }
}