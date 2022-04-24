package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LimitActivity extends AppCompatActivity {
   EditText MonthlyLimit;
   EditText MonthlyIncome;
   Button limitButton;
   ImageView gotoCategoryLimit;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_limit);



      MonthlyLimit=findViewById(R.id.MonthlyLimit);
      MonthlyIncome = findViewById(R.id.MonthlyIncome);

      limitButton=findViewById(R.id.limitButton);
      gotoCategoryLimit=findViewById(R.id.goto_categorieswise);
      gotoCategoryLimit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(),CategoriesLimitActivity.class);
            intent.putExtra("Monthlylimit",MonthlyLimit.getText().toString());
            startActivity(intent);
         }
      });
      limitButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if(!MonthlyLimit.getText().toString().trim().isEmpty()){
               Intent intent=new Intent(getApplicationContext(),CategoriesLimitActivity.class);
               intent.putExtra("Monthlylimit",MonthlyLimit.getText().toString());
               startActivity(intent);
            }else{
               Toast.makeText(LimitActivity.this,"Enter Your Monthly Limit..",Toast.LENGTH_SHORT).show();
            }

            if(!MonthlyIncome.getText().toString().trim().isEmpty()){
               Intent intent=new Intent(getApplicationContext(),CategoriesLimitActivity.class);
               intent.putExtra("Monthlylimit",MonthlyLimit.getText().toString());
               startActivity(intent);
            }else{
               Toast.makeText(LimitActivity.this,"Enter Your Monthly Income..",Toast.LENGTH_SHORT).show();
            }

         }
      });
   }
}