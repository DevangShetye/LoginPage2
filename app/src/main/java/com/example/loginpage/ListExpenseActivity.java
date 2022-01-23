package com.example.loginpage;

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
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListExpenseActivity extends AppCompatActivity {
    private FloatingActionButton fabbutton;
    public DatabaseReference budgetRef;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    private TextView totalBudgetAmountTextView;
    private TextView totalBudgetAmountLeftTextView;
    private RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_expense);

        mAuth= FirebaseAuth.getInstance();
        budgetRef= FirebaseDatabase.getInstance().getReference().child("Expense List").child(mAuth.getCurrentUser().getUid());
        loader=new ProgressDialog(this);
        totalBudgetAmountTextView= findViewById(R.id.totalBudgetAmountTextView);
        totalBudgetAmountLeftTextView=findViewById(R.id.totalBudgetAmountLeftTextView);
        recyclerview=findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int TotalAmount=0;
                int monthlylimit=30000;
                int monthlyolimit;



                for(DataSnapshot snap: snapshot.getChildren()){
                    Listexpensedata listexpensedata= snap.getValue(Listexpensedata.class);
                    TotalAmount+=listexpensedata.getAmount();
                    monthlyolimit=monthlylimit-TotalAmount;

                    String SumTotal = String.valueOf("Total Expense : Rs"+TotalAmount);
                    String SumrTotal = String.valueOf("Total Budget Left : Rs"+monthlyolimit);

                    totalBudgetAmountTextView.setText(SumTotal);

                   totalBudgetAmountLeftTextView.setText(SumrTotal);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fabbutton=findViewById(R.id.fabbutton);
        fabbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });
    }

    private void additem() {
        AlertDialog.Builder myDialog= new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.input_layout,null);
        myDialog.setView(myview);

        final AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner =myview.findViewById(R.id.itemspinner);
        final EditText amount = myview.findViewById(R.id.amount);
        final Button cancle= myview.findViewById(R.id.cancle);
        final Button save = myview.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String budgetAmount = amount.getText().toString();
                String budgetItem= itemSpinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(budgetAmount)){
                    amount.setError("Amount is required");
                    return;
                }
                if(budgetItem.equals(budgetAmount)){
                    Toast.makeText(ListExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }
                else{
                    loader.setMessage("Adding a Item");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String id= budgetRef.push().getKey();
                    DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal= Calendar.getInstance();
                    String date=dateFormat.format(cal.getTime());

                    MutableDateTime epoch=new MutableDateTime();
                    epoch.setTime(0);
                    DateTime now= new DateTime();
                    Months months = Months.monthsBetween(epoch,now);

                    Listexpensedata listexpensedata = new Listexpensedata(budgetItem,date,id,null,Integer.parseInt(budgetAmount),months.getMonths());
                    budgetRef.child(id).setValue(listexpensedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ListExpenseActivity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ListExpenseActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
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
        FirebaseRecyclerOptions<Listexpensedata>options=new FirebaseRecyclerOptions.Builder<Listexpensedata>().setQuery(budgetRef,Listexpensedata.class).build();


        FirebaseRecyclerAdapter<Listexpensedata,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Listexpensedata, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Listexpensedata Listexpensedata ) {
                holder.setItemAmount("Allocated amount Rs : "+ Listexpensedata.getAmount());
                holder.setitemDate("On : "+Listexpensedata.getDate());
                holder.setItemName("Item Name : "+Listexpensedata.getItem());
                holder.notes.setVisibility(View.GONE);
                switch (Listexpensedata.getItem()){
                    case "Food and Dining":
                        holder.itemImageView.setImageResource(R.drawable.food);
                        break;
                    case "Electricity and Gas":
                        holder.itemImageView.setImageResource(R.drawable.electricityngas);
                        break;
                    case "Housing":
                        holder.itemImageView.setImageResource(R.drawable.housing);
                        break;
                    case "Medical":
                        holder.itemImageView.setImageResource(R.drawable.medicalpng);
                        break;
                    case "Entertainment":
                        holder.itemImageView.setImageResource(R.drawable.entertainment);
                        break;
                }

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout,parent,false);


                return new MyViewHolder(view);
            }
        };
        recyclerview.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public ImageView itemImageView;
        public TextView notes;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            mview= itemView;
            itemImageView=itemView.findViewById(R.id.itemImageView);
            notes=itemView.findViewById(R.id.note);
        }


        public void setItemName(String itemName){
            TextView item=mview.findViewById(R.id.item);
            item.setText(itemName);

        }
        public void setItemAmount(String itemAmount){
            TextView amount=mview.findViewById(R.id.amount);
            amount.setText(itemAmount);

        }
        public void setitemDate(String itemDate){
            TextView date=mview.findViewById(R.id.date);
            date.setText(itemDate);

        }



    }
}
