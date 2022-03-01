package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeeklyAnalytics extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef,personalRef;

    private TextView totalBudgetAmountTextView, analyticsElectricityAmount,analyticsFoodAmount,analyticsHousingAmount,analyticsEntertainmentAmount;
    private TextView analyticsMedicalAmount, monthSpentAmount;

    private RelativeLayout linearLayoutFood,linearLayoutMedical,linearLayoutHousing,linearLayoutEntertainment,linearLayoutElectricity;
    private RelativeLayout linearLayoutAnalysis;

    private AnyChartView anyChartView;
    private TextView progress_ratio_electricity,progress_ratio_food,progress_ratio_housing,progress_ratio_medical, progress_ratio_ent, monthRatioSpending;
    private ImageView status_Image_medical, status_Image_food,status_Image_housing,status_Image_ent,status_Image_electricity, monthRatioSpending_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_analytics);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);


        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);

        //general analytic
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_Image = findViewById(R.id.monthRatioSpending_Image);

        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHousingAmount = findViewById(R.id.analyticsHousingAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsElectricityAmount = findViewById(R.id.analyticsElectricityAmount);
        analyticsMedicalAmount = findViewById(R.id.analyticsMedicalAmount);


        //Relative layouts views
        linearLayoutFood = findViewById(R.id.linearLayoutFood);
        linearLayoutHousing = findViewById(R.id.linearLayoutHousing);
        linearLayoutEntertainment = findViewById(R.id.linearLayoutEntertainment);
        linearLayoutMedical = findViewById(R.id.linearLayoutMedical);
        linearLayoutElectricity = findViewById(R.id.linearLayoutElectricity);


        //textviews
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_housing = findViewById(R.id.progress_ratio_housing);
        progress_ratio_ent = findViewById(R.id.progress_ratio_ent);
        progress_ratio_electricity = findViewById(R.id.progress_ratio_electricity);
        progress_ratio_medical = findViewById(R.id.progress_ratio_medical);

//imageviews
        status_Image_food = findViewById(R.id.status_Image_food);
        status_Image_housing = findViewById(R.id.status_Image_housing);
        status_Image_ent = findViewById(R.id.status_Image_ent);
        status_Image_medical = findViewById(R.id.status_Image_medical);
        status_Image_electricity = findViewById(R.id.status_Image_electricity);


        //anyChartView
        anyChartView = findViewById(R.id.anyChartView);

        getTotalWeekFoodExpense();
        getTotalWeekHousingExpenses();
        getTotalWeekEntertainmentExpenses();
        getTotalWeekMedicalExpenses();
        getTotalWeekElectricityExpenses();
        getTotalWeekSpending();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        loadGraph();
                        setStatusAndImageResource();
                    }
                },
                2000
        );

    }

    private void getTotalWeekFoodExpense(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Food and Dining"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("weekFood").setValue(totalAmount);
                }else {
                    linearLayoutFood.setVisibility(View.GONE);
                    personalRef.child("weekFood").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalytics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHousingExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Housing"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHousingAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("weekHousing").setValue(totalAmount);
                }else {
                    linearLayoutHousing.setVisibility(View.GONE);
                    personalRef.child("weekHousing").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalytics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntertainmentExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Entertainment"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("weekEnt").setValue(totalAmount);
                }else {
                    linearLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("weekEnt").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalytics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekMedicalExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Medical"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsMedicalAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("weekMedi").setValue(totalAmount);
                }else {
                    linearLayoutMedical.setVisibility(View.GONE);
                    personalRef.child("weekMedi").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalytics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekElectricityExpenses(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        String itemNweek = "Electricity and Gas"+weeks.getWeeks();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsElectricityAmount.setText("Spent: " + totalAmount);
                    }
                    personalRef.child("weekElec").setValue(totalAmount);
                }else {
                    linearLayoutElectricity.setVisibility(View.GONE);
                    personalRef.child("weekElec").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WeeklyAnalytics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekSpending(){
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expense List").child(onlineUserId);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for (DataSnapshot ds :  dataSnapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount+=pTotal;

                    }
                    totalBudgetAmountTextView.setText("Total week's spending: Rs. "+ totalAmount);
                    monthSpentAmount.setText("Total Spent: Rs. "+totalAmount);
                }else {
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int foodTotal;
                    if (snapshot.hasChild("weekFood")) {
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }

                    int housingTotal;
                    if (snapshot.hasChild("weekHousing")) {
                        housingTotal = Integer.parseInt(snapshot.child("weekHousing").getValue().toString());
                    } else {
                        housingTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("weekEnt")) {
                        entTotal = Integer.parseInt(snapshot.child("weekEnt").getValue().toString());
                    } else {
                        entTotal = 0;
                    }

                    int mediTotal;
                    if (snapshot.hasChild("weekMedi")) {
                        mediTotal = Integer.parseInt(snapshot.child("weekMedi").getValue().toString());
                    } else {
                        mediTotal = 0;
                    }

                    int elecTotal;
                    if (snapshot.hasChild("weekElec")) {
                        elecTotal = Integer.parseInt(snapshot.child("weekElec").getValue().toString());
                    } else {
                        elecTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Housing exp", housingTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("Entertainment", entTotal));
                    data.add(new ValueDataEntry("Electricity", elecTotal));
                    data.add(new ValueDataEntry("Medical", mediTotal));

                    pie.data(data);

                    pie.title("Week Analytics");

                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);
                    pie.legend().title()
                            .text("Items Spent On")
                            .padding(0d, 0d, 10d, 0d);

                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);
                } else {
                    Toast.makeText(WeeklyAnalytics.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() ){

                    float foodTotal;
                    if (snapshot.hasChild("weekFood")){
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    float housingTotal;
                    if (snapshot.hasChild("weekHousing")){
                        housingTotal = Integer.parseInt(snapshot.child("weekHousing").getValue().toString());
                    }else {
                        housingTotal = 0;
                    }

                    float entTotal;
                    if (snapshot.hasChild("weekEnt")){
                        entTotal = Integer.parseInt(snapshot.child("weekEnt").getValue().toString());
                    }else {
                        entTotal=0;
                    }

                    float mediTotal;
                    if (snapshot.hasChild("weekMedi")){
                        mediTotal = Integer.parseInt(snapshot.child("weekMedi").getValue().toString());
                    }else {
                        mediTotal = 0;
                    }

                    float elecTotal;
                    if (snapshot.hasChild("weekElec")){
                        elecTotal = Integer.parseInt(snapshot.child("weekElec").getValue().toString());
                    }else {
                        elecTotal = 0;
                    }

                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("week")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("week").getValue().toString());
                    }else {
                        monthTotalSpentAmount = 0;
                    }



                    float foodRatio;
                    if (snapshot.hasChild("weekFoodRatio")){
                        foodRatio = Integer.parseInt(snapshot.child("weekFoodRatio").getValue().toString());
                    }else {
                        foodRatio = 0;
                    }

                    float housingRatio;
                    if (snapshot.hasChild("weekHouseRatio")){
                        housingRatio = Integer.parseInt(snapshot.child("weekHouseRatio").getValue().toString());
                    }else {
                        housingRatio = 0;
                    }

                    float entRatio;
                    if (snapshot.hasChild("weekEntRatio")){
                        entRatio= Integer.parseInt(snapshot.child("weekEntRatio").getValue().toString());
                    }else {
                        entRatio = 0;
                    }

                    float mediRatio;
                    if (snapshot.hasChild("weekMedicalRatio")){
                        mediRatio= Integer.parseInt(snapshot.child("weekMedicalRatio").getValue().toString());
                    }else {
                        mediRatio=0;
                    }

                    float elecRatio;
                    if (snapshot.hasChild("weekElecRatio")){
                         elecRatio = Integer.parseInt(snapshot.child("weekElecRatio").getValue().toString());
                    }else {
                        elecRatio = 0;
                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("weeklyBudget")){
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("weeklyBudget").getValue().toString());
                    }else {
                        monthTotalSpentAmountRatio =0;
                    }

                    float monthPercent = (monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;
                    if (monthPercent<50){
                        monthRatioSpending.setText(monthPercent+" %" +" used of "+monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.green);
                    }else if (monthPercent >= 50 && monthPercent <100){
                        monthRatioSpending.setText(monthPercent+" %" +" used of "+monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.brown);
                    }else {
                        monthRatioSpending.setText(monthPercent+" %" +" used of "+monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_Image.setImageResource(R.drawable.red);

                    }

                    float foodPercent = (foodTotal/foodRatio)*100;
                    if (foodPercent<50){
                        progress_ratio_food.setText(foodPercent+" %" +" used of "+foodRatio + ". Status:");
                        status_Image_food.setImageResource(R.drawable.green);
                    }else if (foodPercent >= 50 && foodPercent <100){
                        progress_ratio_food.setText(foodPercent+" %" +" used of "+foodRatio + ". Status:");
                        status_Image_food.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_food.setText(foodPercent+" %" +" used of "+foodRatio + ". Status:");
                        status_Image_food.setImageResource(R.drawable.red);

                    }

                    float housingPercent = (housingTotal/housingRatio)*100;
                    if (housingPercent<50){
                        progress_ratio_housing.setText(housingPercent+" %" +" used of "+housingRatio + ". Status:");
                        status_Image_housing.setImageResource(R.drawable.green);
                    }else if (housingPercent >= 50 && housingPercent <100){
                        progress_ratio_housing.setText(housingPercent+" %" +" used of "+housingRatio + ". Status:");
                        status_Image_housing.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_housing.setText(housingPercent+" %" +" used of "+housingRatio + ". Status:");
                        status_Image_housing.setImageResource(R.drawable.red);

                    }

                    float entPercent = (entTotal/entRatio)*100;
                    if (entPercent<50){
                        progress_ratio_ent.setText(entPercent+" %" +" used of "+entRatio + ". Status:");
                        status_Image_ent.setImageResource(R.drawable.green);
                    }else if (entPercent >= 50 && entPercent <100){
                        progress_ratio_ent.setText(entPercent+" %" +" used of "+entRatio + ". Status:");
                        status_Image_ent.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_ent.setText(entPercent+" %" +" used of "+entRatio + ". Status:");
                        status_Image_ent.setImageResource(R.drawable.red);

                    }

                    float mediPercent = (mediTotal/mediRatio)*100;
                    if (mediPercent<50){
                        progress_ratio_medical.setText(mediPercent+" %" +" used of "+mediRatio + ". Status:");
                        status_Image_medical.setImageResource(R.drawable.green);
                    }
                    else if (mediPercent >= 50 && mediPercent <100){
                        progress_ratio_medical.setText(mediPercent+" %" +" used of "+mediRatio + ". Status:");
                        status_Image_medical.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_medical.setText(mediPercent+" %" +" used of "+mediRatio + ". Status:");
                        status_Image_medical.setImageResource(R.drawable.red);

                    }

                    float elecPercent = (elecTotal/elecRatio)*100;
                    if (elecPercent<50){
                        progress_ratio_electricity.setText(elecPercent+" %" +" used of "+elecRatio + ". Status:");
                        status_Image_electricity.setImageResource(R.drawable.green);
                    }else if (elecPercent >= 50 && elecPercent <100){
                        progress_ratio_electricity.setText(elecPercent+" %" +" used of "+elecRatio + ". Status:");
                        status_Image_electricity.setImageResource(R.drawable.brown);
                    }else {
                        progress_ratio_electricity.setText(elecPercent+" %" +" used of "+elecRatio + ". Status:");
                        status_Image_electricity.setImageResource(R.drawable.red);

                    }
                }
                else {
                    Toast.makeText(WeeklyAnalytics.this, "setStatusAndImageResource Errors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    }