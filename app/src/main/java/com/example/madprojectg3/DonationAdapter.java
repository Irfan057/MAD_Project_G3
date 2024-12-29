package com.example.madprojectg3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private final Context context;
    private final List<DonationProject> donationProjects;

    public DonationAdapter(Context context, List<DonationProject> donationProjects) {
        this.context = context;
        this.donationProjects = donationProjects;
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationViewHolder holder, int position) {
        DonationProject project = donationProjects.get(position);
        holder.projectName.setText(project.getName());
        holder.projectDescription.setText(project.getShortdesc());

        holder.donateButton.setOnClickListener(v -> {
            String url = project.getLink();
            if (url != null && !url.isEmpty()) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No browser found to open the link", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return donationProjects.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        TextView projectName;
        TextView projectDescription;
        Button donateButton;

        public DonationViewHolder(View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.projectName);
            projectDescription = itemView.findViewById(R.id.projectDescription);
            donateButton = itemView.findViewById(R.id.donateButton);
        }
    }
}
