package com.example.madprojectg3;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class userFragment extends Fragment implements OnMapReadyCallback {

    private TextView usernameTextView, skintypeTextView, cityTextView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private static final String TAG = "userFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 123;
    private MapView mapView;
    private GoogleMap googleMap;
    private static final LatLng KUALA_LUMPUR = new LatLng(3.1390, 101.6869); // KL coordinates
    private static final float DEFAULT_ZOOM = 12f;

    public userFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize views
        usernameTextView = view.findViewById(R.id.time2);
        skintypeTextView = view.findViewById(R.id.skintype);
        cityTextView = view.findViewById(R.id.time3);
        TextView sunscreenLinkTextView = view.findViewById(R.id.tv_sunscreen_reco_link);
        TextView healthTipsLinkTextView = view.findViewById(R.id.tv_health_reco_link);
        ImageButton editBtn = view.findViewById(R.id.editBtn);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        ImageButton logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> handleLogout());

        // Initialize location services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        setupLocationRequest();
        setupLocationCallback();

        // Handle user data
        handleUserData(editBtn, sunscreenLinkTextView, healthTipsLinkTextView);

        // Check location settings and start location updates
        checkLocationSettings();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        try {
            // Customize map settings
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

            // Move camera to Kuala Lumpur with animation
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(KUALA_LUMPUR, DEFAULT_ZOOM));

            // Add a marker for Kuala Lumpur
            googleMap.addMarker(new MarkerOptions()
                    .position(KUALA_LUMPUR)
                    .title("Kuala Lumpur"));

            // Optional: Enable my location button if you have location permissions
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        } catch (Exception e) {
            Log.e("MapError", "Error setting up map: " + e.getMessage());
        }
    }


    // Lifecycle methods for MapView
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        mapView.onPause(); // Pause the MapView
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUsername();
        mapView.onResume(); // Resume the MapView
        if (fusedLocationProviderClient != null && locationCallback != null) {
            startLocationUpdates(); // Start location updates
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void refreshUsername() {
        // Check if arguments are passed (e.g., from navigation)
        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            if (username != null && !username.isEmpty()) {
                usernameTextView.setText(username);
                return; // If username found in arguments, update and exit
            }
        }

        // If no arguments, fetch username from SharedPreferences
        android.content.SharedPreferences sharedPreferences = requireContext()
                .getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "N/A");

        // Set the username to the TextView
        usernameTextView.setText(username);
    }


    private void setupLocationRequest() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // 10 seconds
                .setFastestInterval(5000); // 5 seconds
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateCityName(location.getLatitude(), location.getLongitude());
                    // Once we get a location, we can stop updates
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                }
            }
        };
    }

    private void handleLogout() {
        // Show confirmation dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear user data from SharedPreferences
                    clearUserData();

                    // Navigate to login screen
                    navigateToLogin();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void clearUserData() {
        // Clear SharedPreferences
        android.content.SharedPreferences sharedPreferences = requireContext()
                .getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void navigateToLogin() {
        // Create intent for login activity
        Intent intent = new Intent(requireContext(), LoginForm.class); // Replace with your actual login activity name
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void handleUserData(ImageButton editBtn, TextView sunscreenLinkTextView, TextView healthTipsLinkTextView) {
        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            String skintype = args.getString("skintype");

            usernameTextView.setText(username != null ? username : "N/A");
            skintypeTextView.setText(skintype != null ? skintype : "N/A");

            // Set sunscreen link based on skin type
            if (skintype != null) {
                String sunscreenLink;
                String healthTipsLink;
                switch (skintype.toLowerCase(Locale.ROOT)) {
                    case "oily":
                        sunscreenLink = "https://www.watsons.com.my/blog/skincare-tips/best-sunscreens-for-oily-skin";
                        healthTipsLink = "https://www.healthline.com/health/beauty-skin-care/oily-skin-routine";
                        break;
                    case "dry":
                        sunscreenLink = "https://www.allure.com/story/sunscreens-for-dry-skin";
                        healthTipsLink = "https://www.aad.org/public/everyday-care/skin-care-basics/dry/dermatologists-tips-relieve-dry-skin";
                        break;
                    case "normal":
                        sunscreenLink = "https://www.hommesmalaysia.com/grooming/dermatologist-recommended-facial-sunscreens";
                        healthTipsLink = "https://www.webmd.com/beauty/normal-skin";
                        break;
                    case "sensitive":
                        sunscreenLink = "https://www.glamour.com/gallery/best-sunscreen-for-sensitive-skin";
                        healthTipsLink = "https://www.healthline.com/health/beauty-skin-care/sensitive-skin-care-routine";
                        break;
                    default:
                        sunscreenLink = "https://www.general-skincare.com";
                        healthTipsLink = "https://www.general-skincare.com/tips";
                        break;
                }

                // Create a clickable span for sunscreen recommendations
                SpannableString sunscreenSpannableString = new SpannableString("Click here for sunscreen recommendations");
                ClickableSpan sunscreenClickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        openWebLink(sunscreenLink);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark));
                        ds.setUnderlineText(true);
                    }
                };

                sunscreenSpannableString.setSpan(sunscreenClickableSpan, 0, sunscreenSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sunscreenLinkTextView.setText(sunscreenSpannableString);
                sunscreenLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
                sunscreenLinkTextView.setHighlightColor(Color.TRANSPARENT);

                // For Health Tips Link
                SpannableString spannableStringHealth = new SpannableString("Click here for health tips");
                ClickableSpan clickableSpanHealth = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        openWebLink(healthTipsLink);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark));
                        ds.setUnderlineText(true);
                    }
                };

                spannableStringHealth.setSpan(clickableSpanHealth, 0, spannableStringHealth.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                healthTipsLinkTextView.setText(spannableStringHealth);
                healthTipsLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
                healthTipsLinkTextView.setHighlightColor(Color.TRANSPARENT);
            } else {
                sunscreenLinkTextView.setText("Skin type not set");
                healthTipsLinkTextView.setText("Skin type not set");
            }

            editBtn.setOnClickListener(v -> {
                Fragment editUserInfoFragment = new edit_user_info();
                editUserInfoFragment.setArguments(args);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, editUserInfoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
        } else {
            Toast.makeText(getContext(), "Error: Missing user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(requireActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(requireActivity(), locationSettingsResponse -> {
            // Location settings are satisfied, start location updates
            startLocationUpdates();
        });

        task.addOnFailureListener(requireActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    // Show dialog to enable location services
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(requireActivity(), LOCATION_SETTINGS_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {
                    Log.e(TAG, "Error showing location settings dialog", sendEx);
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void updateCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                if (cityName != null && !cityName.isEmpty()) {
                    cityTextView.setText(cityName);
                } else {
                    String adminArea = addresses.get(0).getAdminArea();
                    cityTextView.setText(adminArea != null ? adminArea : "Location not found");
                }
                Log.d(TAG, "Location found: " + cityTextView.getText());
            } else {
                cityTextView.setText("Location not found");
                Log.e(TAG, "No address found for the location");
            }
        } catch (IOException e) {
            cityTextView.setText("Error fetching location");
            Log.e(TAG, "Geocoder IOException: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                cityTextView.setText("Location access denied");
            }
        }
    }


}