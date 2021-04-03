package com.luvtas.campingau.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luvtas.campingau.R;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

public class CampSitePostFragment extends Fragment {

    private ImageView upload_banner, campsite_image, campsite_post, back;
    private EditText campsite_name, campsite_address, campsite_info, campsite_description;
    private Uri imageUri;


    public CampSitePostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camp_site_post, container, false);

        campsite_image = view.findViewById(R.id.campsite_image);
        campsite_name = view.findViewById(R.id.campsite_name);
        campsite_address = view.findViewById(R.id.campsite_address);
        campsite_info = view.findViewById(R.id.campsite_info);
        campsite_description = view.findViewById(R.id.campsite_description);
        campsite_post = view.findViewById(R.id.campsite_post);
        upload_banner = view.findViewById(R.id.upload_banner);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExploreFragment pre = new ExploreFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pre ,"prefragment").addToBackStack(null).commit();
            }
        });

        upload_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(getActivity());
            }
        });

        campsite_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDetails();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Intent intent = CropImage.activity(imageUri).getIntent(getActivity());
        getActivity().startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.container);
        if (fragmentById != null) {
            fragmentById.onActivityResult(requestCode, resultCode, data);
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                campsite_image.setImageURI(imageUri);
            } else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(PostActivity.this, MainActivity.class));
//            finish();
            }
        }

    }


    private void postDetails() {

    }
}
