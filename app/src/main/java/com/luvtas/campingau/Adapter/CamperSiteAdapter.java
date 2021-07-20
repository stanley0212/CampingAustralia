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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.text.DateFormat.getDateTimeInstance;

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
//        viewHolder.btn_follow.setVisibility(View.VISIBLE);
        viewHolder.campsite_name.setText(camperSiteModel.getCamperSiteName());
        viewHolder.campsite_sub.setText(camperSiteModel.getCamperSiteSub());
        viewHolder.campsite_address.setText(camperSiteModel.getCamperSiteAddress());
        viewHolder.description.setText(camperSiteModel.getCamperSiteDescription());
        viewHolder.time.setText(getTimeDate(camperSiteModel.getServerTimeStamp()));
        viewHolder.campsite_latlng.setText(camperSiteModel.getCamperSiteLatLng());

        viewHolder.recyclerViewInner.setAdapter(new PostAdapter.ImageAdapter(camperSiteModel.getCamperSiteImages()));
        viewHolder.recyclerViewInner.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        if (viewHolder.recyclerViewInner.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(viewHolder.recyclerViewInner);

        showChips(viewHolder.chipGroup, camperSiteModel.getCamperSiteSummary());
//        //isFollowing(camperSiteModel.getCamperSiteID(),viewHolder.btn_follow);
//
//        if(camperSiteModel.getCamperSiteID().equals(firebaseUser.getUid())){
//            viewHolder.btn_follow.setVisibility(View.GONE);
//        }
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("profileid", camperSiteModel.getCamperSiteID());
//                editor.apply();
//
//                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//            }
//        });

//        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewHolder.btn_follow.getText().toString().equals("follow")){
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(camperSiteModel.getCamperSiteID()).setValue(true);
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("followers").child(camperSiteModel.getCamperSiteID()).setValue(true);
//                } else {
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(camperSiteModel.getCamperSiteID()).removeValue();
//                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("followers").child(camperSiteModel.getCamperSiteID()).removeValue();
//                }
//            }
//        });

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

    @Override
    public int getItemCount() {
        return camperSiteModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView campsite_name, campsite_sub, campsite_address,description,time,campsite_latlng;
        private ImageView campsite_image;
        private Button btn_follow;
        RecyclerView recyclerViewInner;
        ChipGroup chipGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            campsite_name = itemView.findViewById(R.id.campsite_name);
            campsite_sub = itemView.findViewById(R.id.campsite_sub);
            campsite_address = itemView.findViewById(R.id.campsite_address);
//            //btn_follow = itemView.findViewById(R.id.btn_follow);
            campsite_image = itemView.findViewById(R.id.image);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);
            campsite_latlng = itemView.findViewById(R.id.campsite_latlng);
            recyclerViewInner = itemView.findViewById(R.id.rvImageCampsite);
            chipGroup = itemView.findViewById(R.id.CampSite);
        }
    }

    static class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        List<String> imagesUrl;

        public ImageAdapter(List<String> imagesUrl) {
            this.imagesUrl = imagesUrl;
        }
        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.ivPreview);
            }

            public ImageView getImageView() {
                return imageView;
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_post_detail_row_item, viewGroup, false);

            return new ImageAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            Glide.with(viewHolder.itemView).load(imagesUrl.get(position)).optionalCenterCrop().into(viewHolder.imageView);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return imagesUrl.size();
        }
    }

    public void showChips(ChipGroup chipGroup, List<Integer> chipList) {
        HashMap<Integer, Integer> campSiteMap = new HashMap<>();

        campSiteMap.put(1, R.id.chip1);
        campSiteMap.put(2, R.id.chip2);
        campSiteMap.put(3, R.id.chip3);
        campSiteMap.put(4, R.id.chip4);
        campSiteMap.put(5, R.id.chip5);
        campSiteMap.put(6, R.id.chip6);
        campSiteMap.put(7, R.id.chip7);
        campSiteMap.put(8, R.id.chip8);
        campSiteMap.put(9, R.id.chip9);
        campSiteMap.put(10, R.id.chip10);
        campSiteMap.put(11, R.id.chip11);
        campSiteMap.put(12, R.id.chip12);
        campSiteMap.put(13, R.id.chip13);
        campSiteMap.put(14, R.id.chip14);
        campSiteMap.put(15, R.id.chip15);
        campSiteMap.put(16, R.id.chip16);
        campSiteMap.put(17, R.id.chip17);
        campSiteMap.put(18, R.id.chip18);
        campSiteMap.put(19, R.id.chip19);
        campSiteMap.put(20, R.id.chip20);
        campSiteMap.put(21, R.id.chip21);

        try {
            for (Integer id : chipList){
                chipGroup.findViewById(campSiteMap.get(id)).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            // TODO: get no id
        }

    }


//    private void isFollowing(final String userid, final Button button){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(userid).exists()){
//                    button.setText("following");
//                } else {
//                    button.setText("follow");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
