package com.example.loginpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {
      //  holder.location.setText(model.getLocation());

       //Glide.with(holder.img.getContext()).load(model.getImageUrl()).into(holder.img);
        Glide.with(holder.img.getContext()).load(model.getImageUrl()).into(holder.img);





    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singlerow,viewGroup,false);
        return new myviewholder(view);

    }

    class myviewholder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView location;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            location=(TextView)itemView.findViewById(R.id.locationtext);

        }
    }

}
