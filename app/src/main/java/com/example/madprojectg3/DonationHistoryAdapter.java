package com.example.madprojectg3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder> {

    private List<DonationHistory> donationHistoryList;
    private Context context;

    public DonationHistoryAdapter(Context context, List<DonationHistory> donationHistoryList) {
        this.context = context;
        this.donationHistoryList = donationHistoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donation_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DonationHistory donationHistory = donationHistoryList.get(position);

        holder.tvProjectName.setText(donationHistory.getReceiver());
        holder.tvAmount.setText("Amount: RM" + donationHistory.getDonationAmount());
        holder.tvDate.setText("Date: " + donationHistory.getDateTime());
    }

    @Override
    public int getItemCount() {
        return donationHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProjectName, tvAmount, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
