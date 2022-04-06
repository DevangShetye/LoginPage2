package com.example.loginpage;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ThisWeek extends AppCompatActivity {

    private TextView totalWeekAmountTextView;
    private TextView totalWeekAmountLeftTextView;
    private ProgressBar progressBar;
    private RecyclerView recyclerview;

    private WeekSpendingAdapter weekSpendingAdapter;
    private List<Listexpensedata> myDataList;

    public DatabaseReference budgetRef;
    private FirebaseAuth mAuth;
    String onlineUserId = "";

    private String type = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_week);

        totalWeekAmountTextView = findViewById(R.id.totalWeekAmountTextView);
        totalWeekAmountLeftTextView = findViewById(R.id.totalWeekAmountLeftTextView);
        progressBar = findViewById(R.id.progressBar);
        recyclerview = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);

        mAuth= FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();


        myDataList = new ArrayList<>();
        weekSpendingAdapter = new WeekSpendingAdapter(ThisWeek.this, myDataList);
        recyclerview.setAdapter(weekSpendingAdapter);

       /* if(getIntent().getExtras()!=null){
            type = getIntent().getStringExtra("type");
            if(type.equals("week")){
                readWeekSpendingItems();
            }
            else if(type.equals("month")){
                readMonthSpendingItems();
            }
        }*/
        readWeekSpendingItems();
    }


    private void readWeekSpendingItems() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setTime(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        budgetRef = FirebaseDatabase.getInstance().getReference().child("Expense List").child(mAuth.getCurrentUser().getUid());
        Query query = budgetRef.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Listexpensedata listexpensedata = dataSnapshot.getValue(Listexpensedata.class);
                    myDataList.add(listexpensedata);
                }
                weekSpendingAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                int TotalAmount=0;
                int monthlylimit=30000;
                int monthlyolimit;



                for(DataSnapshot snap: snapshot.getChildren()){
                    Listexpensedata listexpensedata= snap.getValue(Listexpensedata.class);
                    TotalAmount+=listexpensedata.getAmount();
                    monthlyolimit=monthlylimit-TotalAmount;

                    String SumTotal = String.valueOf("Total Week's Spending : Rs"+TotalAmount);
                    String SumrTotal = String.valueOf("Total Budget Left : Rs"+monthlyolimit);

                    totalWeekAmountTextView.setText(SumTotal);

                    totalWeekAmountLeftTextView.setText(SumrTotal);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}