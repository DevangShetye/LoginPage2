package com.example.loginpage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //add firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference personalRef,budgetRef;
    private String onlineUserId = "";

    private Float foodData,HousingData,EntertainmentData,MedicalData,ElectricityData;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //declare the database refrence object
        mAuth=FirebaseAuth.getInstance();
        int updateamount = 0;
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(mAuth.getCurrentUser().getUid());
        budgetRef= FirebaseDatabase.getInstance().getReference().child("Expense List").child(mAuth.getCurrentUser().getUid());
        //
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        Button thisweekbtn = (Button)view.findViewById(R.id.thisweekbtn);
        Button thismonthbtn = (Button)view.findViewById(R.id.thismonthbtn);
        BarChart barChart=(BarChart)view.findViewById(R.id.barChart);
        ArrayList<BarEntry> visitors=new ArrayList<>();
       //////////////////////////////////////////////////////////////////////////////
        Query query = budgetRef.orderByChild("item").equalTo("Housing");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal += Integer.parseInt(String.valueOf(total));
                    }
                    int monthHouseRatio = pTotal;
                    visitors.add(new BarEntry(2014,pTotal));

                BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData =new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setText("Chart");
                barChart.animateY(2000);
                    personalRef.child("monthHouseRatio").setValue(monthHouseRatio);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query1 = budgetRef.orderByChild("item").equalTo("Entertainment");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int eTotal = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    eTotal += Integer.parseInt(String.valueOf(total));
                }
                int monthEntRatio = eTotal;
                visitors.add(new BarEntry(2015,eTotal));

                BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData =new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setText("Chart");
                barChart.animateY(2000);
                personalRef.child("monthEntRatio").setValue(monthEntRatio);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query2 = budgetRef.orderByChild("item").equalTo("Medical");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int mTotal = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    mTotal += Integer.parseInt(String.valueOf(total));
                }
                int monthMedicalRatio = mTotal;
                visitors.add(new BarEntry(2016,mTotal));

                BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData =new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setText("Chart");
                barChart.animateY(2000);
                personalRef.child("monthMedicalRatio").setValue(monthMedicalRatio);;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query3 = budgetRef.orderByChild("item").equalTo("Electricity and Gas");
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int gTotal = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    gTotal += Integer.parseInt(String.valueOf(total));
                }
                int monthElecRatio = gTotal;
                visitors.add(new BarEntry(2017,gTotal));

                BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData =new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setText("Chart");
                barChart.animateY(2000);
                personalRef.child("monthElecRatio").setValue(monthElecRatio);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query4 = budgetRef.orderByChild("item").equalTo("Food and Dining");
        query4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int fTotal = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    fTotal += Integer.parseInt(String.valueOf(total));
                }
                int monthFoodRatio = fTotal;
                visitors.add(new BarEntry(2018,fTotal));

                BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData =new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setText("Chart");
                barChart.animateY(2000);
                personalRef.child("monthFoodRatio").setValue(monthFoodRatio);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










        thisweekbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ThisWeek.class);
                //in.putExtra("type","week");
                startActivity(intent);
            }
        });

        thismonthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ThisMonth.class);
                //in.putExtra("type","month");
                startActivity(intent);
            }
        });

        return view;
    }

    /*private void showBarData(DataSnapshot snapshot) {


        for(DataSnapshot ds:snapshot.getChildren()){
            CategoriesTotal categoriesTotal=new CategoriesTotal();
            categoriesTotal.setMonthElecRatio(ds.getValue(CategoriesTotal.class).getMonthElecRatio());
            categoriesTotal.setMonthFoodRatio(ds.getValue(CategoriesTotal.class).getMonthFoodRatio());
            categoriesTotal.setMonthHouseRatio(ds.getValue(CategoriesTotal.class).getMonthHouseRatio());
            categoriesTotal.setMonthMedicalRatio(ds.getValue(CategoriesTotal.class).getMonthMedicalRatio());
            categoriesTotal.setMonthEntRatio(ds.getValue(CategoriesTotal.class).getMonthEntRatio());


            ArrayList<BarEntry> visitors=new ArrayList<>();
            visitors.add(new BarEntry(2014, Float.parseFloat(categoriesTotal.getMonthElecRatio())));
            visitors.add(new BarEntry(2015, Float.parseFloat(categoriesTotal.getMonthFoodRatio())));
            visitors.add(new BarEntry(2016, Float.parseFloat(categoriesTotal.getMonthEntRatio())));
            visitors.add(new BarEntry(2017, Float.parseFloat(categoriesTotal.getMonthHouseRatio())));
            visitors.add(new BarEntry(2018, Float.parseFloat(categoriesTotal.getMonthMedicalRatio())));
            BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                visitors.add(new BarEntry(2014,234));
                visitors.add(new BarEntry(2015,364));
                visitors.add(new BarEntry(2016,632));
                visitors.add(new BarEntry(2017,842));
                visitors.add(new BarEntry(2018,135));
                BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData =new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setText("Chart");
                barChart.animateY(2000);

                //showBarData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        }
    }*/


}