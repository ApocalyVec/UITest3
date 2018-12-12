package com.apocalyvec.sleepandsound;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddHardwareActivity extends AppCompatActivity {

    private String KID;
    private String UID;
//    private String currentPID = null;

    // UI elements
    private RecyclerView hw_list;

    //database elements
    private DatabaseReference hw_ref;
    private DatabaseReference kid_ref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hardware);

        KID = getIntent().getExtras().get("KID").toString();
//        if(getIntent().hasExtra("currentPID")) {
//            currentPID = getIntent().getExtras().get("currentPID").toString();
//        }

        //connect UI elements
        hw_list = findViewById(R.id.hw_list);

        //connect to firebase
        hw_ref = FirebaseDatabase.getInstance().getReference().child("hardwares");
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        kid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("kids").child(KID);


        //Dummy for now
//        HashMap<String, String> kidMap = new HashMap<> ();
//        kidMap.put("associatedKid", KID);
//        hw_ref.child("6c5687be-728d-461e-ac30-30c4196d0a0c").child("associatedKid").setValue(KID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Hardware> options =
                new FirebaseRecyclerOptions.Builder<Hardware>().setQuery(hw_ref, Hardware.class).build();

        FirebaseRecyclerAdapter<Hardware, AddHardwareViewHolder> adapter = new FirebaseRecyclerAdapter<Hardware, AddHardwareViewHolder>(options) {
            //TODO only show UNASSOCIATED hardwares
            @Override
            protected void onBindViewHolder(@NonNull AddHardwareViewHolder holder, final int position, @NonNull final Hardware model) {
                holder.tv_hwTimeStamp.setText("Registered on " + model.getTimestamp());

                if(model.getStatus().equals("UNASSOCIATED")) {
                    holder.tv_hwStatus.setText("Product Unassociated");
                }
                else {
                    holder.tv_hwStatus.setText("Associated with " + model.getStatus());
                }
                holder.btnAssocaited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String PID = model.getPid();

                        kid_ref.child("associatedPID").setValue(PID);
//                        hw_ref.child(PID).child("status").setValue(kid_ref.child("kidName"));
                        //deassociated previous product
//                        if(currentPID != null) {
//                            hw_ref.child(currentPID).child("status").setValue("UNASSOCIATED");
//                        }

                        Intent childViewIntent = new Intent(AddHardwareActivity.this, ChildViewActivity.class);
                        childViewIntent.putExtra("KID", KID);
                        childViewIntent.putExtra("PID", PID);

                        startActivity(childViewIntent);
                    }
                });
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
        hw_list.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent childViewIntent = new Intent(AddHardwareActivity.this, ChildViewActivity.class);
        childViewIntent.putExtra("KID", KID);
        startActivity(childViewIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent childViewIntent = new Intent(AddHardwareActivity.this, ChildViewActivity.class);
        childViewIntent.putExtra("KID", KID);
        startActivity(childViewIntent);
        return super.onOptionsItemSelected(item);
    }

    public static class AddHardwareViewHolder extends RecyclerView.ViewHolder {
        TextView tv_hwTimeStamp;
        TextView tv_hwStatus;
        View mView;
        Button btnAssocaited;

        public AddHardwareViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tv_hwTimeStamp = mView.findViewById(R.id.tv_register_time);
            btnAssocaited = mView.findViewById(R.id.btn_associate_hardware);
            tv_hwStatus = mView.findViewById(R.id.tv_status);
        }
    }
}
