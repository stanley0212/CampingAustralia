package com.luvtas.campingau.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Fragment.ProfileFragment;
import com.luvtas.campingau.Model.CamperSiteModel;
import com.luvtas.campingau.R;

import java.util.List;

public class CamperSiteAdapter extends RecyclerView.Adapter<CamperSiteAdapter.ViewHolder> {

    private Context mContext;
    private List<CamperSiteModel> camperSiteModels;
    FirebaseUser firebaseUser;

    public CamperSiteAdapter(Context mContext, List<CamperSiteModel> camperSiteModels) {
        this.mContext = mContext;
        this.camperSiteModels = camperSiteModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.campsite_list, parent, false);

        return new CamperSiteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final CamperSiteModel camperSiteModel = camperSiteModels.get(position);
        viewHolder.btn_follow.setVisibility(View.VISIBLE);
        viewHolder.campsite_name.setText(camperSiteModel.getCamperSiteName());
        viewHolder.campsite_sub.setText(camperSiteModel.getCamperSiteSub());
        viewHolder.campsite_address.setText(camperSiteModel.getCamperSiteAddress());

        Glide.with(mContext).load(camperSiteModel.getCamperSiteImage()).into(viewHolder.campsite_image);
        isFollowing(camperSiteModel.getCamperSiteID(),viewHolder.btn_follow);

        if(camperSiteModel.getCamperSiteID().equals(firebaseUser.getUid())){
            viewHolder.btn_follow.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", camperSiteModel.getCamperSiteID());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });

        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.btn_follow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(camperSiteModel.getCamperSiteID()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("followers").child(camperSiteModel.getCamperSiteID()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(camperSiteModel.getCamperSiteID()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("followers").child(camperSiteModel.getCamperSiteID()).removeValue();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView campsite_name, campsite_sub, campsite_address;
        private ImageView campsite_image;
        private Button btn_follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            campsite_name = itemView.findViewById(R.id.campsite_name);
            campsite_sub = itemView.findViewById(R.id.campsite_sub);
            campsite_address = itemView.findViewById(R.id.campsite_address);
            btn_follow = itemView.findViewById(R.id.btn_follow);
            campsite_image = itemView.findViewById(R.id.campsite_image);

        }
    }


    private void isFollowing(final String userid, final Button button){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).exists()){
                    button.setText("following");
                } else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}