package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    EditText InputUserName,InputEmail,InputPassword;
    Button registerbuton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");


        InputUserName=findViewById(R.id.input_user_name);
        InputEmail=findViewById(R.id.input_email);
        InputPassword=findViewById(R.id.input_password);
        registerbuton=findViewById(R.id.register_button);
        registerbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!InputUserName.getText().toString().trim().isEmpty()){
                    if(!InputEmail.getText().toString().trim().isEmpty()){
                        if(!InputPassword.getText().toString().trim().isEmpty()){
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegistrationActivity.this,"Input Password",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegistrationActivity.this,"Input Email",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistrationActivity.this,"Input Username",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}