package com.apocalyvec.sleepandsound;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private DatabaseReference mDatabaseKids;
    private RecyclerView mKidsList;
    private View homeView;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDatabaseKids = FirebaseDatabase.getInstance().getReference().child("kids");
        homeView = inflater.inflate(com.apocalyvec.sleepandsound.R.layout.fragment_home, container, false);
        return homeView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // connect to GUI
        mKidsList = homeView.findViewById(R.id.kids_list);
//        mKidsList.setHasFixedSize(true);
        mKidsList.setLayoutManager(new LinearLayoutManager(getContext()));


        // get current user
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void onStart() {
        super.onStart();

        //put in the card view for children

        FirebaseRecyclerOptions<Kids> options = new FirebaseRecyclerOptions.Builder<Kids>().setQuery(mDatabaseKids, Kids.class).build();

        FirebaseRecyclerAdapter<Kids, kidsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Kids, kidsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final kidsViewHolder holder, int position, @NonNull Kids model) {
                String userIDs = getRef(position).getKey();  //use of user ID need to be implemented
                mDatabaseKids.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //temporary
                        String kidImage = "https://firebasestorage.googleapis.com/v0/b/sleepandsound-d1073.appspot.com/o/Blog_Images%2Fimage%3A59?alt=media&token=d45a0704-2e2a-429b-888b-18e7537f5bc8";
                        String kidName = "Jack";
                        String kidAge = "12";

                        if(dataSnapshot.hasChild("image")) {
//                            String kidImage = dataSnapshot.child("image").getValue().toString();
//                            String kidName = dataSnapshot.child("name").getValue().toString();
//                            String kidAge = dataSnapshot.child("age").getValue().toString();

                            holder.setName(kidName);
                            holder.setAge(kidAge);
                            Picasso.get().load(kidImage).placeholder(R.drawable.ic_profile).into(holder.childImage);
                        }
                        else { //if the has not set the profile image
//                            String kidName = dataSnapshot.child("name").getValue().toString();
//                            String kidAge = dataSnapshot.child("age").getValue().toString();

                            holder.setName(kidName);
                            holder.setAge(kidAge);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public kidsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_row, viewGroup, false);
                kidsViewHolder viewHolder = new kidsViewHolder(view);
                return viewHolder;
            }
        };

        mKidsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class kidsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView childImage;
        TextView childName;
        TextView childage;

        public kidsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            childName = mView.findViewById(R.id.child_name);
            childage = mView.findViewById(R.id.child_age);
            childImage = mView.findViewById(R.id.child_photo);
        }

        public void setName(String name) {
            childName.setText(name);
        }

        public void setAge(String age) {
            childage.setText(age);
        }

        public void setKidImage(Uri imageUri) {
            childImage.setImageURI(imageUri);
        }
    }
}
