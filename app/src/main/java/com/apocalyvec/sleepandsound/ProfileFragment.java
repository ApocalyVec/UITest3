package com.apocalyvec.sleepandsound;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    //UI fields
    private Button updateAccountSettings;
    private EditText userName;
    private CircleImageView userProfileImage;
    private View profileView;

    //database fields
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();
        profileView = inflater.inflate(com.apocalyvec.sleepandsound.R.layout.fragment_profile, container, false);
        return profileView;
    }

    private void RetrieveUserInfo() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeFileds();
        rootRef.child("Users").child(currentUserID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitializeFileds() {
        updateAccountSettings = profileView.findViewById(R.id.update_settings_button);
        userName = profileView.findViewById(R.id.set_user_name);
        userProfileImage = profileView.findViewById(R.id.set_profile_image);

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });
    }

    private void UpdateSettings() {
        String setUserName = userName.getText().toString().trim();

        if(TextUtils.isEmpty(setUserName)) {
            Toast.makeText(getActivity(), "Please put your user name.", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, String> profileMap = new HashMap<> ();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setUserName);
            rootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(), "Error: "+ message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
