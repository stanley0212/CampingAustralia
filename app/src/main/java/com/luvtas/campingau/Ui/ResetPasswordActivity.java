package com.luvtas.campingau.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.luvtas.campingau.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText useremail;
    private Button sendEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        useremail = findViewById(R.id.user_email);
        sendEmail = findViewById(R.id.btn_sendEmail);

        auth = FirebaseAuth.getInstance();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = useremail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ResetPasswordActivity.this,getApplicationContext().getResources().getString(R.string.please_enter_your_email_address), Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this,getApplicationContext().getResources().getString(R.string.please_visit_your_email_to_reset_your_password), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this, Login2Activity.class));
                                finish();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this,getApplicationContext().getResources().getString(R.string.reset_password_error) +message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
