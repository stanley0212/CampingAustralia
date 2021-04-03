package com.luvtas.campingau.Ui;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.luvtas.campingau.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampSitePostActivity extends AppCompatActivity {
    private ImageView upload_banner, campsite_image, campsite_post, back;
    private EditText campsite_name, campsite_address, campsite_info, campsite_description;
    private Uri imageUri;
    private Spinner spinner;
    private String myUrl = "";
    private String image_check;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private ChipGroup chipGroup;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_site_post);

        campsite_image = findViewById(R.id.campsite_image);
        campsite_name = findViewById(R.id.campsite_name);
        campsite_address = findViewById(R.id.campsite_address);
        campsite_info = findViewById(R.id.campsite_info);
        campsite_description = findViewById(R.id.campsite_description);
        campsite_post = findViewById(R.id.campsite_post);
        upload_banner = findViewById(R.id.upload_banner);
        spinner = findViewById(R.id.spinner_sub);

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

        for (String camp : CampSite)
        {
            Chip chip = new Chip(this);
            chip.setText(camp);
            chip.setChipBackgroundColorResource(R.color.colorAccent);
            chip.setCloseIconVisible(true);
            chip.setTextColor(getResources().getColor(R.color.white));
            //chip.setTextAppearance(R.style.ChipTextAppearance);
            chip.setId(ViewCompat.generateViewId());

            // edit:
            chip.setCheckable(true);
            chipGroup.addView(chip);
        };

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
                CropImage.activity().setAspectRatio(1,1).start(CampSitePostActivity.this);
            }
        });

        campsite_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDetails();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            campsite_image.setImageURI(imageUri);
        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.something_gone_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void postDetails() {
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
        } else if(image_check == "ok") {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Posting...");
            progressDialog.show();

            if (imageUri != null) {
                final StorageReference filerefrence = storageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(imageUri));

                uploadTask = filerefrence.putFile(imageUri);
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

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Campsite");
                            String postid = reference.push().getKey();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("CamperSiteID", postid);
                            hashMap.put("CamperSiteSub", spinner.getSelectedItem().toString());
                            hashMap.put("CamperSiteImage", myUrl);
                            hashMap.put("CamperSiteDescription", campsite_description.getText().toString());
                            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            hashMap.put("username", FirebaseAuth.getInstance().getCurrentUser());
                            hashMap.put("CamperSiteAddress", campsite_address.getText().toString());
                            hashMap.put("CamperSiteInfo", campsite_info.getText().toString());
                            hashMap.put("CamperSiteName",campsite_name.getText().toString());
                            hashMap.put("CamperSiteSummary", chipGroup.getCheckedChipId());


                            reference.child(postid).setValue(hashMap);
                            progressDialog.dismiss();
                            startActivity(new Intent(CampSitePostActivity.this, MainActivity.class));
                            finish();
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
}
