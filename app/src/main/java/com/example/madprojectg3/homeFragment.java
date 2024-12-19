package com.example.madprojectg3;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class homeFragment extends Fragment {

    private TextView uvIndexTextView;
    private TextView uvIndexText;
    private ImageView weatherImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the TextView and ImageView
        uvIndexTextView = rootView.findViewById(R.id.uvindexnum);
        uvIndexText = rootView.findViewById(R.id.uvIndexText);
        weatherImageView = rootView.findViewById(R.id.weatherimage);

        // Call WeatherAPI when the fragment is created
        callWeatherApi();

        return rootView;
    }

    // Method to call WeatherAPI
    private void callWeatherApi() {
        String apiKey = "547cc9fb080b45a7a0055516241912s";
        double latitude = 3.1667;
        double longitude = 101.7;

        // Construct the URL to get current weather data
        String url = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=Kuala Lumpur";

        // Create OkHttp client and request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        // Make the API call asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WeatherApi", "Error: " + e.getMessage());
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> uvIndexTextView.setText("Error fetching data"));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("WeatherApi", "Response: " + responseBody);

                        // Parse the response to get UV index and weather icon
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject current = jsonResponse.getJSONObject("current");
                        double uvIndex = current.getDouble("uv");
                        String weatherIconUrl = "https:" + current.getJSONObject("condition").getString("icon");

                        // Update the UI with the UV index and weather icon
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                uvIndexTextView.setText("UVI: " + uvIndex);
                                Glide.with(requireContext())
                                        .load(weatherIconUrl)
                                        .into(weatherImageView);
                                updateUvIndexText(uvIndex); // Call method to update UV index text
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> uvIndexTextView.setText("Error parsing data"));
                        }
                    }
                } else {
                    Log.e("WeatherApi", "Response error: " + response.message());
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> uvIndexTextView.setText("Error fetching data"));
                    }
                }
            }
        });
    }

    // Method to update the UV index description and color
    private void updateUvIndexText(double uvIndex) {
        if (uvIndex >= 0 && uvIndex <= 2) {
            uvIndexText.setText("Low UV Index");
            uvIndexText.setTextColor(Color.parseColor("#00FF00")); // Green
        } else if (uvIndex >= 3 && uvIndex <= 5) {
            uvIndexText.setText("Moderate UV Index");
            uvIndexText.setTextColor(Color.parseColor("#FFFF00")); // Yellow
        } else if (uvIndex >= 6 && uvIndex <= 7) {
            uvIndexText.setText("High UV Index");
            uvIndexText.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else if (uvIndex >= 8 && uvIndex <= 10) {
            uvIndexText.setText("Very High UV Index");
            uvIndexText.setTextColor(Color.parseColor("#FF0000")); // Red
        } else if (uvIndex >= 11) {
            uvIndexText.setText("Extreme UV Index");
            uvIndexText.setTextColor(Color.parseColor("#800080")); // Purple
        } else {
            uvIndexText.setText("Unknown UV Index");
            uvIndexText.setTextColor(Color.GRAY);
        }
    }
}
