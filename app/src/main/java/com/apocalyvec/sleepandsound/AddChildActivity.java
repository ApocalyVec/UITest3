package com.apocalyvec.sleepandsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddChildActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText childFirstName;
    private EditText childLastName;
    private EditText childAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        childFirstName = findViewById(R.id.ChildFirstName);
        childLastName = findViewById(R.id.ChildLastName);
        childAge = findViewById(R.id.ChildAge);
    }

    public void onAddClicked(View view) {
        mDatabase.child(childFirstName.getText().toString().trim()).setValue("Ziheng Li");
        //Log.d("ChildViewActivity", "Database Clicked");
    }
}
