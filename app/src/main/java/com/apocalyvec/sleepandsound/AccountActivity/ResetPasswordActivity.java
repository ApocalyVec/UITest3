package com.apocalyvec.sleepandsound.AccountActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apocalyvec.sleepandsound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passwordEmail = findViewById(R.id.etPasswordEmail);
        resetPassword = findViewById(R.id.btnPasswordReset);

        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = passwordEmail.getText().toString().trim();
                if(useremail.equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your registered email ID", Toast.LENGTH_LONG).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Password Reset Link Sent to Your Email", Toast.LENGTH_LONG).show();
                                finish();
                                Intent newIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(newIntent);
                            }
                            else {
                                Toast.makeText(ResetPasswordActivity.this, "The Email Is Not Registered", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
