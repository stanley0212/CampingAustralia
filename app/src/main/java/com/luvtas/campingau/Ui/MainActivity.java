package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luvtas.campingau.Fragment.ExploreFragment;
import com.luvtas.campingau.Fragment.FavoriteFragment;
import com.luvtas.campingau.Fragment.HomeFragment;
import com.luvtas.campingau.Fragment.MessageFragment;
import com.luvtas.campingau.Fragment.ProfileFragment;
import com.luvtas.campingau.R;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle intent = getIntent().getExtras();
        if(intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profiled", publisher);
            editor.apply();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //Fragment fragment;
            switch (menuItem.getItemId())
            {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_explore:
                    fragment = new ExploreFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_post:
//                    fragment = new PostFragment();
//                    loadFragment(fragment);
                    startActivity(new Intent(MainActivity.this, PostActivity.class));
                    return true;
                case R.id.navigation_favorite: ;
                    fragment = new MessageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            if(fragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
