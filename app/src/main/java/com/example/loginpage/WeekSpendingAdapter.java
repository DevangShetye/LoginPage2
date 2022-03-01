package com.example.loginpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeekSpendingAdapter extends RecyclerView.Adapter<WeekSpendingAdapter.MyViewHolder> {

    private Context mContext;
    private List<Listexpensedata> myDataList;

    public WeekSpendingAdapter(Context mContext, List<Listexpensedata> listexpensedata) {
        this.mContext = mContext;
        this.myDataList = listexpensedata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout,parent,false);
        return new WeekSpendingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Listexpensedata listexpensedata = myDataList.get(position);
        holder.setItemAmount("Allocated amount Rs : " + listexpensedata.getAmount());
        holder.setitemDate("On : " + listexpensedata.getDate());
        holder.setItemName("Item Name : " + listexpensedata.getItem());
        holder.notes.setVisibility(View.GONE);
        switch (listexpensedata.getItem()) {
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

    /*@Override
    protected void onBindViewHolder(@NonNull ListExpenseActivity.MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Listexpensedata Listexpensedata) {

        holder.setItemAmount("Allocated amount Rs : " + Listexpensedata.getAmount());
        holder.setitemDate("On : " + Listexpensedata.getDate());
        holder.setItemName("Item Name : " + Listexpensedata.getItem());
        holder.notes.setVisibility(View.GONE);
        switch (Listexpensedata.getItem()) {
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

    }*/

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        /*public TextView item, amount, date, notes;
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes= itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.image_view);
        }*/

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

