package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;


public class LoginPageActivity extends AppCompatActivity {
    private TextView Verifyname,Verifypassword;
    private Button VerifyLoginbutton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user==null){
                    startActivity(new Intent(LoginPageActivity.this,MainActivity.class));
                    finish();


                }

            }
        };
        setContentView(R.layout.activity_login_page);
        VerifyLoginbutton=(Button)findViewById(R.id.login_button);
        Verifyname=(EditText)findViewById(R.id.verify_user_name);
        Verifypassword=(EditText)findViewById(R.id.verify_password);

        mAuth=FirebaseAuth.getInstance();

        VerifyLoginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.login_button:
                        userLogin();
                }
            }
        });

    }

    private void userLogin() {
        
        String userName=Verifyname.getText().toString().trim();
        String password=Verifypassword.getText().toString().trim();

        if(userName.isEmpty()){
            Verifyname.setError("Registered Name is Required");
            Verifyname.requestFocus();
            return;
        }
        if(Patterns.EMAIL_ADDRESS.matcher(userName).matches()){
            Verifyname.setError("Please Enter a Valid Name");
            Verifyname.requestFocus();
            return;
        }
        if(password.isEmpty()){
            Verifypassword.setError("Password is Required");
            Verifypassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginPageActivity.this,"Failed to Login ! Please Check your Credentials",Toast.LENGTH_LONG).show();



                }else{
                    startActivity(new Intent(LoginPageActivity.this,MainActivity.class));


                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {

        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}