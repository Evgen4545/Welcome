package com.boom.welcome.ui.data.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.boom.welcome.R;
import com.boom.welcome.ui.data.ListInfo;
import com.boom.welcome.ui.data.MaskTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;



public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>{




    private final LayoutInflater inflater;
    private final List<ListInfo> voiceInfoList;
    private  Context context;

    public AdapterList( Context context, List<ListInfo> voiceInfoList) {

        this.voiceInfoList = voiceInfoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.my_list, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListInfo list = voiceInfoList.get(position);
        holder.nameView.setText(list.getTurneket());
        holder.descriptionView.setText(list.getTime());

        final Transformation transformation = new MaskTransformation(context, R.drawable.rounded_convers_transformation);
        Picasso.get()
                .load(list.getPhoto())
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .transform(transformation)
                .into(holder.photo);



    }

    @Override
    public int getItemCount() {
        return voiceInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nameView,  descriptionView;
        final ImageView photo;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.turneket);
            descriptionView = view.findViewById(R.id.time);
            photo = view.findViewById(R.id.listImage);

        }
    }
}