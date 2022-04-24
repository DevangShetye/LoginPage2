package com.example.loginpage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SuggestionActivity extends AppCompatActivity {
    ArrayList suggestion;
    TextView suggestionText;
    ImageView imagev;
    Button getsuggestion;
    private FirebaseAuth mAuth;
    private DatabaseReference personalRef,budgetRef;
    LinearLayout layout;
    int a=3900;
    int food=3000;
    int electricity=3000;
    int house=4000;
    int medical=3200;
    int entertainment=3900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        ArrayList<String>suggestion=new ArrayList<>();
        ArrayList<Integer>itemsAmount=new ArrayList<>();
        suggestion.add("Hey your expense looks great expect for your Housing expense. It's off the charts");
        suggestion.add("Seems like you have exceeded your Clothing Limit..!");
        suggestion.add("Seems like you have exceeded your Medical Limit..!");
        suggestion.add("Seems like you have exceeded your Electricity & Gas Limit..!");
        suggestion.add("You have exceeded your Food Limit ....want to RESET..?");
        suggestion.add("Your All Over Expense Looks Good and Under Control Keep it up");


        suggestion.add("You have Exceeded 50% of your Housing Limit");
        suggestion.add("You have Exceeded 50% of your Clothing Limit");
        suggestion.add("You have Exceeded 50% of your Medical Limit");
        suggestion.add("You have Exceeded 50% of your Electricity & Gas  Limit");
        suggestion.add("You have Exceeded 50% of your Food Limit");
        suggestionText=findViewById(R.id.suggest_Text);
        mAuth=FirebaseAuth.getInstance();
        budgetRef= FirebaseDatabase.getInstance().getReference().child("Expense List").child(mAuth.getCurrentUser().getUid());
        layout = findViewById(R.id.suggest_card);
        imagev=findViewById(R.id.st1);
        getsuggestion=findViewById(R.id.add_suggestions);
        getsuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(foodt>food & electricityt>electricity & house<houset){
                    addsuggestions(suggestion.get(0));
                    addsuggestions(suggestion.get(1));
                    addsuggestions(suggestion.get(2));
                }else if(electricityt>electricity){
                    addsuggestions(suggestion.get(1));


                }*/
                Query query = budgetRef.orderByChild("item").equalTo("Housing");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int pTotal = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            pTotal += Integer.parseInt(String.valueOf(total));
                        }
                        int monthHouseRatio = pTotal;
                        itemsAmount.add(monthHouseRatio);
                        //addsuggestions(itemsAmount.get(0));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query query1 = budgetRef.orderByChild("item").equalTo("Entertainment");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int eTotal = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            eTotal += Integer.parseInt(String.valueOf(total));
                        }
                        int monthEntRatio = eTotal;
                        itemsAmount.add(monthEntRatio);
                       // addsuggestions(itemsAmount.get(1));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query query2 = budgetRef.orderByChild("item").equalTo("Medical");
                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int mTotal = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            mTotal += Integer.parseInt(String.valueOf(total));
                        }
                        int monthMedicalRatio = mTotal;
                        itemsAmount.add(monthMedicalRatio);
                       // addsuggestions(itemsAmount.get(2));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                Query query3 = budgetRef.orderByChild("item").equalTo("Electricity and Gas");
                query3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int gTotal = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            gTotal += Integer.parseInt(String.valueOf(total));
                        }
                        int monthElecRatio = gTotal;
                        itemsAmount.add(monthElecRatio);
                        //addsuggestions(itemsAmount.get(3));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Query query4 = budgetRef.orderByChild("item").equalTo("Food and Dining");
                query4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int fTotal = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            Object total = map.get("amount");
                            fTotal += Integer.parseInt(String.valueOf(total));
                        }
                        int monthFoodRatio = fTotal;
                        itemsAmount.add(monthFoodRatio);
                     //   addsuggestions(itemsAmount.get());
                        //+++++++++++++++++++++Conditions ++++++++++++++++++++++++++++1)house2)entertainment3)medical4)gas5)food
                        if(itemsAmount.get(0)> house){
                            addsuggestions(suggestion.get(0));
                        }
                        if(itemsAmount.get(0)> house/2){
                            addsuggestions(suggestion.get(6));
                        }
                        if(itemsAmount.get(1)>entertainment){
                            addsuggestions(suggestion.get(1));
                        }
                        if(itemsAmount.get(1)>entertainment/2){
                            addsuggestions(suggestion.get(7));
                        }

                        if(itemsAmount.get(2)>medical){
                            addsuggestions(suggestion.get(2));
                        }
                        if(itemsAmount.get(2)>medical/2){
                            addsuggestions(suggestion.get(8));
                        }
                        if(itemsAmount.get(3)>electricity){
                            addsuggestions(suggestion.get(3));
                        }
                        if(itemsAmount.get(3)>electricity/2){
                            addsuggestions(suggestion.get(9));
                        }
                        if(itemsAmount.get(4)>food){
                            addsuggestions(suggestion.get(4));
                        }
                        if(itemsAmount.get(4)>food/2){
                            addsuggestions(suggestion.get(10));
                        }
                        if(itemsAmount.get(0)< house&itemsAmount.get(1)<entertainment&itemsAmount.get(2)<medical&itemsAmount.get(3)<electricity&itemsAmount.get(4)<food){
                            addsuggestions(suggestion.get(5));
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


        });


    }
    private void addsuggestions(String suggestion) {
        final View view = getLayoutInflater().inflate(R.layout.suggestion_layout, null);
        ImageView abc=view.findViewById(R.id.st1);
        abc.setImageResource(R.drawable.warning);
        TextView randomtext=view.findViewById(R.id.suggest_Text);
        randomtext.setText(suggestion);
        layout.addView(view);
    }


}