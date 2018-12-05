package com.apocalyvec.sleepandsound.behavior;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apocalyvec.sleepandsound.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddHardwareActivity extends AppCompatActivity {

    // UI elements
    private RecyclerView hw_list;

    //database elements
    private DatabaseReference hw_ref;

    private String KID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hardware);

        KID = getIntent().getExtras().get("KID").toString();

        //connect UI elements
        hw_list = findViewById(R.id.hw_list);

        //connect to firebase
        hw_ref = FirebaseDatabase.getInstance().getReference().child("hardwares");

        //Dummy for now
        HashMap<String, String> kidMap = new HashMap<> ();
        kidMap.put("associatedKid", KID);

        hw_ref.child("b7930720-b095-46aa-ba48-5f095d2856f8").child("associatedKid").setValue(KID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Hardware> options =
                new FirebaseRecyclerOptions.Builder<Hardware>().setQuery(hw_ref, Hardware.class).build();

        FirebaseRecyclerAdapter<Hardware, AddHardwareViewHolder> adapter = new FirebaseRecyclerAdapter<Hardware, AddHardwareViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AddHardwareViewHolder holder, int position, @NonNull Hardware model) {
                holder.tv_hwTimeStamp.setText("Registered on" + model.getTimestamp());
            }

            @NonNull
            @Override
            public AddHardwareViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hardware_row, viewGroup, false);
                AddHardwareViewHolder addHardwareViewHolder = new AddHardwareViewHolder(view);

                return addHardwareViewHolder;
            }
        };

        hw_list.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AddHardwareViewHolder extends RecyclerView.ViewHolder {
        TextView tv_hwTimeStamp;
        View mView;

        public AddHardwareViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tv_hwTimeStamp = mView.findViewById(R.id.tv_register_time);
        }
    }
}
