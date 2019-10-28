package com.kinyua.dairy.Fragments;


import android.app.Activity;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kinyua.dairy.Activities.LoginActivity;
import com.kinyua.dairy.EdtProfileActivity;
import com.kinyua.dairy.R;

import java.io.IOException;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private TextView name, email, phone, uname;
    AppCompatImageView ppic;


    // Creating URI.
    private Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    private int Image_Request_Code = 7;

    private FirebaseAuth auth;

    private Button profilepic, edtprofile;
    String downloadUrl;


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
        profilepic = view.findViewById(R.id.profilepic);

        ppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FilePathUri != null) {
                    final StorageReference storageReference2nd = FirebaseStorage.getInstance().getReference().child("Profile Images").child("All_images" + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                    // Adding addOnSuccessListener to second StorageReference.
                    storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2nd.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downloadUrl = task.getResult().toString();
                                }
                            });
                        }
                    });
                }

                final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                reference.child("profilepic").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Profile Picture uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                profilepic.setVisibility(View.GONE);
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

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

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



                profilepic.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(),FilePathUri.toString() , Toast.LENGTH_SHORT).show();



            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}
