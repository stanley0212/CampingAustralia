package com.luvtas.campingau.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.luvtas.campingau.Adapter.NotificationFragmentAdapter;
import com.luvtas.campingau.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClickBottomNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClickBottomNotificationFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView backfragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClickBottomNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClickBottomNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClickBottomNotificationFragment newInstance(String param1, String param2) {
        ClickBottomNotificationFragment fragment = new ClickBottomNotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_click_bottom_notification, container,false);
//        backfragment = view.findViewById(R.id.backfragment);
//        backfragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().popBackStack();
//            }
//        });


        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        NotificationFragmentAdapter notificationFragmentAdapter = new NotificationFragmentAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        notificationFragmentAdapter.addFragment(new MessageFragment(),getActivity().getResources().getString(R.string.b_message));
        notificationFragmentAdapter.addFragment(new NotificationFragment(), getActivity().getResources().getString(R.string.b_notification));
        viewPager.setAdapter(notificationFragmentAdapter);
        // Inflate the layout for this fragment
        return view;
    }
}
