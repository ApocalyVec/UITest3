package com.apocalyvec.sleepandsound.AccountActivity;

import android.content.Intent;
import android.net.nsd.NsdManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apocalyvec.sleepandsound.MainActivity;
import com.apocalyvec.sleepandsound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    ProgressBar signUpProgress;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //connect to database
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        //define UI elements
        setupUIViews();
        signUpProgress.setVisibility(View.INVISIBLE);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    signUpProgress.setVisibility(View.VISIBLE);
                    regButton.setVisibility(View.INVISIBLE);

                    //Upload to the database
                    //String name = userName.getText().toString().trim();
                    final String name = userName.getText().toString().trim();
                    String password = userPassword.getText().toString().trim();
                    String email = userEmail.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                //put user information in the database
                                String currentUserID = firebaseAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserID).setValue(name);

                                HashMap<String, String> profileMap = new HashMap<> ();
                                profileMap.put("uid", currentUserID);
                                profileMap.put("name", name);
                                rootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
//                                            Toast.makeText(SignupActivity.this, "New user added", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String message = task.getException().toString();
                                            Toast.makeText(SignupActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                Toast.makeText(SignupActivity.this, "Registered, You Are Good To Go!", Toast.LENGTH_SHORT).show();;
                                Intent newIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(newIntent);
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(SignupActivity.this, "Something Weng Wrong, Error: "+message, Toast.LENGTH_SHORT).show();;
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent newIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(newIntent);
            }
        });
    }

    private void setupUIViews () {
        userName = findViewById(R.id.etUserName);
        userPassword = findViewById(R.id.etPassword);
        userEmail = findViewById(R.id.etUserEmail);
        regButton = findViewById(R.id.btnRegister);
        userLogin = findViewById(R.id.tvUserLogin);
        signUpProgress = findViewById(R.id.signup_progress);
    }

    private Boolean validate() {
        Boolean rtn = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else {
            rtn = true;
        }

        return rtn;
    }
}
