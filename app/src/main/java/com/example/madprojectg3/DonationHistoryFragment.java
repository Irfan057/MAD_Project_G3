package com.example.madprojectg3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DonationHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonationHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonationHistoryFragment newInstance(String param1, String param2) {
        DonationHistoryFragment fragment = new DonationHistoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donation_history, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewDonationHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        donationHistoryList = new ArrayList<>();
        adapter = new DonationHistoryAdapter(getContext(), donationHistoryList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchDonationHistoryData();

        return rootView;
    }

    private void fetchDonationHistoryData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("userdonation")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String receiver = document.getString("Receiver");
                                Object donationAmountObj = document.get("DonationAmount_RM");
                                String donationAmount = donationAmountObj != null ? donationAmountObj.toString() : "N/A";
                                String date = document.getString("Date");
                                String time = document.getString("Time_24H");
                                String dateTime = date + ", " + time; // Concatenate date and time

                                // Create DonationHistory object and add it to the list
                                DonationHistory donationHistory = new DonationHistory(receiver, donationAmount, dateTime);
                                donationHistoryList.add(donationHistory);
                            }

                            // Notify the adapter that data has been loaded
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}