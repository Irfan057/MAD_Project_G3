package com.example.madprojectg3;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class homeFragment extends Fragment {

    private TextView uvIndexTextView;
    private TextView uvIndexText;
    private TextView alerttxt; // Added for the alert message
    private ImageView weatherImageView;

    private TextView tempTextView;

    private ImageView IVAlertSign;

    private ProgressBar uvprogressbar;


    //forecast:
    private TextView forecastTxt;
    private TextView uvPeakNum;
    private TextView uvLowNum;
    private TextView tvTimePeak;
    private TextView tvTimeLow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the TextViews and ImageView
        uvIndexTextView = rootView.findViewById(R.id.uvindexnum);
        uvIndexText = rootView.findViewById(R.id.uvIndexText);
        alerttxt = rootView.findViewById(R.id.alerttxt); // Initialize the alert TextView
        weatherImageView = rootView.findViewById(R.id.weatherimage);
        tempTextView = rootView.findViewById(R.id.tempnum);
        IVAlertSign = rootView.findViewById(R.id.IVAlertSign);
        uvprogressbar = rootView.findViewById(R.id.uvprogressbar);


        uvPeakNum = rootView.findViewById(R.id.uvpeaknum);
        uvLowNum = rootView.findViewById(R.id.uvlownum);
        tvTimePeak = rootView.findViewById(R.id.tvtimepeak);
        tvTimeLow = rootView.findViewById(R.id.tvtimelow);
        forecastTxt = rootView.findViewById(R.id.forecasttxt);

        // Call WeatherAPI when the fragment is created
        callWeatherApi();

        fetchUvForecast();
        makeForecastAlert();

        return rootView;
    }

    // Method to call WeatherAPI
    private void callWeatherApi() {
        String apiKey = "547cc9fb080b45a7a0055516241912";
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
                        double tempCelsius = current.getDouble("temp_c");

                        String weatherIconUrl = "https:" + current.getJSONObject("condition").getString("icon");

                        // Update the UI with the UV index and weather icon
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                uvIndexTextView.setText(String.format("%.1f", uvIndex));
                                tempTextView.setText("Temperature: " + String.format("%.1f", tempCelsius) + "° C"); // Update temp// Displays 8.0 as 8;
                                Glide.with(requireContext())
                                        .load(weatherIconUrl)
                                        .into(weatherImageView);
                                updateUvIndexText(uvIndex); // Call method to update UV index text
                                updateAlertMessage(uvIndex); // Call method to update the alert message
                                updateAlertIcon(uvIndex);
                                updateUvProgress((int) uvIndex);
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


    private void fetchUvForecast() {
        String apiKey = "547cc9fb080b45a7a0055516241912";
        String location = "Kuala Lumpur";  // You can change this to any location

        // Construct the URL for the WeatherAPI forecast endpoint
        String forecastUrl = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + location + "&days=1&aqi=no&alerts=no";

        // Create OkHttp client and request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        // Make the API call asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("UVForecastApi", "Error: " + e.getMessage());
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> uvPeakNum.setText("Error fetching data"));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("UVForecastApi", "Response: " + responseBody);

                        // Parse the response to find peak and lowest UV indices
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray forecastDays = jsonResponse.getJSONObject("forecast").getJSONArray("forecastday");

                        double highestUv = -1.0;
                        double lowestUv = Double.MAX_VALUE;
                        String highestUvTime = "";
                        String lowestUvTime = "";

                        // Loop through the forecast data for each day
                        for (int i = 0; i < forecastDays.length(); i++) {
                            JSONObject dayData = forecastDays.getJSONObject(i);
                            JSONArray hourlyData = dayData.getJSONArray("hour");

                            // Loop through hourly data for each day
                            for (int j = 0; j < hourlyData.length(); j++) {
                                JSONObject hourData = hourlyData.getJSONObject(j);
                                double uv = hourData.getDouble("uv");
                                String uvTime = hourData.getString("time");  // Use time field for each hour

                                // Find the highest UV
                                if (uv > highestUv) {
                                    highestUv = uv;
                                    highestUvTime = uvTime;
                                }

                                // Find the lowest UV
                                if (uv < lowestUv) {
                                    lowestUv = uv;
                                    lowestUvTime = uvTime;
                                }
                            }
                        }

                        // Update UI with the highest and lowest UV values and times
                        final double finalHighestUv = highestUv;
                        final String finalHighestUvTime = highestUvTime;
                        final double finalLowestUv = lowestUv;
                        final String finalLowestUvTime = lowestUvTime;

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                uvPeakNum.setText(String.format("%.1f", finalHighestUv));

                                // Format time to 12-hour format with AM/PM
                                tvTimePeak.setText(formatTo12HourFormat(finalHighestUvTime));

                                uvLowNum.setText(String.format("%.1f", finalLowestUv));

                                // Format time to 12-hour format with AM/PM
                                tvTimeLow.setText(formatTo12HourFormat(finalLowestUvTime));
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> uvPeakNum.setText("Error parsing data"));
                        }
                    }
                } else {
                    Log.e("UVForecastApi", "Response error: " + response.message());
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> uvPeakNum.setText("Error fetching data"));
                    }
                }
            }
        });
    }

    private void makeForecastAlert() {
        String apiKey = "547cc9fb080b45a7a0055516241912";
        String location = "Kuala Lumpur";

        String forecastUrl = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + location + "&days=1&aqi=no&alerts=no";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("UVForecastApi", "Error: " + e.getMessage());
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        forecastTxt.setText("Unable to get forecast");
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray forecastDays = jsonResponse.getJSONObject("forecast").getJSONArray("forecastday");

                        // Get the hourly data for today
                        JSONObject todayData = forecastDays.getJSONObject(0);
                        JSONArray hourlyData = todayData.getJSONArray("hour");

                        // Get current time
                        Calendar currentTime = Calendar.getInstance();
                        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);

                        // Format current time for display
                        String currentTimeStr = String.format("%02d:00", currentHour);

                        double currentUv = 0;
                        double nextHourUv = 0;
                        double next3HourMaxUv = 0;
                        String maxUvTime = "";
                        boolean foundCurrent = false;

                        // Loop through hourly data
                        for (int i = 0; i < hourlyData.length(); i++) {
                            JSONObject hourData = hourlyData.getJSONObject(i);
                            double uv = hourData.getDouble("uv");
                            String time = hourData.getString("time");
                            int hour = Integer.parseInt(time.split(" ")[1].split(":")[0]);

                            // Find current hour's UV
                            if (hour == currentHour) {
                                currentUv = uv;
                                foundCurrent = true;
                            }
                            // Check next hour
                            else if (foundCurrent && hour == currentHour + 1) {
                                nextHourUv = uv;
                            }
                            // Track maximum UV in next 3 hours
                            else if (foundCurrent && hour > currentHour && hour <= currentHour + 3) {
                                if (uv > next3HourMaxUv) {
                                    next3HourMaxUv = uv;
                                    maxUvTime = time;
                                }
                            }
                        }

                        // Final values for UI update
                        final double finalCurrentUv = currentUv;
                        final double finalNextHourUv = nextHourUv;
                        final double finalNext3HourMaxUv = next3HourMaxUv;
                        final String finalMaxUvTime = maxUvTime;
                        final String finalCurrentTimeStr = currentTimeStr;

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                StringBuilder forecastMessage = new StringBuilder();

                                // Current UV level context with current time
                                //forecastMessage.append(String.format("Current UV at %s: %.1f. ",
                                        //formatTo12HourFormat(finalCurrentTimeStr), finalCurrentUv));

                                // Check for significant increases
                                if (finalNextHourUv > finalCurrentUv + 1) {
                                    // Immediate increase in next hour
                                    forecastMessage.append(String.format("UV index will rise to %.1f in the next hour",
                                            finalNextHourUv));
                                }
                                else if (finalNext3HourMaxUv > finalCurrentUv + 2) {
                                    // Significant increase within 3 hours
                                    forecastMessage.append(String.format("UV index expected to rise to %.1f at %s",
                                            finalNext3HourMaxUv, formatTo12HourFormat(finalMaxUvTime)));
                                }
                                else if (finalCurrentUv > 5) {
                                    // High current UV but no significant increase
                                    forecastMessage.append("UV index remains high. Take precautions.");
                                }
                                else {
                                    // No significant changes
                                    forecastMessage.append("No significant UV increase expected in the next 3 hours");
                                }

                                forecastTxt.setText(forecastMessage.toString());
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                forecastTxt.setText("Unable to generate forecast");
                            });
                        }
                    }
                } else {
                    Log.e("UVForecastApi", "Response error: " + response.message());
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            forecastTxt.setText("Unable to get forecast");
                        });
                    }
                }
            }
        });
    }



    // Function to format the time from 24-hour to 12-hour format
    private String formatTo12HourFormat(String time) {
        try {
            // Parse the time to LocalDateTime
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(time, inputFormatter);

            // Format the time to 12-hour format with AM/PM
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            return dateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return time;  // If parsing fails, return the original time string
        }
    }









    // Helper method to format UV time
    private String formatUvTime(String uvTime) {
        // Convert the time string (e.g., "2024-12-26T12:00:00Z") to a user-friendly format
        return uvTime.replace("T", " ").replace("Z", " UTC");
    }








    // Method to update the UV index description and color
    private void updateUvIndexText(double uvIndex) {
        if (uvIndex >= 0 && uvIndex <= 2) {
            uvIndexText.setText("Low UV");
            uvIndexText.setTextColor(Color.parseColor("#00FF00")); // Green
            //uvIndexTextView.setTextColor(Color.parseColor("#FFFF00"));

        } else if (uvIndex > 2 && uvIndex <= 5) {
            uvIndexText.setText("Moderate UV");
            uvIndexText.setTextColor(Color.parseColor("#FFFF00")); // Yellow
            //uvIndexTextView.setTextColor(Color.parseColor("#FFFF00"));

        } else if (uvIndex > 5 && uvIndex <= 8) {
            uvIndexText.setText("High UV");
            uvIndexText.setTextColor(Color.parseColor("#FFA500")); // Orange

        } else if (uvIndex > 8 && uvIndex <= 10) {
            uvIndexText.setText("Very High UV");
            uvIndexText.setTextColor(Color.parseColor("#FF0000")); // Red

        } else if (uvIndex > 10) {
            uvIndexText.setText("Extreme UV");
            uvIndexText.setTextColor(Color.parseColor("#800080")); // Purple

        } else {
            uvIndexText.setText("Unknown UV Index");
            uvIndexText.setTextColor(Color.GRAY);
        }
    }











    // Method to update the alert message based on UV index
    private void updateAlertMessage(double uvIndex) {
        String alertMessage = "";

        if (uvIndex >= 0 && uvIndex <= 2) {
            alertMessage = "You're safe to go outside without sunscreen!";
            //alerttxt.setTextColor(Color.parseColor("#ffeb3b")); // Green color
        } else if (uvIndex > 2 && uvIndex <= 5) {
            alertMessage = "Consider applying sunscreen if staying outdoors for long!";
            //alerttxt.setTextColor(Color.parseColor("#ffeb3b")); // Yellow color
        } else if (uvIndex > 5 && uvIndex <= 8) {
            alertMessage = "Apply sunscreen and take precautions to avoid prolonged exposure!";
            //alerttxt.setTextColor(Color.parseColor("#ff5722")); // Orange color
        } else if (uvIndex > 8 && uvIndex <= 10) {
            alertMessage = "Sunscreen is essential! Avoid prolonged sun exposure!";
            //alerttxt.setTextColor(Color.parseColor("#d32f2f")); // Red color
        } else if (uvIndex > 10) {
            alertMessage = "The UV Index is Extreme! Stay in the shade and apply sunscreen frequently!";
            //alerttxt.setTextColor(Color.parseColor("#7b1fa2")); // Purple color
        }

        // Update the alert text
        alerttxt.setText(alertMessage);
    }











    private void updateAlertIcon(double uvIndex) {
        if (uvIndex <= 3) {
            IVAlertSign.setImageResource(R.drawable.shield); // Set shield.png for low UV
        } else {
            IVAlertSign.setImageResource(R.drawable.danger); // Set alert.png for high UV
        }
    }







    private void updateUvProgress(int uvIndex) {
        // If UV index is 0, set the progress to 1
        int progress = (uvIndex == 0) ? 1 : (int) ((uvIndex / 11.0) * 100);  // Ensure the minimum progress is 2

        // Set the progress of the ProgressBar
        uvprogressbar.setProgress(progress);

        // Adjust tint color based on the UV index range
        if (uvIndex <= 2) {
            uvprogressbar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        } else if (uvIndex <= 5) {
            uvprogressbar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        } else if (uvIndex <= 7) {
            uvprogressbar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        } else if (uvIndex <= 10) {
            uvprogressbar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        } else {
            uvprogressbar.setProgressTintList(ColorStateList.valueOf(Color.DKGRAY));
        }

        Log.d("UV Progress", "UV Index: " + uvIndex + ", Progress: " + progress);

    }






}
