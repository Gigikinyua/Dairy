package com.kinyua.dairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kinyua.dairy.Activities.MainActivity;

import java.util.HashMap;

public class EdtProfileActivity extends AppCompatActivity {
    EditText fname, lname, username, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_profile);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone_number);
        password = findViewById(R.id.passwrdedt);

        Button sbmt = findViewById(R.id.btnedit);
        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = fname.getText().toString();
                final String lastname = lname.getText().toString();
                final String phonenum = phone.getText().toString();
                final String usrname = username.getText().toString();
                final String psswrd = password.getText().toString();

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                assert firebaseUser != null;
                final String userid = firebaseUser.getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                if (!TextUtils.isEmpty(firstname)) {
                    reference.child("firstname").setValue(firstname);
                }
                if (!TextUtils.isEmpty(lastname)) {
                    reference.child("lastname").setValue(lastname);
                }
                if (!TextUtils.isEmpty(phonenum)) {
                    reference.child("phonenumber").setValue(phonenum);
                }
                if (!TextUtils.isEmpty(usrname)) {
                    reference.child("username").setValue(usrname);
                }
                if (!TextUtils.isEmpty(psswrd)) {
                    reference.child("password").setValue(psswrd);
                }

                startActivity(new Intent(EdtProfileActivity.this, MainActivity.class));
                finish();

            }
        });
    }
}
