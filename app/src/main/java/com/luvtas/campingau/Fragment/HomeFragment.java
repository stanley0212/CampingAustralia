package com.luvtas.campingau.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.efaso.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Adapter.PostAdapter;
import com.luvtas.campingau.Model.CamperSiteModel;
import com.luvtas.campingau.Model.PostModel;
import com.luvtas.campingau.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostModel> postLists;
    private OnFragmentInteractionListener mListener;

    PlayerView playerView;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.toolbar_logo);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);     //返回上一頁用的 註記

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recyclerView.setAdapter(postAdapter);
        readPosts();
        //postAdapter = new PostAdapter(postLists,getActivity());
        AdmobNativeAdAdapter admobNativeAdAdapter=AdmobNativeAdAdapter.Builder.with(
                "ca-app-pub-3180077679928430/9830517747",//admob native ad id  ca-app-pub-3940256099942544/2247696110  //  my ca-app-pub-3180077679928430/9857460566
                postAdapter,//current adapter
                "custom"//Set the size "small", "medium" or "custom"
        ).adItemInterval(8)//Repeat interval
                .build();

        //Adding adapter to recyclerview
        recyclerView.setAdapter(admobNativeAdAdapter);

        return view;
    }

    private void readPosts(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                postLists.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostModel post = snapshot.getValue(PostModel.class);
                    postLists.add(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_search, menu);
//        menu.findItem(R.id.preview_page).setVisible(false);
//        menu.findItem(R.id.search).setVisible(false);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    fetchSearch(query);
                } else {
                    readPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    fetchSearch(newText);
                } else {
                    readPosts();
                }
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    private void fetchSearch(final String s) {
       // Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("description").startAt(s).endAt(s+"\uf8ff");
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("description");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postLists.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    PostModel postModel = snapshot.getValue(PostModel.class);
//                    postLists.add(postModel);
//                }
//                postAdapter.notifyDataSetChanged();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);
                    if(postModel.getDescription().toLowerCase().contains(s.toLowerCase())){
                        postLists.add(postModel);
                    }
                }
                postAdapter = new PostAdapter(getActivity(), postLists);
                postAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if(id == R.id.notification){
//            SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
////            editor.putString("postid", notificationModel.getPostid());
////            editor.apply();
//
//            ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
