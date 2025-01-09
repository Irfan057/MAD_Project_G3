package com.example.madprojectg3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebSearch extends AppCompatActivity {
    String SearchURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String username = intent.getStringExtra("username");
        String skintype = intent.getStringExtra("skintype");
        SearchURL = intent.getStringExtra("SearchURL");
        WebView webView = findViewById(R.id.WVSearch);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(SearchURL);
        Button BtnBack = findViewById(R.id.backButton);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentback = new Intent(WebSearch.this, MainActivity.class);
                intentback.putExtra("userId",userId);
                intentback.putExtra("username",username);
                intentback.putExtra("skintype",skintype);
                intentback.putExtra("transition_id",1);
                startActivity(intentback);
            }
        });
    }
}