package com.apocalyvec.sleepandsound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddChildActivity extends AppCompatActivity {

    private DatabaseReference rootRef;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private String currentUserID;

    private EditText childName;
    private EditText childAge;
    private ImageView mSelectImage;

    private static final int GALLERY_REQUEST = 1;

    private Uri imageUri = null;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        //connect to the database
        rootRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        childName = findViewById(R.id.etChildName);
        childAge = findViewById(R.id.etChildAge);
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

            final String name = childName.getText().toString().trim();
            final String age = childAge.getText().toString().trim();
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
                        Toast.makeText(AddChildActivity.this, "Successfully added to storage", Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {

                            String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            System.out.println("Upload " + photoStringLink);

//                            DatabaseReference newChild = mDatabase.push();
//                            newChild.child("name").setValue(name);
//                            newChild.child("age").setValue(age);
//                            newChild.child("image").setValue(photoStringLink);

                            HashMap<String, String> kidMap = new HashMap<>();
                            kidMap.put("kidName", name);
                            kidMap.put("age", age);
                            kidMap.put("image", photoStringLink);
                            rootRef.child("Users").child(currentUserID).child("kids").push().setValue(kidMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(AddChildActivity.this, "Successfully added kid", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String message = task.getException().toString();
                                        Toast.makeText(AddChildActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } else {
                        // Handle failures
                        Toast.makeText(AddChildActivity.this, "Something went wrong, check your internet.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            finish();
            Intent newIntent = new Intent(AddChildActivity.this, MainActivity.class);
            startActivity(newIntent);
        }
        mProgress.dismiss();
    }

    private boolean validate() {
        boolean rtn = false;
        String name = childName.getText().toString();
        String age = childAge.getText().toString();

        if(name.isEmpty() || age.isEmpty() || (imageUri == null)){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else {
            rtn = true;
        }
        return rtn;
    }
}
