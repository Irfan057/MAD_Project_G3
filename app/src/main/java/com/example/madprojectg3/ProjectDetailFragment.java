package com.example.madprojectg3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ProjectDetailFragment extends Fragment {

    public static ProjectDetailFragment newInstance(String name, String description, String websiteUrl, String imageURL) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putString("website_url", websiteUrl);
        args.putString("imageURL", imageURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project_detail, container, false);

        // Get project data from the arguments
        String name = getArguments().getString("name");
        String description = getArguments().getString("description");
        String websiteUrl = getArguments().getString("website_url");
        String imageURL = getArguments().getString("imageURL");

        // Set the project details in the UI
        TextView nameTextView = rootView.findViewById(R.id.projectNameTextView);
        TextView descriptionTextView = rootView.findViewById(R.id.projectDescriptionTextView);
        Button websiteButton = rootView.findViewById(R.id.websiteButton);
        ImageView projectImage = rootView.findViewById(R.id.projectImageView);

        nameTextView.setText(name);
        descriptionTextView.setText(description);

        websiteButton.setOnClickListener(v -> {
            // Open the project website URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
            startActivity(browserIntent);
        });

        Glide.with(this)
                .load(imageURL) // Image URL from Firestore
                .apply(new RequestOptions()
                        .placeholder(R.drawable.image_placeholder) // Add a placeholder image
                        .error(R.drawable.image_placeholder)) // Add an error image
                .into(projectImage);

        return rootView;
    }
}
