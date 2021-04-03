package com.luvtas.campingau.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Adapter.CamperSiteAdapter;
import com.luvtas.campingau.Model.CamperSiteModel;
import com.luvtas.campingau.R;
import com.luvtas.campingau.Ui.CampSitePostActivity;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    private RecyclerView recyclerView;
    private CamperSiteAdapter camperSiteAdapter;
    private List<CamperSiteModel> camperSiteModel;
    EditText seatch_bar;


    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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
                searchCampSite(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchCampSite(final String s){
        Query query = FirebaseDatabase.getInstance().getReference("Campsite").orderByChild("CamperSiteName").startAt(s).endAt(s+"\uf8ff");
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
