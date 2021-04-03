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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.R;

public class Login2Activity extends AppCompatActivity {
    EditText email, password;
    Button btn_login;
    TextView txt_signup;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        email = findViewById(R.id.login_user_email);
        password = findViewById(R.id.login_user_password);
        txt_signup = findViewById(R.id.sign_up);
        btn_login = findViewById(R.id.btn_login);

        firebaseAuth = FirebaseAuth.getInstance();

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login2Activity.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(Login2Activity.this);
                progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.please_wait));
                progressDialog.show();

                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(Login2Activity.this, getApplicationContext().getResources().getString(R.string.all_fields_are_required), Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(str_email, str_password)
                            .addOnCompleteListener(Login2Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login2Activity.this, getApplicationContext().getResources().getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
