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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogDonationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogDonationFragment extends Fragment {

    private Spinner projectSpinner;
    private EditText amountEditText;
    private Button logDonationButton;
    private FirebaseFirestore db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> projectList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogDonationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonationLoggingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogDonationFragment newInstance(String param1, String param2) {
        LogDonationFragment fragment = new LogDonationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_donation, container, false);

        projectSpinner = view.findViewById(R.id.projectSpinner);
        amountEditText = view.findViewById(R.id.amountEditText);
        logDonationButton = view.findViewById(R.id.logDonationButton);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        projectList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, projectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(adapter);

        // Load spinner data from Firestore
        loadSpinnerData();

        // Button click listener
        logDonationButton.setOnClickListener(v -> logDonation());

        return view;
    }

    private void loadSpinnerData() {
        db.collection("donation")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String projectName = document.getString("name");
                            if (projectName != null) {
                                projectList.add(projectName);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify spinner of data change
                    } else {
                        Toast.makeText(requireContext(), "Failed to load projects: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

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

        // Firestore data map
        Map<String, Object> donationData = new HashMap<>();
        donationData.put("UserName", "John Doe"); // Replace with actual user data if available
        donationData.put("DonationAmount_RM", donationAmount);
        donationData.put("Date", date);
        donationData.put("Time_24H", time);
        donationData.put("Receiver", selectedProject);

        // Save data to Firestore
        db.collection("userdonation")
                .add(donationData)
                .addOnSuccessListener(documentReference -> Toast.makeText(requireContext(), "Donation logged successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to log donation: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}