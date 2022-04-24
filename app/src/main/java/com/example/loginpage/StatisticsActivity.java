package com.example.loginpage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    Button GotoExpense;
    public DatabaseReference budgetRef;
    private FirebaseAuth mAuth;
    private PieChart pieChart;
    public float FoodAndDining= (float) 0.2;
    public float Medical= (float) 0.4;
    public float Entertainment=(float) 0.5;
    public float ElectricityAndGas=(float)0.13;
    public float Housing=(float) 0.25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        mAuth= FirebaseAuth.getInstance();
        budgetRef= FirebaseDatabase.getInstance().getReference().child("Expense List").child(mAuth.getCurrentUser().getUid());

        pieChart = findViewById(R.id.piechart);
        GotoExpense=findViewById(R.id.goto_expense);
        setupPieChart();
        loadPieChartData();

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    Listexpensedata listexpensedata= snap.getValue(Listexpensedata.class);
                    if(listexpensedata.getItem()=="Food and Dining"){
                        FoodAndDining+=listexpensedata.getAmount();
                    }else if(listexpensedata.getItem()=="Electricity and Gas"){
                        ElectricityAndGas+=listexpensedata.getAmount();
                    }else if(listexpensedata.getItem()=="Housing"){
                        Housing+=listexpensedata.getAmount();
                    }else if(listexpensedata.getItem()=="Medical"){
                        Medical+=listexpensedata.getAmount();
                    }else{
                        Entertainment+=listexpensedata.getAmount();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try {
            GotoExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(),ListExpenseActivity.class);
                    Toast.makeText(StatisticsActivity.this,"Add Expenses",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Toast.makeText(StatisticsActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(6);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by Category");
        pieChart.setCenterTextSize(12);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(FoodAndDining, "Food & Dining"));
        entries.add(new PieEntry(Medical, "Medical"));
        entries.add(new PieEntry(Entertainment, "Clothing"));
        entries.add(new PieEntry(ElectricityAndGas, "Electricity and Gas"));
        entries.add(new PieEntry(Housing, "Housing"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}