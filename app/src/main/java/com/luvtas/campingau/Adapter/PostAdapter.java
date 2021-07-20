package com.luvtas.campingau.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.luvtas.campingau.Fragment.CheckUserProfileFragment;
import com.luvtas.campingau.Fragment.PostDetailFragment;
import com.luvtas.campingau.Fragment.ProfileFragment;
import com.luvtas.campingau.Model.PostModel;
import com.luvtas.campingau.Model.UserModel;
import com.luvtas.campingau.R;
import com.luvtas.campingau.Ui.CommentsActivity;
import com.luvtas.campingau.Ui.OpenImageActivity;
import com.luvtas.campingau.Ui.PostActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.text.DateFormat.getDateTimeInstance;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mContext;
    public List<PostModel> mPost;
    ImageView OpenImage;
    String publisher,uid_photo;

    private FirebaseUser firebaseUser;
    StorageReference storageReference;
    MediaController mediaController;

    public PostAdapter(Context mContext, List<PostModel> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.campsite_status, parent, false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                UserModel userModel = datasnapshot.getValue(UserModel.class);
                uid_photo = userModel.getUserimage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        PostModel postModel = mPost.get(position);

        publisher = postModel.getPublisher();
        if(postModel.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(postModel.getDescription());
        }
        holder.title.setText(postModel.getTitle());
        //holder.publisher.setText(postModel.getPublisher());
        holder.username.setText(postModel.getUsername());

        getUserInfo(holder.image_profile,holder.username,postModel.getPublisher());

//        Glide.with(mContext).load(postModel.getPostimage()).into(holder.post_image);

        //Glide.with(mContext).load(postModel.getProfile_image()).into(holder.image_profile);
        holder.time.setText(getTimeDate(postModel.getTime()));
        holder.campsite_kids.setText(postModel.getSub());
        isLiked(postModel.getPostid(), holder.like);
        nrLikes(holder.likes, postModel.getPostid());
        getComments(postModel.getPostid(), holder.comments);
        isSaved(postModel.getPostid(), holder.save);
        if(postModel.getBlue_check().equals("1")){
            holder.blue_check.setVisibility(View.VISIBLE);
        } else {
            holder.blue_check.setVisibility(View.GONE);
        }

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("publisher", postModel.getPublisher());
                editor.apply();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckUserProfileFragment()).commit();
            }
        });
//
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("publisher", postModel.getPublisher());
                editor.apply();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckUserProfileFragment()).commit();
            }
        });

//        holder.publisher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("profileid", postModel.getPublisher());
//                editor.apply();
//                //((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//            }
//        });

        if (postModel.getImageType().equals("image")) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.recyclerView.setAdapter(new ImageAdapter(postModel.getPostImages()));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelper = new PagerSnapHelper();
            if (holder.recyclerView.getOnFlingListener() == null)
                snapHelper.attachToRecyclerView(holder.recyclerView);
//            recyclerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
////                editor.putString("postid", postModel.getPostid());
////                editor.apply();
////                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment()).commit();
//                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), view, "sharedView");
//                    Intent intent = new Intent(mContext, OpenImageActivity.class);
//                    intent.putExtra("shareimage", postModel.getPostimage());
//                    intent.putExtra("shareImages", postModel.getPostImages().toArray(new String[0]));
//                    intent.putExtra("image_type", postModel.getImageType());
//                    mContext.startActivity(intent, activityOptionsCompat.toBundle());
//                }
//            });
            holder.R_layout_image.setVisibility(View.VISIBLE);
            holder.R_layout_video.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.previewImageView.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.GONE);
        } else if (postModel.getImageType().equals("video")) {
            holder.R_layout_image.setVisibility(View.GONE);
            holder.R_layout_video.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.previewImageView.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.GONE);
            Glide.with(mContext).load(postModel.getPostimage()).into(holder.previewImageView);
            try {
                if (mediaController == null) {
                    mediaController = new MediaController(mContext);
                }
                mediaController.setAnchorView(holder.videoView);
                Uri video_path = Uri.parse(postModel.getPostimage());
                holder.videoView.setMediaController(mediaController);
                holder.videoView.setVideoURI(video_path);
                holder.videoView.seekTo(3);
                holder.playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.previewImageView.setVisibility(View.GONE);
                        holder.playButton.setVisibility(View.GONE);
                        holder.videoView.start();
//                        OpenImage = (ImageView) v.findViewById(R.id.post_pic);
//                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, v, "sharedView");
//                        Intent intent = new Intent(mContext, OpenImageActivity.class);
//                        intent.putExtra("shareimage", postModel.getPostimage());
//                        intent.putExtra("image_type", postModel.getImageType());
//                        mContext.startActivity(intent, activityOptionsCompat.toBundle());
                    }
                });
//                holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        holder.playButton.setVisibility(View.VISIBLE);
//                    }
//                });
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.something_gone_wrong), Toast.LENGTH_SHORT).show();
            }
        }

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(postModel.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(postModel.getPostid()).removeValue();
                }
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postModel.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(postModel.getPublisher(),postModel.getPostid());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postModel.getPostid()).child(firebaseUser.getUid()).removeValue();
                }

            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", postModel.getPostid());
                intent.putExtra("publisherid", postModel.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid", postModel.getPostid());
                intent.putExtra("publisherid", postModel.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                editPost(postModel.getPostid());
                                return true;
                            case R.id.delete:
                                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mContext).create();
                                alertDialog.setTitle(getApplicationContext().getResources().getString(R.string.do_you_want_to_delete));
                                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, getApplicationContext().getResources().getString(R.string.no),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, getApplicationContext().getResources().getString(R.string.yes),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseDatabase.getInstance().getReference("Posts")
                                                        .child(postModel.getPostid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(mContext,getApplicationContext().getResources().getString(R.string.delete_post_complete), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                //deletePost(postModel.getPostid(), Gravity.BOTTOM);
                                return true;
                            case R.id.report:
                                Toast.makeText(mContext,"Report click", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if(!postModel.getPublisher().equals(firebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }
                popupMenu.show();
            }
        });



        //publisherinfo(holder.image_profile, holder.username, holder.publisher,postModel.getPublisher());
    }

//    private void deletePost(String postid, int gravity) {
//        final Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.delete_post);
//
//        Window window = dialog.getWindow();
//        if(window == null){
//            return;
//        }
//
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = gravity;
//        window.setAttributes(windowAttributes);
//
//        if (Gravity.BOTTOM == gravity){
//            dialog.setCancelable(true);
//        } else {
//            dialog.setCancelable(false);
//        }
//
//    }

    private void getUserInfo(final ImageView imageView, final TextView username, final String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                Glide.with(mContext).load(userModel.getUserimage()).into(imageView);
                //username.setText(userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String getTimeDate(long timeStamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timeStamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, like, comment, save,blue_check, more,previewImageView,playButton;
        public TextView username, likes, publisher, description, comments,title, time, campsite_kids;
        public VideoView videoView;
        public RelativeLayout R_layout_image, R_layout_video;
        public RecyclerView recyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            campsite_kids = itemView.findViewById(R.id.campsite_kids);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            image_profile = itemView.findViewById(R.id.image_profile);
//            post_image = itemView.findViewById(R.id.post_image);
            comments = itemView.findViewById(R.id.comments);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            //publisher = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
            blue_check = itemView.findViewById(R.id.blue_check);
            more = itemView.findViewById(R.id.more);
            videoView = itemView.findViewById(R.id.player_video);
            R_layout_image = itemView.findViewById(R.id.R_layout_image);
            R_layout_video = itemView.findViewById(R.id.R_layout_video);
            previewImageView = itemView.findViewById(R.id.previewImageView);
            playButton = itemView.findViewById(R.id.playButton);
            recyclerView = itemView.findViewById(R.id.rvImages);
        }
    }

    private void getComments(String postid, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText(getApplicationContext().getResources().getString(R.string.view_all) +" "+dataSnapshot.getChildrenCount() + " " + getApplicationContext().getResources().getString(R.string.comments));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLiked(String postid, final ImageView imageView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_favorite_in_24dp);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNotifications(String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("comment", " liked your post.");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);
        hashMap.put("time", ServerValue.TIMESTAMP);
        reference.push().setValue(hashMap);
    }

    private void nrLikes(final TextView likes, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+ " "+ getApplicationContext().getResources().getString(R.string.like));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isSaved(final String postid, final ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.ic_turned_in_black_24dp);
                    imageView.setTag("saved");
                } else {
                    imageView.setImageResource(R.drawable.ic_save_in_not_black_24dp);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void editPost(String postid){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(getApplicationContext().getResources().getString(R.string.edit_post));

        EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(layoutParams);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(getApplicationContext().getResources().getString(R.string.edit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("description", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Posts").child(postid).updateChildren(hashMap);
                    }
                });
        alertDialog.setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void getText(String postid, final EditText editText){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText.setText(dataSnapshot.getValue(PostModel.class).getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        List<String> imagesUrl;

        public ImageAdapter(List<String> imagesUrl) {
            this.imagesUrl = imagesUrl;
        }
        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
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
        public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_post_detail_row_item, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            Glide.with(viewHolder.itemView).load(imagesUrl.get(position)).optionalCenterCrop().into(viewHolder.imageView);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return imagesUrl.size();
        }
    }
//    private void publisherinfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserModel userModel = dataSnapshot.getValue(UserModel.class);
//                Glide.with(mContext).load(userModel.getUserimage()).into(image_profile);
//                username.setText(userModel.getUsername());
//                //publisher.setText(userModel.getUsername());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
}
