package com.kinyua.dairy.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kinyua.dairy.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ImageUploadInfo> mainImageUploadInfoList;

    public RecyclerViewAdapter(Context context,List<ImageUploadInfo> TempList) {

        this.mainImageUploadInfoList = TempList;

        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_data, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = mainImageUploadInfoList.get(position);

        holder.cowname.setText(UploadInfo.getCowName());
        holder.cowid.setText(UploadInfo.getCowId());
        holder.cowowner.setText(UploadInfo.getCowOwner());
        holder.dob.setText(UploadInfo.getCowDOB());

        //Loading image from Glide library.
//        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return mainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        public ImageView imageView;
        TextView cowname, cowid, cowowner, dob;

        ViewHolder(View itemView) {
            super(itemView);

            cowname = itemView.findViewById(R.id.cownamedisply);
            cowid = itemView.findViewById(R.id.cowiddisply);
            dob = itemView.findViewById(R.id.cowdobdisply);
            cowowner = itemView.findViewById(R.id.cowownerdisply);

//            imageView = itemView.findViewById(R.id.imageView);
//
//
        }
    }

//    public void updateWith(){
//        this.mainImageUploadInfoList = TempList;
//        notifyDataSetChanged();
//    }
}