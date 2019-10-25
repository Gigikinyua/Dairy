package com.kinyua.dairy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kinyua.dairy.R;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private TextView forgotPass, createAccount;
    private Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebase auth instance
        auth = FirebaseAuth.getInstance();


        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        //set the view
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.emailedt);
        inputPassword = findViewById(R.id.passwrdedt);
        progressBar = findViewById(R.id.loginProgress);
        loginbtn = findViewById(R.id.btnlogin);
        forgotPass = findViewById(R.id.forgotpwd);
        createAccount = findViewById(R.id.createaccount);

        auth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUPActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter Email Address!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter Password!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Incorrect Email or Password",
                                            Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}
