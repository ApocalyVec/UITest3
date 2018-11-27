package com.apocalyvec.sleepandsound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildViewActivity extends AppCompatActivity {

    private DatabaseReference rootRef;

    //ui fields
    private TextView childName;
    private Button updateButton;
    private CircleImageView childImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view);

//        mFireBaseBtn = (Button) findViewById(R.id.mFireBaseBtn);

        //connect to the database
        rootRef = FirebaseDatabase.getInstance().getReference();

        //connect to UI element
        childName = findViewById(R.id.cv_child_name);
        updateButton = findViewById(R.id.cv_child_update);
        childImage = findViewById(R.id.cv_child_image);

    }
}
