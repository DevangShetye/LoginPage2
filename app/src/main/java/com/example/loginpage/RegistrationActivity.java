package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText InputUserName,InputEmail,InputPassword;
    private Button registerbuton;
    private TextView RegisterQn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        /*Toast.makeText(RegistrationActivity.this, "Firebase Connection Successful",Toast.LENGTH_LONG).show();
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");
        //myRef.setValue("Hello, World!");
        //Calling our firebase database i.e root node
        rootNode = FirebaseDatabase.getInstance();
        //To call specific nodes of the database
        reference = rootNode.getReference().child("registeredUsers");*/

        InputUserName=(EditText) findViewById(R.id.input_user_name);
        InputEmail=(EditText) findViewById(R.id.input_email);
        InputPassword=(EditText) findViewById(R.id.input_password);
        registerbuton=findViewById(R.id.register_button);
        RegisterQn = findViewById(R.id.RegisterQn);

        RegisterQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginPage.class);
                startActivity(intent);
            }
        });


        registerbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting data from EditText into String variable
                final String userName = InputUserName.getText().toString();
                final String email = InputEmail.getText().toString();
                final String password = InputPassword.getText().toString();

                if(!InputUserName.getText().toString().trim().isEmpty()){
                    if(!InputEmail.getText().toString().trim().isEmpty()){
                        if(!InputPassword.getText().toString().trim().isEmpty()){
                            Intent intent=new Intent(getApplicationContext(),LoginPage.class);
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

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    UserHelperClass helperClass = new UserHelperClass(userName, email, password);
                                    FirebaseDatabase.getInstance().getReference("Registered_Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegistrationActivity.this, "You are successfully registered", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(RegistrationActivity.this, "Registration Failed! Try Again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(RegistrationActivity.this, "Registration Failed! Try Again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });



                //Calling the UserHelperClass
                //reference.child(userName).setValue(helperClass);
            }
        });
    }
}