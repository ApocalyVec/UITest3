package com.apocalyvec.sleepandsound.AccountActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apocalyvec.sleepandsound.MainActivity;
import com.apocalyvec.sleepandsound.R;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    ProgressBar loginProgress;
    TextView tvRegister;

    TextView loginMail;
    TextView loginPassword;

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


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    loginProgress.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent newIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(newIntent);
            }
        });
    }

    private Boolean validate() {
        Boolean rtn = false;

        String mail = loginMail.getText().toString();
        String password = loginPassword.getText().toString();

        if(mail.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT);
        }else {
            rtn = true;
        }

        return rtn;
    }
}
