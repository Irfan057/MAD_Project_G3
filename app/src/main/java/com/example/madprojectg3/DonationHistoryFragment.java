package com.example.madprojectg3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonationHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private DonationHistoryAdapter adapter;
    private List<DonationHistory> donationHistoryList;

    public DonationHistoryFragment() {
        // Required empty public constructor
    }

    public static DonationHistoryFragment newInstance(String param1, String param2) {
        DonationHistoryFragment fragment = new DonationHistoryFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donation_history, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewDonationHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        donationHistoryList = new ArrayList<>();
        adapter = new DonationHistoryAdapter(getContext(), donationHistoryList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Realtime Database
        fetchDonationHistoryData();

        return rootView;
    }

    private void fetchDonationHistoryData() {
        // Retrieve userId from arguments
        String userId = getArguments() != null ? getArguments().getString("userId") : null;

        if (userId == null) {
            Toast.makeText(getContext(), "User ID not available", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userDonationsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("donationHistory");

        userDonationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donationHistoryList.clear();
                for (DataSnapshot donationSnapshot : snapshot.getChildren()) {
                    String receiver = donationSnapshot.child("Receiver").getValue(String.class);
                    Object donationAmountObj = donationSnapshot.child("DonationAmount_RM").getValue();
                    String donationAmount = donationAmountObj != null ? donationAmountObj.toString() : "N/A";
                    String date = donationSnapshot.child("Date").getValue(String.class);
                    String time = donationSnapshot.child("Time_24H").getValue(String.class);
                    String dateTime = date + ", " + time; // Concatenate date and time

                    // Create DonationHistory object and add it to the list
                    DonationHistory donationHistory = new DonationHistory(receiver, donationAmount, dateTime);
                    donationHistoryList.add(donationHistory);
                }

                // Notify the adapter that data has been loaded
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
