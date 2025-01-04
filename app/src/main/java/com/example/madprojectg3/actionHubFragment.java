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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link actionHubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class actionHubFragment extends Fragment {

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

        // Initialize Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("project");

        // Fetch data from Realtime Database
        fetchData(databaseReference);

        // Set item click listener to open project details
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Project selectedProject = projectList.get(position);

            if (selectedProject == null) {
                Toast.makeText(getContext(), "Error: Project not found!", Toast.LENGTH_SHORT).show();
                return;
            }

            Fragment projectDetailFragment = ProjectDetailFragment.newInstance(
                    selectedProject.getName(),
                    selectedProject.getDescription(),
                    selectedProject.getWebsiteUrl(),
                    selectedProject.getimageURL()
            );

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, projectDetailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button donateButton = view.findViewById(R.id.donateButton);
        donateButton.setOnClickListener(v -> {
            donationFragment donationFragment = new donationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, donationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Button logDonationButton = view.findViewById(R.id.logButton);
        logDonationButton.setOnClickListener(v -> {
            LogDonationFragment logDonationFragment = new LogDonationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, logDonationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Button historyButton = view.findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            DonationHistoryFragment historyFragment = new DonationHistoryFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, historyFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void fetchData(DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();

                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    String name = projectSnapshot.child("name").getValue(String.class);
                    String description = projectSnapshot.child("desc").getValue(String.class);
                    String websiteURL = projectSnapshot.child("website_url").getValue(String.class);
                    String imageURL = projectSnapshot.child("image_url").getValue(String.class);

                    Project project = new Project(name, description, websiteURL, imageURL);
                    projectList.add(project);
                }

                if (getActivity() != null) {
                    ProjectAdapter adapter = new ProjectAdapter(getContext(), projectList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

