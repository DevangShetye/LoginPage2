package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText InputNumber1,InputNumber2,InputNumber3,InputNumber4,InputNumber5,InputNumber6;
    Button buttongetotp;
    String getOtpBackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);
        InputNumber1=findViewById(R.id.IputOtp1);
        InputNumber2=findViewById(R.id.IputOtp2);
        InputNumber3=findViewById(R.id.IputOtp3);
        InputNumber4=findViewById(R.id.IputOtp4);
        InputNumber5=findViewById(R.id.IputOtp5);
        InputNumber6=findViewById(R.id.IputOtp6);

        TextView textView=findViewById(R.id.MobileText);
        textView.setText(String.format("+91-%s",getIntent().getStringExtra("mobile")));
        buttongetotp = findViewById(R.id.buttongetotp);
        getOtpBackend=getIntent().getStringExtra("backendotp");
        ProgressBar progressbarVerififyOtp=findViewById(R.id.progressbar_login);


        buttongetotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!InputNumber1.getText().toString().trim().isEmpty()&&!InputNumber2.getText().toString().trim().isEmpty()&&!InputNumber3.getText().toString().trim().isEmpty()&&!InputNumber4.getText().toString().trim().isEmpty()&&!InputNumber5.getText().toString().trim().isEmpty()&&!InputNumber6.getText().toString().trim().isEmpty()){
                    String enterOtpCode=InputNumber1.getText().toString()+
                            InputNumber2.getText().toString()+
                            InputNumber3.getText().toString()+
                            InputNumber4.getText().toString()+
                            InputNumber5.getText().toString()+
                            InputNumber6.getText().toString();
                    if(getOtpBackend!=null){
                        progressbarVerififyOtp.setVisibility(View.VISIBLE);
                        buttongetotp.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential Phoneauthcredential= PhoneAuthProvider.getCredential(getOtpBackend,enterOtpCode);
                        FirebaseAuth.getInstance().signInWithCredential(Phoneauthcredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbarVerififyOtp.setVisibility(View.GONE);
                                buttongetotp.setVisibility(View.VISIBLE);
                                if(task.isSuccessful()){
                                    Intent intent=new Intent(getApplicationContext(),LimitActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this,"Enter Correct OTP",Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    }else{
                        Toast.makeText(LoginActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(LoginActivity.this,"OTP VERIFIED",Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(LoginActivity.this,"Enter All Numbers",Toast.LENGTH_SHORT).show();
                }
            }
        });

        MoveCursor();
        TextView resendlabel=findViewById(R.id.ResendOtp);
        resendlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra("mobile"),
                        60, TimeUnit.SECONDS,
                        LoginActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {



                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {


                                Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onCodeSent(@NonNull String current_backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getOtpBackend=current_backendotp;
                                Toast.makeText(LoginActivity.this, "OTP Sended Successfully", Toast.LENGTH_SHORT).show();




                            }
                        });

            }
        });




    }



    private void MoveCursor() {
        InputNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    InputNumber2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        InputNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    InputNumber3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        InputNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    InputNumber4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        InputNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    InputNumber5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        InputNumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    InputNumber6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}