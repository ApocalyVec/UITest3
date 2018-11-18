package com.apocalyvec.sleepandsound.AccountActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apocalyvec.sleepandsound.R;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    ProgressBar loginProgress;
    TextView TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        loginProgress = findViewById(R.id.login_progress);
        TextView = findViewById(R.id.tvRegister);
        loginProgress.setVisibility(View.INVISIBLE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }
        });
    }
}
