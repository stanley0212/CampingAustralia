package com.luvtas.campingau.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Adapter.CamperSiteAdapter;
import com.luvtas.campingau.Adapter.MultiImageAdapter;
import com.luvtas.campingau.Model.CamperSiteModel;
import com.luvtas.campingau.Model.MultiImageModel;
import com.luvtas.campingau.Model.RatingModel;
import com.luvtas.campingau.Model.UserModel;
import com.luvtas.campingau.R;
import com.luvtas.campingau.Ui.CampSitePostActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExploreFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;


    private RecyclerView recyclerView;
    private CamperSiteAdapter camperSiteAdapter;
    private List<CamperSiteModel> camperSiteModel;
    EditText seatch_bar;
    private FirebaseUser firebaseUser;
    private String uid, username, comment;
    private float rating;

    private  MultiImageModel camperSiteModelMulitImage;


    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference_image = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference_image.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                uid = userModel.getId();
                username = userModel.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    
//    void onRaringButtonClick(){
//        showDialogRating();
//    }
//
//    private void showDialogRating() {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
//        builder.setTitle(getContext().getResources().getString(R.string.rating_campsite));
//        builder.setMessage(getContext().getResources().getString(R.string.please_fill_information));
//
//        View itemview = LayoutInflater.from(getContext()).inflate(R.layout.layout_rating, null);
//        RatingBar ratingBar = (RatingBar) itemview.findViewById(R.id.rating_bar);
//        EditText rating_comment = (EditText) itemview.findViewById(R.id.rating_comment);
//
//        builder.setView(itemview);
//
//        builder.setNegativeButton(getContext().getResources().getString(R.string.rating_cancel), (dialog, which) -> {
//            dialog.dismiss();
//        });
//        builder.setPositiveButton(getContext().getResources().getString(R.string.rating_ok), (dialog, which) -> {
//            RatingModel ratingModel = new RatingModel();
//            ratingModel.setName(username);
//            ratingModel.setUid(uid);
//            ratingModel.setComment(rating_comment.getText().toString());
//            ratingModel.setRatingValue(ratingBar.getRating());
//            Map<String,Object> serverTimeStamp = new HashMap<>();
//            serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
//            ratingModel.setCommentTimeStamp(serverTimeStamp);
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container,false);
        FloatingActionButton fab = view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CampSitePostFragment nextFrag= new CampSitePostFragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
//                        .addToBackStack(null)
//                        .commit();
                Intent intent = new Intent(getActivity(), CampSitePostActivity.class);
                startActivity(intent);
            }
        });

//        String key = getArguments().getString("Campsite");
//        camperSiteModelMulitImage = ViewModelProviders.of(this).get(MultiImageModel.class);
//        init();
//        camperSiteModelMulitImage.getMultiImageList(key).observe(getViewLifecycleOwner(), multiImageModels -> {
//            MultiImageAdapter multiImageAdapter = new MultiImageAdapter(getContext(), multiImageModels);
//        });


        recyclerView = view.findViewById(R.id.recycler_view_campsite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        seatch_bar = view.findViewById(R.id.search_info);
        camperSiteModel = new ArrayList<>();
        camperSiteAdapter = new CamperSiteAdapter(getContext(),camperSiteModel);
        recyclerView.setAdapter(camperSiteAdapter);

        readCampSite();

        seatch_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchCampSite(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void init() {
    }

    private void searchCampSite(final String s){
        //Query query = FirebaseDatabase.getInstance().getReference("Campsite").orderByChild("CamperSiteName").startAt(s).endAt(s+"\uf8ff");
        Query query = FirebaseDatabase.getInstance().getReference("Campsite").orderByChild("CamperSiteName");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                camperSiteModel.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CamperSiteModel camperSiteModel2 = snapshot.getValue(CamperSiteModel.class);
                    camperSiteModel.add(camperSiteModel2);
                }
                camperSiteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readCampSite(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campsite");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(seatch_bar.getText().toString().equals("")){
                    camperSiteModel.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        CamperSiteModel camperSiteModel1 = snapshot.getValue(CamperSiteModel.class);
                        camperSiteModel.add(camperSiteModel1);
                    }
                    camperSiteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
