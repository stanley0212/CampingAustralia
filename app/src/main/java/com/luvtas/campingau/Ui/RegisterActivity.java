package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luvtas.campingau.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, email, password;
    Button btn_register;
    TextView txt_login;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.register_username);
        email = findViewById(R.id.register_user_email);
        password = findViewById(R.id.register_user_password);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, Login2Activity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.please_wait));
                progressDialog.show();

                String str_username = userName.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(RegisterActivity.this, getApplicationContext().getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length() < 8)
                {
                    Toast.makeText(RegisterActivity.this, getApplicationContext().getResources().getString(R.string.password_must_have_8_characters), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    register(str_username, str_email, str_password);
                }
            }
        });
    }

    private void register(final String username, final String email, final String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("email", email);
                            hashMap.put("password", password);
                            hashMap.put("username", username);
                            hashMap.put("bio","");
                            hashMap.put("userimage", "https://firebasestorage.googleapis.com/v0/b/campingau-6b84d.appspot.com/o/default_user.png?alt=media&token=6e91947a-d2c0-497d-93a0-953e86870c5b");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else{
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, getApplicationContext().getResources().getString(R.string.you_can_not_register_with_this_email_or_password), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
