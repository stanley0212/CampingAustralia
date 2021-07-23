package com.luvtas.campingau.Ui;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.luvtas.campingau.Model.UserModel;
import com.luvtas.campingau.R;
import com.luvtas.campingau.Util.RealPathUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.luvtas.campingau.Util.RealPathUtil.getDownsizedImageBytes;

public class CampSitePostActivity extends AppCompatActivity {
    private ImageView upload_banner, campsite_image, campsite_post, back;
    private EditText campsite_name, campsite_info, campsite_description;
    private Spinner spinner;
    private String myUrl = "";
    private String image_check;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private ChipGroup chipGroup;
    public Button btnShowResult;
    public ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    Dialog dialog;

    private FirebaseUser firebaseUser;
    private String userid, currentname,currentProfileImage;
    private TextView username,campsite_address,place_name,place_latlng;
    private ImageView profile_image;

    private Place placeSelected;
    private AutocompleteSupportFragment places_fragment;
    private PlacesClient placesClient;
    private List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG);

    RecyclerView recyclerView;
    static List<Uri> imageListUriLocal;
    String[] imagesArrayUri;
    int maxSelectedPics = 20;
    int PICK_MULTI_IMAGE_REQUEST = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_site_post);

        Places.initialize(getApplicationContext(),"AIzaSyBgwE1bLrIS9RIGJb5ZYetDTwr614aQF90");

        campsite_image = findViewById(R.id.campsite_image);
        campsite_name = findViewById(R.id.campsite_name);
        campsite_info = findViewById(R.id.campsite_info);
        campsite_description = findViewById(R.id.campsite_description);
        campsite_post = findViewById(R.id.campsite_post);
        upload_banner = findViewById(R.id.upload_banner);
        spinner = findViewById(R.id.spinner_sub);
        place_name = findViewById(R.id.place_name);
        place_latlng = findViewById(R.id.place_latlng);
        recyclerView = findViewById(R.id.rvCampsiteImage);

//        campsite_address.setFocusable(false);
//        campsite_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(CampSitePostActivity.this);
//                startActivityForResult(intent,999);
//            }
//        });




        campsite_address = findViewById(R.id.campsite_address);
        places_fragment = (AutocompleteSupportFragment)getSupportFragmentManager()
                .findFragmentById(R.id.places_autocomplete_fragment);
        ImageView searchIcon = (ImageView)((LinearLayout)places_fragment.getView()).getChildAt(0);
        // Set the desired icon
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_black_24dp));

        // Set the desired behaviour on click
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CampSitePostActivity.this, "YOUR DESIRED BEHAVIOUR HERE", Toast.LENGTH_SHORT).show();
            }
        });

        places_fragment.setPlaceFields(placeFields);
        places_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                placeSelected = place;
                campsite_address.setText(place.getAddress());
                place_name.setText(place.getName());
                place_latlng.setText(String.valueOf(place.getLatLng()));
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(CampSitePostActivity.this,""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //ExploreFragment  Campsite 選項陣列
        String[] CampSite = new String[]{getApplicationContext().getResources().getString(R.string.campsite_wifi),
                getApplicationContext().getResources().getString(R.string.campsite_wheelchair),
                getApplicationContext().getResources().getString(R.string.campsite_water_from_tap),
                getApplicationContext().getResources().getString(R.string.campsite_toilet),
                getApplicationContext().getResources().getString(R.string.campsite_swimming_pool),
                getApplicationContext().getResources().getString(R.string.campsite_surface),
                getApplicationContext().getResources().getString(R.string.campsite_suit_any_car),
                getApplicationContext().getResources().getString(R.string.campsite_pet_welcome),
                getApplicationContext().getResources().getString(R.string.campsite_laundromat),
                getApplicationContext().getResources().getString(R.string.campsite_large_vehicle_access),
                getApplicationContext().getResources().getString(R.string.campsite_kitchen),
                getApplicationContext().getResources().getString(R.string.campsite_internet),
                getApplicationContext().getResources().getString(R.string.campsite_household_power),
                getApplicationContext().getResources().getString(R.string.campsite_hot_shower),
                getApplicationContext().getResources().getString(R.string.campsite_dump_station),
                getApplicationContext().getResources().getString(R.string.campsite_credit_card),
                getApplicationContext().getResources().getString(R.string.campsite_cellular_signal),
                getApplicationContext().getResources().getString(R.string.campsite_caravan_power),
                getApplicationContext().getResources().getString(R.string.campsite_cabin),
                getApplicationContext().getResources().getString(R.string.campsite_bed_supplied),
                getApplicationContext().getResources().getString(R.string.campsite_bbq),
        };

        chipGroup = findViewById(R.id.CampSite);

//        for (String camp : CampSite)
//        {
//            Chip chip = new Chip(this);
//            chip.setText(camp);
//            //chip.setChipBackgroundColorResource(R.color.colorAccent);
//            chip.setCloseIconVisible(true);
////            chip.setIconStartPadding(3f);
////            chip.setPadding(60, 10, 60, 10);
//            chip.setTextColor(getResources().getColor(R.color.black));
//            //chip.setTextAppearance(R.style.ChipTextAppearance);
//            chip.setId(ViewCompat.generateViewId());
//
//            // edit:
//            chip.setCheckable(true);
//            chipGroup.addView(chip);
//        };

        // OnclickChange item to check
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                group.isClickable();
            }
        });

        // Campsite Sub ArrayList
        List<String> sub = new ArrayList<>();
        sub.add(0, getApplicationContext().getResources().getString(R.string.choose_sub));
        sub.add(getApplicationContext().getResources().getString(R.string.vic));
        sub.add(getApplicationContext().getResources().getString(R.string.nsw));
        sub.add(getApplicationContext().getResources().getString(R.string.qld));
        sub.add(getApplicationContext().getResources().getString(R.string.tas));
        sub.add(getApplicationContext().getResources().getString(R.string.act));
        sub.add(getApplicationContext().getResources().getString(R.string.nt));
        sub.add(getApplicationContext().getResources().getString(R.string.sa));
        sub.add(getApplicationContext().getResources().getString(R.string.wa));
        ArrayAdapter<String> dataSpinnerAdapter;
        dataSpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sub);
        dataSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals(getApplicationContext().getResources().getString(R.string.choose_sub))){
                    //do nothing
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(),"Selected : "+item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("CampSitePhotos");  //Firebase Storage Paths


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upload_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_check = "ok";
//                CropImage.activity().setAspectRatio(1,1).start(CampSitePostActivity.this);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select images"), PICK_MULTI_IMAGE_REQUEST);
            }
        });

        campsite_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageListUriLocal.size() > 0) {
                    imagesArrayUri = new String[imageListUriLocal.size()];
                    postDetails(0);
                }

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                currentname = userModel.getUsername();
                currentProfileImage = userModel.getUserimage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setAdapter(new CustomAdapter());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        imageListUriLocal = new ArrayList<>(maxSelectedPics);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_MULTI_IMAGE_REQUEST) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        imageListUriLocal.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else if (data.getData() != null) {
                    imageListUriLocal.add(Uri.fromFile(new File(new RealPathUtil().getRealPathFromURI(this, data.getData()))));
                    //TODO: do something
                }
            }
            if (recyclerView.getAdapter() != null)
                recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void postDetails(int index) {
        Uri imageUri = imageListUriLocal.get(index);
        if(spinner.getSelectedItem().toString().equals(getApplicationContext().getResources().getString(R.string.choose_sub))) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(getApplicationContext().getResources().getString(R.string.alert_error))
                    .setMessage(getApplicationContext().getResources().getString(R.string.choose_sub))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            alertDialog.show();
        } else if(image_check.equals("ok")) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Posting...");
            progressDialog.show();

            if (imageUri != null) {
                final StorageReference filerefrence = storageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(imageUri));
                // scaling the image
                int scaleDivider = 1;
                try {
                    Bitmap fullBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    int scaleWidth = fullBitmap.getWidth() / scaleDivider;
                    int scaleHeight = fullBitmap.getHeight() / scaleDivider;
                    byte[] downsizedImageBytes =
                            getDownsizedImageBytes(fullBitmap, scaleWidth, scaleHeight);

                    uploadTask = filerefrence.putBytes(downsizedImageBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filerefrence.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Object downloadUri = task.getResult();
                            myUrl = downloadUri.toString();
                            imagesArrayUri[index] = myUrl;
                            progressDialog.dismiss();
                            if (index == imageListUriLocal.size() - 1) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campsite");
                                String postid = reference.push().getKey();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("CamperSiteID", postid);
                                hashMap.put("CamperSiteSub", spinner.getSelectedItem().toString());
                                hashMap.put("CamperSiteImages", Arrays.asList(imagesArrayUri));
                                hashMap.put("CamperSiteDescription", campsite_description.getText().toString());
                                hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                hashMap.put("username", currentname);
                                hashMap.put("CamperSiteAddress", campsite_address.getText().toString());
                                hashMap.put("CamperSiteInfo", campsite_info.getText().toString());
                                hashMap.put("CamperSiteName",campsite_name.getText().toString());
                                hashMap.put("ServerTimeStamp", ServerValue.TIMESTAMP);
                                hashMap.put("CamperSiteSummary", getChipList());
                                hashMap.put("CamperSiteLatLng", place_latlng.getText().toString());
                                if (placeSelected != null) {
                                    hashMap.put("CamperSiteLongitude", placeSelected.getLatLng().longitude);
                                    hashMap.put("CamperSiteLatitude", placeSelected.getLatLng().latitude);
                                }

                                reference.child(postid).setValue(hashMap);
                                imageListUriLocal.clear();
                                startActivity(new Intent(CampSitePostActivity.this, MainActivity.class));
                                finish();
                            } else {
                                postDetails(index + 1);
                            }
                        } else {
                            Toast.makeText(CampSitePostActivity.this, getApplicationContext().getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CampSitePostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CampSitePostActivity.this, getApplicationContext().getResources().getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
            }
        } else {
            alertError();
        }
    }

    private void alertError() {
        dialog.setContentView(R.layout.error_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
        dialog.show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        static class ViewHolder extends RecyclerView.ViewHolder {
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
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_row_item_campsite, viewGroup, false);

            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            layoutParams.width = screenWidth / 5;
            layoutParams.height = screenWidth / 5;
            view.setLayoutParams(layoutParams);

            return new CustomAdapter.ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            Glide.with(viewHolder.itemView).load(imageListUriLocal.get(position)).optionalCenterCrop().into(viewHolder.imageView);
            viewHolder.itemView.findViewById(R.id.ivDelete).setOnClickListener(v -> {
                imageListUriLocal.remove(position);
                notifyDataSetChanged();
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return imageListUriLocal.size();
        }
    }

    public List<Integer> getChipList() {
        HashMap<Integer, Integer> campSiteMap = new HashMap<>();
        List<Integer> camperSiteSummary = new ArrayList<>();

        campSiteMap.put(R.id.chip1, 1);
        campSiteMap.put(R.id.chip2, 2);
        campSiteMap.put(R.id.chip3, 3);
        campSiteMap.put(R.id.chip4, 4);
        campSiteMap.put(R.id.chip5, 5);
        campSiteMap.put(R.id.chip6, 6);
        campSiteMap.put(R.id.chip7, 7);
        campSiteMap.put(R.id.chip8, 8);
        campSiteMap.put(R.id.chip9, 9);
        campSiteMap.put(R.id.chip10, 10);
        campSiteMap.put(R.id.chip11, 11);
        campSiteMap.put(R.id.chip12, 12);
        campSiteMap.put(R.id.chip13, 13);
        campSiteMap.put(R.id.chip14, 14);
        campSiteMap.put(R.id.chip15, 15);
        campSiteMap.put(R.id.chip16, 16);
        campSiteMap.put(R.id.chip17, 17);
        campSiteMap.put(R.id.chip18, 18);
        campSiteMap.put(R.id.chip19, 19);
        campSiteMap.put(R.id.chip20, 20);
        campSiteMap.put(R.id.chip21, 21);

        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id : ids){
            camperSiteSummary.add(campSiteMap.get(id));
        }

        return camperSiteSummary;
    }
}
