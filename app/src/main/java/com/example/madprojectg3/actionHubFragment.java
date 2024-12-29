package com.example.madprojectg3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link actionHubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class actionHubFragment extends Fragment {

    // Parameters for this fragment (optional for your use case)
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private List<Project> projectList;

    public actionHubFragment() {
        // Required empty public constructor
    }

    public static actionHubFragment newInstance(String param1, String param2) {
        actionHubFragment fragment = new actionHubFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_action_hub, container, false);

        listView = rootView.findViewById(R.id.projectListView);
        projectList = new ArrayList<>();

        // Initialize Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        fetchData(db);

        // Set item click listener to open project details
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            // Get the selected project
            Project selectedProject = projectList.get(position);

            // Check if the selected project is null
            if (selectedProject == null) {
                Toast.makeText(getContext(), "Error: Project not found!", Toast.LENGTH_SHORT).show();
                return; // Stop further execution
            }

            // Create an instance of ProjectDetailFragment with the project details
            Fragment projectDetailFragment = ProjectDetailFragment.newInstance(
                    selectedProject.getName(),
                    selectedProject.getDescription(),
                    selectedProject.getWebsiteUrl(),
                    selectedProject.getimageURL()
            );

            // Replace the current fragment with ProjectDetailFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, projectDetailFragment)
                    .addToBackStack(null) // Allows user to go back to the previous fragment
                    .commit();
        });

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle Button Clicks for Fragment Navigation
        Button donateButton = view.findViewById(R.id.donateButton);
        donateButton.setOnClickListener(v -> {
            // Switch to DonationFragment
            donationFragment donationFragment = new donationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, donationFragment); // Replace with your container's ID
            transaction.addToBackStack(null); // Optional, if you want to add the fragment to back stack
            transaction.commit();
        });

        Button logDonationButton = view.findViewById(R.id.logButton);
        logDonationButton.setOnClickListener(v -> {
            // Switch to LogDonationFragment
            LogDonationFragment logDonationFragment = new LogDonationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, logDonationFragment); // Replace with your container's ID
            transaction.addToBackStack(null); // Optional, if you want to add the fragment to back stack
            transaction.commit();
        });

        Button historyButton = view.findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            // Switch to DonationHistoryFragment
            DonationHistoryFragment historyFragment = new DonationHistoryFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, historyFragment); // Replace with your container's ID
            transaction.addToBackStack(null); // Optional, if you want to add the fragment to back stack
            transaction.commit();
        });
    }

    private void fetchData(FirebaseFirestore db) {
        // Fetch data from Firestore (projects collection)
        db.collection("projects")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            // Loop through each document in the collection
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Retrieve the fields from the document
                                String name = document.getString("name");
                                String description = document.getString("description");
                                String websiteURL = document.getString("website_url");
                                String imageURL = document.getString("image_url");

                                // Create a new Project object and add it to the list
                                Project project = new Project(name, description, websiteURL, imageURL);
                                projectList.add(project);
                            }

                            // Notify the adapter that data has been updated
                            if (getActivity() != null) {
                                ProjectAdapter adapter = new ProjectAdapter(getContext(), projectList);
                                listView.setAdapter(adapter);
                            }
                        }
                    } else {
                        // Handle errors if Firestore fetch fails
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
