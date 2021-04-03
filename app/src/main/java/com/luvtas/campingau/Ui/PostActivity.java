package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    private ImageView  image_added, profile_image,image_show;
    private String image_check;
    private TextView post, cancel, username;
    private EditText post_title, post_description;
    Dialog dialog;
    private Spinner spinner;

    private FirebaseUser firebaseUser;
    private String userid, currentname,currentProfileImage;

    private SlidrInterface slidrInterface;


    public PostActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        cancel = findViewById(R.id.cancel);
        image_added = findViewById(R.id.image_added);
        image_show = findViewById(R.id.image_show);
        post = findViewById(R.id.post);
        post_title = findViewById(R.id.post_title);
        post_description = findViewById(R.id.post_description);
        dialog = new Dialog(this);
        spinner = findViewById(R.id.spinner_sub);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userid = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                username.setText(userModel.getUsername());
                currentname = userModel.getUsername();
                currentProfileImage = userModel.getUserimage();
                Glide.with(PostActivity.this).load(userModel.getUserimage()).into(profile_image);
                //publisher.setText(userModel.getUsername());
                //image_photo = userModel.getUserimage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Spinner Choose
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

        storageReference = FirebaseStorage.getInstance().getReference("Posts");  //Firebase Storage Paths

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        image_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_check = "ok";
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(PostActivity.this);
            }
        });
        slidrInterface = Slidr.attach(this);  // 向右滑動 關閉 Activity
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
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
            progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.posting));
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

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                            String postid = reference.push().getKey();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("postid", postid);
                            hashMap.put("sub", spinner.getSelectedItem().toString());
                            hashMap.put("postimage", myUrl);
                            hashMap.put("description", post_description.getText().toString());
                            hashMap.put("title", post_title.getText().toString());
                            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            hashMap.put("time", ServerValue.TIMESTAMP);
                            hashMap.put("username", currentname);
                            hashMap.put("profile_image", currentProfileImage);

                            reference.child(postid).setValue(hashMap);
                            progressDialog.dismiss();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(PostActivity.this, getApplicationContext().getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(PostActivity.this, getApplicationContext().getResources().getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            image_show.setImageURI(imageUri);
        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.something_gone_wrong), Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(PostActivity.this, MainActivity.class));
//            finish();
        }
    }
}
