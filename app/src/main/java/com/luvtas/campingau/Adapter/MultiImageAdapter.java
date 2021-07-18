package com.luvtas.campingau.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.luvtas.campingau.Model.MultiImageModel;
import com.luvtas.campingau.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luvtas.campingau.Model.CamperSiteModel;

import java.util.List;

public class MultiImageAdapter extends RecyclerView.Adapter<MultiImageAdapter.MyViewHolder> {

    Context context;
    List<CamperSiteModel> camperSiteModels;

    public MultiImageAdapter(Context context, List<CamperSiteModel> multiImageModels) {
        this.context = context;
        this.camperSiteModels = multiImageModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.multi_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(camperSiteModels.get(position).getCamperSiteImage()).into(holder.campsite_image);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView campsite_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            campsite_image = (ImageView) itemView.findViewById(R.id.campsite_image);
        }
    }
}
