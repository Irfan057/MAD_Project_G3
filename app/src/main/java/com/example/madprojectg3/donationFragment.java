package com.example.madprojectg3;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link donationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class donationFragment extends Fragment {

    private RecyclerView donationRecyclerView;
    private DonationAdapter donationAdapter;
    private List<DonationProject> donationProjects = new ArrayList<>();

    // Realtime Database Reference
    private DatabaseReference donationRef;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public donationFragment() {
        // Required empty public constructor
    }

    public static donationFragment newInstance(String param1, String param2) {
        donationFragment fragment = new donationFragment();
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
        donationRef = FirebaseDatabase.getInstance().getReference("donation"); // Reference to "donation" node
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donation, container, false);

        donationRecyclerView = rootView.findViewById(R.id.donationRecyclerView);
        donationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        donationAdapter = new DonationAdapter(getContext(), donationProjects);
        donationRecyclerView.setAdapter(donationAdapter);

        fetchDonationProjects(); // Fetch data from Realtime Database

        return rootView;
    }

    // Fetch donation project data from Realtime Database
    private void fetchDonationProjects() {
        donationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donationProjects.clear(); // Clear any existing data
                for (DataSnapshot donationSnapshot : dataSnapshot.getChildren()) {
                    String name = donationSnapshot.child("name").getValue(String.class);
                    String shortdesc = donationSnapshot.child("shortdesc").getValue(String.class);
                    String link = donationSnapshot.child("link").getValue(String.class);

                    DonationProject project = new DonationProject(name, shortdesc, link);
                    donationProjects.add(project);
                }
                donationAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
