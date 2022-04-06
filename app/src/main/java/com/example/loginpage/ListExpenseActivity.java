package com.example.loginpage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ListExpenseActivity extends AppCompatActivity {
    private FloatingActionButton fabbutton;
    public DatabaseReference budgetRef, personalRef;
    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private ProgressDialog loader;
    private String post_key = "";
    private String updateitem = "";
    private int updateamount = 0;


    private TextView totalBudgetAmountTextView;
    private TextView totalBudgetAmountLeftTextView;
    private RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_expense);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        String currentUserUid = mAuth.getCurrentUser().getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference().child("Expense List").child(mAuth.getCurrentUser().getUid());
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(mAuth.getCurrentUser().getUid());
        loader = new ProgressDialog(this);
        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);
        totalBudgetAmountLeftTextView = findViewById(R.id.totalBudgetAmountLeftTextView);
        recyclerview = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int TotalAmount = 0;
                int monthlylimit = 30000;
                int monthlyolimit;


                for (DataSnapshot snap : snapshot.getChildren()) {
                    Listexpensedata listexpensedata = snap.getValue(Listexpensedata.class);
                    TotalAmount += listexpensedata.getAmount();
                    monthlyolimit = monthlylimit - TotalAmount;

                    String SumTotal = String.valueOf("Total Expense : Rs" + TotalAmount);
                    String SumrTotal = String.valueOf("Total Budget Left : Rs" + monthlyolimit);

                    totalBudgetAmountTextView.setText(SumTotal);

                    totalBudgetAmountLeftTextView.setText(SumrTotal);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fabbutton = findViewById(R.id.fabbutton);
        fabbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    int totalammount = 0;

                    for (DataSnapshot snap : snapshot.getChildren()) {

                        Listexpensedata listexpensedata = snap.getValue(Listexpensedata.class);

                        totalammount += listexpensedata.getAmount();

                        String sttotal = String.valueOf("Month Budget: " + totalammount);

                        totalBudgetAmountTextView.setText(sttotal);

                    }
                    int weeklyBudget = totalammount / 4;
                    int dailyBudget = totalammount / 30;
                    personalRef.child("budget").setValue(totalammount);
                    personalRef.child("weeklyBudget").setValue(weeklyBudget);
                    personalRef.child("dailyBudget").setValue(dailyBudget);

                } else {
                    personalRef.child("budget").setValue(0);
                    personalRef.child("weeklyBudget").setValue(0);
                    personalRef.child("dailyBudget").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getMonthFoodBudgetRatios();
        getMonthHousingBudgetRatios();
        getMonthEntBudgetRatios();
        getMonthMedicalBudgetRatios();
        getMonthElecBudgetRatios();


    }

    private void additem() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myview);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myview.findViewById(R.id.itemspinner);
        final EditText amount = myview.findViewById(R.id.amount);
        final Button cancle = myview.findViewById(R.id.cancle);
        final Button save = myview.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetAmount = amount.getText().toString();
                String budgetItem = itemSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(budgetAmount)) {
                    amount.setError("Amount is required");
                    return;
                }
                if (budgetItem.equals(budgetAmount)) {
                    Toast.makeText(ListExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                } else {
                    loader.setMessage("Adding a Item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id = budgetRef.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime epoch = new MutableDateTime();
                    epoch.setTime(0);
                    DateTime now = new DateTime();
                    Weeks weeks = Weeks.weeksBetween(epoch, now);
                    Months months = Months.monthsBetween(epoch, now);

                    String itemNday = budgetItem + date;
                    String itemNweek = budgetItem + weeks.getWeeks();
                    String itemNmonth = budgetItem + months.getMonths();

                    Listexpensedata listexpensedata = new Listexpensedata(budgetItem, date, id, itemNday, itemNweek, itemNmonth, Integer.parseInt(budgetAmount), months.getMonths(), weeks.getWeeks(), null);
                    budgetRef.child(id).setValue(listexpensedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ListExpenseActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ListExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();

                        }
                    });
                }
                dialog.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerOptions<Listexpensedata> options = new FirebaseRecyclerOptions.Builder<Listexpensedata>().setQuery(budgetRef, Listexpensedata.class).build();
        FirebaseRecyclerAdapter<Listexpensedata, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Listexpensedata, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Listexpensedata listexpensedata) {

                if(listexpensedata.id == null){
                    return;
                }
                holder.setItemAmount("Allocated amount Rs : " + listexpensedata.getAmount());
                holder.setitemDate("On : " + listexpensedata.getDate());
                holder.setItemName("Item Name : " + listexpensedata.getItem());
                holder.notes.setVisibility(View.GONE);
                switch (listexpensedata.getItem()) {
                    case "Food and Dining":
                        holder.itemImageView.setImageResource(R.drawable.diet);
                        break;
                    case "Electricity and Gas":
                        holder.itemImageView.setImageResource(R.drawable.chargingstation);
                        break;
                    case "Housing":
                        holder.itemImageView.setImageResource(R.drawable.flash);
                        break;
                    case "Medical":
                        holder.itemImageView.setImageResource(R.drawable.capsules);
                        break;
                    case "Entertainment":
                        holder.itemImageView.setImageResource(R.drawable.popcorn);

                        break;
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        updateitem = listexpensedata.getItem();
                        updateamount = listexpensedata.getAmount();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout, parent, false);


                return new MyViewHolder(view);
            }
        };
        recyclerview.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mview;
        public ImageView itemImageView;
        public TextView notes;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            mview = itemView;
            itemImageView = itemView.findViewById(R.id.itemImageView);
            notes = itemView.findViewById(R.id.note);
        }


        public void setItemName(String itemName) {
            TextView item = mview.findViewById(R.id.item);
            item.setText(itemName);

        }

        public void setItemAmount(String itemAmount) {
            TextView amount = mview.findViewById(R.id.amount);
            amount.setText(itemAmount);

        }

        public void setitemDate(String itemDate) {
            TextView date = mview.findViewById(R.id.date);
            date.setText(itemDate);

        }


    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_exppense_layout, null);

        myDialog.setView(mView);
        final AlertDialog dialog = myDialog.create();

        final TextView mItem = mView.findViewById(R.id.updateItemName);
        final EditText mAmount = mView.findViewById(R.id.updateamount);
        final EditText mNotes = mView.findViewById(R.id.updatenote);

        mNotes.setVisibility(View.GONE);

        mItem.setText(updateitem);

        mAmount.setText(String.valueOf(updateamount));
        mAmount.setSelection(String.valueOf(updateamount).length());

        Button delBut = mView.findViewById(R.id.btnDelete);
        Button btnUpdate = mView.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateamount = Integer.parseInt(mAmount.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime epoch = new MutableDateTime();
                epoch.setTime(0);
                DateTime now = new DateTime();
                Weeks weeks = Weeks.weeksBetween(epoch, now);
                Months months = Months.monthsBetween(epoch, now);

                String itemNday = updateitem + date;
                String itemNweek = updateitem + weeks.getWeeks();
                String itemNmonth = updateitem + months.getMonths();

                Listexpensedata listexpensedata = new Listexpensedata(updateitem, date, post_key, itemNday, itemNweek, itemNmonth, updateamount, months.getMonths(), weeks.getWeeks(), null);
                budgetRef.child(post_key).setValue(listexpensedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ListExpenseActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                dialog.dismiss();

            }
        });

        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetRef.child(post_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ListExpenseActivity.this, "Deleted  successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getMonthFoodBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Food and Dining");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayFoodRatio = pTotal / 30;
                    int weekFoodRatio = pTotal / 4;
                    int monthFoodRatio = pTotal;

                    personalRef.child("dayFoodRatio").setValue(dayFoodRatio);
                    personalRef.child("weekFoodRatio").setValue(weekFoodRatio);
                    personalRef.child("monthFoodRatio").setValue(monthFoodRatio);


                } else {
                    personalRef.child("dayFoodRatio").setValue(0);
                    personalRef.child("weekFoodRatio").setValue(0);
                    personalRef.child("monthFoodRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthHousingBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Housing");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayHouseRatio = pTotal / 30;
                    int weekHouseRatio = pTotal / 4;
                    int monthHouseRatio = pTotal;

                    personalRef.child("dayHouseRatio").setValue(dayHouseRatio);
                    personalRef.child("weekHouseRatio").setValue(weekHouseRatio);
                    personalRef.child("monthHouseRatio").setValue(monthHouseRatio);

                } else {
                    personalRef.child("dayHouseRatio").setValue(0);
                    personalRef.child("weekHouseRatio").setValue(0);
                    personalRef.child("monthHouseRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthEntBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Entertainment");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayEntRatio = pTotal / 30;
                    int weekEntRatio = pTotal / 4;
                    int monthEntRatio = pTotal;

                    personalRef.child("dayEntRatio").setValue(dayEntRatio);
                    personalRef.child("weekEntRatio").setValue(weekEntRatio);
                    personalRef.child("monthEntRatio").setValue(monthEntRatio);

                } else {
                    personalRef.child("dayEntRatio").setValue(0);
                    personalRef.child("weekEntRatio").setValue(0);
                    personalRef.child("monthEntRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthMedicalBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Medical");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayMedicalRatio = pTotal / 30;
                    int weekMedicalRatio = pTotal / 4;
                    int monthMedicalRatio = pTotal;

                    personalRef.child("dayMedicalRatio").setValue(dayMedicalRatio);
                    personalRef.child("weekMedicalRatio").setValue(weekMedicalRatio);
                    personalRef.child("monthMedicalRatio").setValue(monthMedicalRatio);

                } else {
                    personalRef.child("dayMedicalRatio").setValue(0);
                    personalRef.child("weekMedicalRatio").setValue(0);
                    personalRef.child("monthMedicalRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMonthElecBudgetRatios() {
        Query query = budgetRef.orderByChild("item").equalTo("Electricity and Gas");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int pTotal = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        pTotal = Integer.parseInt(String.valueOf(total));
                    }

                    int dayElecRatio = pTotal / 30;
                    int weekElecRatio = pTotal / 4;
                    int monthElecRatio = pTotal;

                    personalRef.child("dayElecRatio").setValue(dayElecRatio);
                    personalRef.child("weekElecRatio").setValue(weekElecRatio);
                    personalRef.child("monthElecRatio").setValue(monthElecRatio);

                } else {
                    personalRef.child("dayElecRatio").setValue(0);
                    personalRef.child("weekElecRatio").setValue(0);
                    personalRef.child("monthElecRatio").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
