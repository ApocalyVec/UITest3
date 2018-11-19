package com.apocalyvec.sleepandsound;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddChildActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUserChildren;
    private EditText childFirstName;
    private EditText childLastName;
    private EditText childAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mDatabaseUserChildren = FirebaseDatabase.getInstance().getReference().child("Users").child("User_1").child("Children");

        childFirstName = findViewById(R.id.ChildFirstName);
        childLastName = findViewById(R.id.ChildLastName);
        childAge = findViewById(R.id.ChildAge);
    }

    public void onAddClicked(View view) {
        String firstName = childFirstName.getText().toString().trim();
        String lastName = childLastName.getText().toString().trim();
        String age = childAge.getText().toString().trim();

        if(validate()) {
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("FirstName", firstName);
            dataMap.put("LastName", lastName);
            dataMap.put("Age", age);

            mDatabaseUserChildren.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(AddChildActivity.this, "Child Added", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(AddChildActivity.this, "Something went wrong, check your internet", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else return;

        //Log.d("ChildViewActivity", "Database Clicked");
    }

    private boolean validate() {
        boolean rtn = false;
        String firstName = childFirstName.getText().toString();
        String lastName = childLastName.getText().toString();
        String age = childAge.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty() || age.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else {
            rtn = true;
        }
        return rtn;
    }
}
