package com.apocalyvec.sleepandsound;

import android.content.Intent;
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

import java.util.Currency;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private DatabaseReference userKidsRef;

    private RecyclerView mKidsList;
    private View homeView;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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


        // Connect to database
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userKidsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("kids");
    }

    @Override
    public void onStart() {
        super.onStart();

        //put in the card view for children

        FirebaseRecyclerOptions<Kids> options =
                new FirebaseRecyclerOptions.Builder<Kids>().setQuery(userKidsRef, Kids.class).build();

        FirebaseRecyclerAdapter<Kids, kidsViewHolder> adapter = new FirebaseRecyclerAdapter<Kids, kidsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull kidsViewHolder holder, final int position, @NonNull Kids model) {
                holder.childName.setText(model.getkidName());
                holder.childage.setText(model.getAge());
                Picasso.get().load(model.getImage()).into(holder.childImage);

                // make the item in the view clickable
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_kid = getRef(position).getKey();
                        Intent childViewIntent = new Intent(getActivity(), ChildViewActivity.class);
                        childViewIntent.putExtra("visit_kid", visit_kid);
                        startActivity(childViewIntent);
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

        mKidsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class kidsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView childImage;
        TextView childName;
        TextView childage;

        public kidsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            childName = mView.findViewById(R.id.child_name);
            childage = mView.findViewById(R.id.child_age);
            childImage = mView.findViewById(R.id.child_photo);
        }

//        public void setName(String name) {
//            childName.setText(name);
//        }
//
//        public void setAge(String age) {
//            childage.setText(age);
//        }
//
//        public void setKidImage(Uri imageUri) {
//            childImage.setImageURI(imageUri);
//        }
    }
}
