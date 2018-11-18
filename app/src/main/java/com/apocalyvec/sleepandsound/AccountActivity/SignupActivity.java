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

public class SignupActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    ProgressBar signUpProgress;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

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
                    String password = userPassword.getText().toString().trim();
                    String email = userEmail.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registered, You Are Good To Go!", Toast.LENGTH_SHORT).show();;
                                Intent newIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(newIntent);
                            }
                            else {
                                Toast.makeText(SignupActivity.this, "Something Weng Wrong, Check Your Internet", Toast.LENGTH_SHORT).show();;
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
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT);
        }else {
            rtn = true;
        }

        return rtn;
    }
}
