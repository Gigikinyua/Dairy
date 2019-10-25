package com.kinyua.dairy.Fragments;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kinyua.dairy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private DatabaseReference mDataRef;
    private RecyclerView mRecycleView;
    private CardView list;
    private TextView name, id, owner, dob, type;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        name = view.findViewById(R.id.cownamedisply);
        id = view.findViewById(R.id.cowiddisply);
        dob = view.findViewById(R.id.cowdobdisply)


        return view;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
////        FirebaseRecyclerAdapter<DataClass,MyViewHolder> adapter = new FirebaseRecyclerAdapter<DataClass, MyViewHolder>(
////                DataClass.class, R.layout.input_data, MyViewHolder.class, mDataRef
////        ){
////
////        }
//    }

}
