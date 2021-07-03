package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.Common.Common;
import com.luvtas.campingau.R;
import com.luvtas.campingau.Remote.ICloudFunctions;
import com.luvtas.campingau.Remote.RetrofitICloudClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_register;
    ImageView facebook_login,google_login;
    TextView txt_forgetpassword;
    LoginButton loginButton;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    AccessTokenTracker accessTokenTracker;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    private static final String TAG = "FacebookAuthentication";

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //firebaseAuth.addAuthStateListener(authStateListener);

        // redirect if user if not null
        if(firebaseUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(authStateListener != null)
//            firebaseAuth.removeAuthStateListener(authStateListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        txt_forgetpassword = findViewById(R.id.txt_forget_password);
        facebook_login = findViewById(R.id.facebook_login);
        google_login = findViewById(R.id.google_login);


        //Facebook SDK init
        FacebookSdk.sdkInitialize(LoginActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "onSuccess" + loginResult);
                        handleFacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "onError" + error);
                    }
                });

                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(user != null){
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                };

                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if(currentAccessToken == null)
                            firebaseAuth.signOut();
                    }
                };

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Login2Activity.class));
                //finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                //finish();
            }
        });

        txt_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                //finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookToken" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Sign in with credential: Successful");
                    firebaseUser = firebaseAuth.getCurrentUser();
                    updateUI(firebaseUser);
                } else {
                    Log.d(TAG, "Sign in with credential: failure" + task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            firebaseUser = firebaseAuth.getCurrentUser();
            String userid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", userid);
            hashMap.put("email", firebaseUser.getEmail());
            hashMap.put("username", firebaseUser.getDisplayName());
            hashMap.put("userimage", firebaseUser.getPhotoUrl());

            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }

}
