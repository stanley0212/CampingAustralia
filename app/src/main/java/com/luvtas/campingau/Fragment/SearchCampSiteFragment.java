package com.luvtas.campingau.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luvtas.campingau.Adapter.CamperSiteAdapter;
import com.luvtas.campingau.Model.CamperSiteModel;
import com.luvtas.campingau.R;


public class SearchCampSiteFragment extends Fragment {

    private RecyclerView recyclerView;
    private CamperSiteAdapter camperSiteAdapter;
    private CamperSiteModel camperSiteModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_camp_site, container,false);
        return view;



    }
}
