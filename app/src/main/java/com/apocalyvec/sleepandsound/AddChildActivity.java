package com.apocalyvec.sleepandsound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddChildActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUserChildren;
    private StorageReference mStorage;
    private EditText childFirstName;
    private EditText childLastName;
    private EditText childAge;
    private ImageView mSelectImage;

    private static final int GALLERY_REQUEST = 1;

    private Uri imageUri = null;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mDatabaseUserChildren = FirebaseDatabase.getInstance().getReference().child("Users").child("User_1").child("Children");
        mStorage = FirebaseStorage.getInstance().getReference();

        childFirstName = findViewById(R.id.ChildFirstName);
        childLastName = findViewById(R.id.ChildLastName);
        childAge = findViewById(R.id.ChildAge);
        mSelectImage = findViewById(R.id.imageSelect);

        mProgress = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mSelectImage.setImageURI(imageUri);
        }
    }

    public void onAddClicked(View view) {
        if(validate()) {
            // show progress dialog
            mProgress.setMessage("Adding child ...");
            mProgress.show();

            String firstName = childFirstName.getText().toString().trim();
            String lastName = childLastName.getText().toString().trim();
            String age = childAge.getText().toString().trim();
            final StorageReference filepath = mStorage.child("Blog_Images").child(imageUri.getLastPathSegment());

            UploadTask uploadTask = filepath.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        System.out.println("Upload " + downloadUri);
                        Toast.makeText(AddChildActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {

                            String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            System.out.println("Upload " + photoStringLink);

                        }

                    } else {
                        // Handle failures
                        Toast.makeText(AddChildActivity.this, "Something went wrong, check your internet.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if(task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//
//                    }
//                }
//            });

//            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {  // use on complete listerer
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // download the target URL
////                    Uri downloadUri = task.getRe();
//                }
//            });


            //using real-time database
//            HashMap<String, String> dataMap = new HashMap<>();
//            dataMap.put("FirstName", firstName);
//            dataMap.put("LastName", lastName);
//            dataMap.put("Age", age);
//
//            mDatabaseUserChildren.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()) {
//                        Toast.makeText(AddChildActivity.this, "Child Added", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(AddChildActivity.this, "Something went wrong, check your internet", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
            mProgress.dismiss();
        }
        // if the information are not added in full
        else return;

        //Log.d("ChildViewActivity", "Database Clicked");
    }

    private boolean validate() {
        boolean rtn = false;
        String firstName = childFirstName.getText().toString();
        String lastName = childLastName.getText().toString();
        String age = childAge.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty() || age.isEmpty() || (imageUri == null)){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else {
            rtn = true;
        }
        return rtn;
    }
}
