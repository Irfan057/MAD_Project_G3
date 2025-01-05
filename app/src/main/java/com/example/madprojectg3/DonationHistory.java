package com.example.madprojectg3;

public class DonationHistory {
    private String receiver;
    private String donationAmount;
    private String dateTime;

    // Constructor
    public DonationHistory(String receiver, String donationAmount, String dateTime) {
        this.receiver = receiver;
        this.donationAmount = donationAmount;
        this.dateTime = dateTime;
    }

    // Getters
    public String getReceiver() {
        return receiver;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public String getDateTime() {
        return dateTime;
    }
}
