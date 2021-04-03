package com.luvtas.campingau.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luvtas.campingau.Fragment.PostDetailFragment;
import com.luvtas.campingau.Model.PostModel;
import com.luvtas.campingau.R;

import java.util.List;

public class MyFotoAdapter extends RecyclerView.Adapter<MyFotoAdapter.ViewHolder> {

    private Context context;
    private List<PostModel> mPost;

    public MyFotoAdapter(Context context, List<PostModel> mPost) {
        this.context = context;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fotos_item, parent,false);
        return new MyFotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PostModel postModel = mPost.get(position);
        Glide.with(context).load(postModel.getPostimage()).into(holder.post_image);
        holder.campsite_kids.setText(postModel.getSub());

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", postModel.getPostid());
                editor.apply();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_image;
        TextView campsite_kids;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.post_image);
            campsite_kids = itemView.findViewById(R.id.campsite_kids);
        }
    }
}
