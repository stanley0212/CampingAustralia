package com.luvtas.campingau.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Model.CommentModel;
import com.luvtas.campingau.Model.UserModel;
import com.luvtas.campingau.R;
import com.luvtas.campingau.Ui.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static java.text.DateFormat.getDateTimeInstance;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentModel> mComment;
    private FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<CommentModel> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final CommentModel commentModel = mComment.get(position);
        holder.comment.setText(commentModel.getComment());
        holder.time.setText(getTimeDate(commentModel.getTime()));
        getUserInfo(holder.image_profile,holder.username,commentModel.getPublisher());

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", commentModel.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", commentModel.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_profile;
        public TextView username, comment,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);
        }
    }

    public void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                Glide.with(mContext).load(userModel.getUserimage()).into(imageView);
                username.setText(userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String getTimeDate(long timeStamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timeStamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
}
