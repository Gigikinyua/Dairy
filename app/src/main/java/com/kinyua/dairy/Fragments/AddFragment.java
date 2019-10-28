package com.kinyua.dairy.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.kinyua.dairy.Helpers.ImageUploadInfo;
import com.kinyua.dairy.R;

import java.io.IOException;
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
    private ImageView imageView;

    // Creating URI.
    Uri FilePathUri;

    String downloadUrl;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    int Image_Request_Code = 7;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();

        name = view.findViewById(R.id.cowname);
        id = view.findViewById(R.id.cowid);
        progressBar = view.findViewById(R.id.cowProgress);
        imageView = view.findViewById(R.id.cowphoto);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

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
                    final DatabaseReference databaseReference = firebaseDatabase.getReference();

                    final String userKey = firebaseUser.getUid();

                    databaseReference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String names = dataSnapshot.child("firstname").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString();

                            if (FilePathUri != null) {
                                final StorageReference storageReference2nd = storageReference.child("Cow_Images").child("All_images" + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                                // Adding addOnSuccessListener to second StorageReference.
                                storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference2nd.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                downloadUrl = task.getResult().toString();
//                                                Toast.makeText(getActivity(), downloadUrl, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cows");

                            String uploadId = reference.push().getKey();

                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(cname, cid, names, cdob, downloadUrl);



//                                HashMap<String, String> hashMap = new HashMap<>();
//
//                                hashMap.put("Cow photo", downloadUrl);
//                                hashMap.put("Cow Name", cname);
//                                hashMap.put("Tag Id", cid);
//                                hashMap.put("Date Of Birth", cdob);
//                                hashMap.put("Type", type.getSelectedItem().toString());
//                                hashMap.put("Owner Name", names);
//
//
//                                reference.child(uploadId).setValue(hashMap);

                            progressBar.setVisibility(View.GONE);

                            reference.child(uploadId).setValue(imageUploadInfo);
                            Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
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

    // Creating Method to get the selected image file Extension from File Path URI.
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
                imageView.setImageBitmap(bitmap);

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}
