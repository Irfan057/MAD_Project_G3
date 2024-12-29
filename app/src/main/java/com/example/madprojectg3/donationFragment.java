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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link donationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class donationFragment extends Fragment {

    private RecyclerView donationRecyclerView;
    private DonationAdapter donationAdapter;
    private List<DonationProject> donationProjects = new ArrayList<>();

    // Firestore instance
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public donationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonationFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_donation, container, false);

        donationRecyclerView = rootView.findViewById(R.id.donationRecyclerView);
        donationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        donationAdapter = new DonationAdapter(getContext(), donationProjects);
        donationRecyclerView.setAdapter(donationAdapter);

        fetchDonationProjects();  // Fetch data from Firestore

        return rootView;
    }

    // Fetch donation project data from Firestore
    private void fetchDonationProjects() {
        CollectionReference donationRef = db.collection("donation"); // Access the "donation" collection

        // Get all documents in the collection
        donationRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            donationProjects.clear(); // Clear any existing data
                            // Loop through each document in the collection
                            for (DocumentSnapshot document : querySnapshot) {
                                String name = document.getString("name");
                                String shortdesc = document.getString("shortdesc");
                                String link = document.getString("link");

                                // Add the data to the donationProjects list
                                DonationProject project = new DonationProject(name, shortdesc, link);
                                donationProjects.add(project);
                            }
                            donationAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load data: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}