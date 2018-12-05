package com.apocalyvec.sleepandsound;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apocalyvec.sleepandsound.behavior.AddHardwareActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildViewActivity extends AppCompatActivity {

    private DatabaseReference rootRef;
    private String currentUserID;
    private FirebaseAuth mAuth;

    private String receiverKid;

    //ui fields
    private TextView childName;
    private Button associateButton;
    private CircleImageView childImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view);

        receiverKid = getIntent().getExtras().get("visit_kid").toString();

//        mFireBaseBtn = (Button) findViewById(R.id.mFireBaseBtn);

        //connect to the database
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //connect to UI element
        childName = findViewById(R.id.cv_child_name);
        associateButton = findViewById(R.id.btn_cv_associate);
        childImage = findViewById(R.id.cv_child_image);

//

        rootRef.child("Users").child(currentUserID).child("kids").child(receiverKid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childName.setText(dataSnapshot.child("kidName").getValue().toString());
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(childImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(this, "Database error" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        associateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childViewIntent = new Intent(ChildViewActivity.this, AddHardwareActivity.class);
                childViewIntent.putExtra("KID", receiverKid);
                startActivity(childViewIntent);
            }
        });
    }
}
