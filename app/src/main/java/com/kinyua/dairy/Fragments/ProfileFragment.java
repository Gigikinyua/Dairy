package com.kinyua.dairy.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.kinyua.dairy.Activities.LoginActivity;
import com.kinyua.dairy.EdtProfileActivity;
import com.kinyua.dairy.R;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private TextView name, email, phone, uname;
    AppCompatImageView ppic;

    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "All_Image_Uploads_Database";
    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    int Image_Request_Code = 7;

    FirebaseAuth auth;

    Button edtprofile;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.name);
        uname = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        ppic = view.findViewById(R.id.profilephoto);

        ppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        edtprofile = view.findViewById(R.id.edtprofile);
        edtprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EdtProfileActivity.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            final String userKey = user.getUid();

            databaseReference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String names = dataSnapshot.child("firstname").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString();
                    String usrname = dataSnapshot.child("username").getValue().toString();
                    String emailaddress = dataSnapshot.child("email").getValue().toString();
                    String phonenum = dataSnapshot.child("phonenumber").getValue().toString();

                    name.setText(names);
                    uname.setText(usrname);
                    email.setText(emailaddress);
                    phone.setText(phonenum);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        TextView logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                ppic.setImageBitmap(bitmap);



            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}
