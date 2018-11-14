package com.apocalyvec.sleepandsound;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private DatabaseReference mDatabaseUserChildren;
    private LinearLayout childrenList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // connect to Firebase
        mDatabaseUserChildren = FirebaseDatabase.getInstance().getReference().child("Users").child("User_1").child("Children").child("Child_1").child("Age");


        return inflater.inflate(com.apocalyvec.sleepandsound.R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // connect to GUI
        childrenList = getActivity().findViewById(R.id.childrenList);

        //put in the card view for children
        mDatabaseUserChildren.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String FirstName = dataSnapshot.getValue().toString();
                TextView name1 = getActivity().findViewById(R.id.childName);
                name1.setText("Name: " + FirstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
