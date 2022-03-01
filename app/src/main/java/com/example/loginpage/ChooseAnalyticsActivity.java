package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseAnalyticsActivity extends AppCompatActivity {
    private Button today_view_btn;
    private Button weekly_view_btn;
    private Button monthly_view_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytics);

        today_view_btn = findViewById(R.id.today_view_btn);
        weekly_view_btn = findViewById(R.id.weekly_view_btn);
        monthly_view_btn = findViewById(R.id.monthly_view_btn);

        today_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticsActivity.this, TodayAnalytics.class);
                startActivity(intent);
            }
        });

       weekly_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticsActivity.this, WeeklyAnalytics.class);
                startActivity(intent);
            }
        });

        monthly_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAnalyticsActivity.this, MonthlyAnalytics.class);
                startActivity(intent);
            }
        });
    }
}