package com.kinyua.dairy.Fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinyua.dairy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    private EditText name, id, dob;
    private Calendar myCalendar;
    private Spinner type;

    private ProgressBar progressBar;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        name = view.findViewById(R.id.cowname);
        id = view.findViewById(R.id.cowid);
        progressBar = view.findViewById(R.id.cowProgress);

        type = view.findViewById(R.id.typeSpinner);
        String[] types = {"Cow", "Bull"};
        ArrayAdapter<String> typeadapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, types);
        type.setAdapter(typeadapter);

        myCalendar = Calendar.getInstance();
        dob = view.findViewById(R.id.cowdob);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }


        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button sbmt = view.findViewById(R.id.btnaddcow);
        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cname = name.getText().toString();
                final String cid = id.getText().toString();
                final String cdob = dob.getText().toString();

                if (TextUtils.isEmpty(cname)) {
                    name.setError("Enter Cow Name!");
                } else if (TextUtils.isEmpty(cid)) {
                    id.setError("Enter Cow Tag ID!");
                } else if (TextUtils.isEmpty(cdob)) {
                    dob.setError("Enter Cow Date of Birth!");
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();

                    final String userKey = firebaseUser.getUid();

                    databaseReference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String names = dataSnapshot.child("firstname").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cows").child(userKey);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Cow Name", cname);
                            hashMap.put("Tag Id", cid);
                            hashMap.put("Date Of Birth", cdob);
                            hashMap.put("Type", type.getSelectedItem().toString());
                            hashMap.put("Owner Name", names);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(),
                                            "CreateCowProfile: onComplete: " + task.isSuccessful(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }


                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return view;
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(myCalendar.getTime()));
    }

}
