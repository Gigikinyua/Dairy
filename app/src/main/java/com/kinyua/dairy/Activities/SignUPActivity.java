package com.kinyua.dairy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kinyua.dairy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class SignUPActivity extends AppCompatActivity {

    private EditText fname, lname, phone, email, uname, pswrd;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.signupProgress);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.emailedt);
        phone = findViewById(R.id.phone_number);
        uname = findViewById(R.id.username);
        pswrd = findViewById(R.id.passwrdedt);
        Button submit = findViewById(R.id.btnsignup);

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        String country;
        for (Locale loc : locales) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        final Spinner countrySpinner = findViewById(R.id.countrySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        countrySpinner.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = fname.getText().toString();
                final String lastname = lname.getText().toString();
                final String emailaddress = email.getText().toString();
                final String phonenum = phone.getText().toString();
                final String username = uname.getText().toString();
                final String password = pswrd.getText().toString();

                if (TextUtils.isEmpty(firstname)) {
                    fname.setError("Enter First Name!");
                } else if (TextUtils.isEmpty(lastname)) {
                    lname.setError("Enter Last Name!");
                }else if (TextUtils.isEmpty(username)) {
                    uname.setError("Enter Username!");
                }else if (username.length() < 5) {
                    uname.setError("Username must be longer than 5 characters");
                }else if (TextUtils.isEmpty(emailaddress)) {
                    email.setError("Enter Email Address!");
                }else if (TextUtils.isEmpty(phonenum)) {
                    phone.setError("Enter Phone Number!");
                }else if (phonenum.length() < 10) {
                    phone.setError("Enter Valid Phone Number!");
                }else if (phonenum.length() > 12) {
                    phone.setError("Enter Valid Phone Number!");
                }else if (TextUtils.isEmpty(password)) {
                    pswrd.setError("Enter Strong Password!");
                }else if (password.length() < 8) {
                    pswrd.setError("Username must be longer than 8 characters");
                } else {

                    progressBar.setVisibility(View.VISIBLE);

                    //create user
                    auth.createUserWithEmailAndPassword(emailaddress, password).addOnCompleteListener(SignUPActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();

                                        assert firebaseUser != null;
                                        final String userid = firebaseUser.getUid();
                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("firstname", firstname);
                                        hashMap.put("lastname", lastname);
                                        hashMap.put("username", username);
                                        hashMap.put("phonenumber", phonenum);
                                        hashMap.put("password", password);
                                        hashMap.put("email", emailaddress);
                                        hashMap.put("country", countrySpinner.getSelectedItem().toString());

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignUPActivity.this,
                                                        "createUserWithEmail: onComplete: " + task.isSuccessful(), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUPActivity.this, "create failed: " + task.getException(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUPActivity.this, "Authentication failed: " + task.getException(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
            }
        });
    }
}
