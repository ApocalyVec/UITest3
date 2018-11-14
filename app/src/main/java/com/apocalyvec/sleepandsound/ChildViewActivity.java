package com.apocalyvec.sleepandsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChildViewActivity extends AppCompatActivity {

    private Button mFireBaseBtn;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view);

        mFireBaseBtn = (Button) findViewById(R.id.mFireBaseBtn);

        //connect to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFireBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 - create CHILD in root object
                // 2 - assign some value to the CHILD object
                mDatabase.child("Name").setValue("Ziheng Li");
                Log.d("ChildViewActivity", "Database Clicked");
            }
        });
    }
}
