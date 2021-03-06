package com.apocalyvec.sleepandsound.AccountActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apocalyvec.sleepandsound.MainActivity;
import com.apocalyvec.sleepandsound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    ProgressBar loginProgress;
    TextView tvRegister;

    TextView loginMail;
    TextView loginPassword;
    TextView resetPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        loginProgress = findViewById(R.id.login_progress);
        tvRegister = findViewById(R.id.tvRegister);
        loginProgress.setVisibility(View.INVISIBLE);
        loginMail = findViewById(R.id.login_mail);
        loginPassword = findViewById(R.id.login_password);
        resetPassword = findViewById(R.id.tvForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        // automatically login if the user has already logged in
        if(user != null) {
            verifyUserExistence();
            finish();
            Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(newIntent);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent newIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(newIntent);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(newIntent);
            }
        });
    }

    private void verifyUserExistence() {
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        rootRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists()) {
                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
//                    Intent newIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
//                    startActivity(newIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void validate() {
        loginProgress.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        String mail = loginMail.getText().toString();
        String password = loginPassword.getText().toString();


        if(mail.isEmpty() || password.isEmpty()){
            progressDialog.dismiss();
            loginProgress.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else {
            firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "You Are in!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(newIntent);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Some Thing Went Wrong, Check Your Credentials", Toast.LENGTH_SHORT).show();
                        loginProgress.setVisibility(View.INVISIBLE);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
