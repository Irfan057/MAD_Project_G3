package com.example.madprojectg3;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LogDonationFragment extends Fragment {

    private Spinner projectSpinner;
    private EditText amountEditText;
    private Button logDonationButton;
    private FirebaseDatabase database;
    private DatabaseReference donationRef, userDonationHistoryRef;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> projectList;
    private String userId;

    public LogDonationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_donation, container, false);

        // Initialize UI components
        projectSpinner = view.findViewById(R.id.projectSpinner);
        amountEditText = view.findViewById(R.id.amountEditText);
        logDonationButton = view.findViewById(R.id.logDonationButton);

        // Initialize Realtime Database references
        database = FirebaseDatabase.getInstance();
        donationRef = database.getReference("donation");
        userDonationHistoryRef = database.getReference("users");

        // Initialize project list and adapter for spinner
        projectList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, projectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(adapter);

        // Load projects into spinner
        loadSpinnerData();

        userId = getArguments() != null ? getArguments().getString("userId") : null;

        // Button click listener to log donation
        logDonationButton.setOnClickListener(v -> logDonation());

        return view;
    }

    // Fetch donation project data for spinner
    private void loadSpinnerData() {
        donationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectList.clear();
                for (DataSnapshot projectSnapshot : snapshot.getChildren()) {
                    String projectName = projectSnapshot.child("name").getValue(String.class);
                    if (projectName != null) {
                        projectList.add(projectName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load projects: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle logging the donation
    private void logDonation() {
        String selectedProject = projectSpinner.getSelectedItem().toString();
        String donationAmountStr = amountEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(donationAmountStr)) {
            Toast.makeText(requireContext(), "Please enter a donation amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double donationAmount = Double.parseDouble(donationAmountStr);

        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());

        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique donation ID
        String donationId = userDonationHistoryRef.child(userId).child("donationHistory").push().getKey();

        if (donationId == null) {
            Toast.makeText(requireContext(), "Failed to generate donation ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create donation data map
        Map<String, Object> donationData = new HashMap<>();
        donationData.put("DonationAmount_RM", donationAmount);
        donationData.put("Date", date);
        donationData.put("Time_24H", time);
        donationData.put("Receiver", selectedProject);

        // Save donation data to Realtime Database under the specific user's donation history (using userId)
        userDonationHistoryRef.child(userId).child("donationHistory").child(donationId).setValue(donationData)
                .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Donation logged successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to log donation: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}



