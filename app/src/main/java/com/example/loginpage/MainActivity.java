package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText EnterNumber;
    Button GetOtpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EnterNumber = findViewById(R.id.input_mobile_number);
        GetOtpButton = findViewById(R.id.buttongetotp);
        final ProgressBar progressBar = findViewById(R.id.progressbar_otp_verification);

        GetOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!EnterNumber.getText().toString().trim().isEmpty()) {
                    if ((EnterNumber.getText().toString().trim()).length() == 10) {
                        progressBar.setVisibility(view.VISIBLE);
                        GetOtpButton.setVisibility(view.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber( "+91" + EnterNumber.getText().toString(),
                                60, TimeUnit.SECONDS,
                                MainActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(view.GONE);
                                        GetOtpButton.setVisibility(view.VISIBLE);


                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {

                                        progressBar.setVisibility(view.GONE);
                                        GetOtpButton.setVisibility(view.VISIBLE);

                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(view.GONE);
                                        GetOtpButton.setVisibility(view.VISIBLE);

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.putExtra("mobile", EnterNumber.getText().toString());
                                        intent.putExtra("backendotp", backendotp);
                                        startActivity(intent);

                                    }
                                });

                    } else {
                        Toast.makeText(MainActivity.this, "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}