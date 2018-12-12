package com.apocalyvec.sleepandsound;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildViewActivity extends AppCompatActivity {

    private DatabaseReference rootRef;
    private DatabaseReference kidRef;
    private DatabaseReference allHwRef;
    private String currentUserID;
    private FirebaseAuth mAuth;

    private String KID;
    private String PID = null;

    //ui fields
    private TextView childName;
//    private TextView tv_ls;
//    private TextView tv_mcp;
    private TextView associateButton;
    private CircleImageView childImage;
    private TextView tvSelectedDate;
    private ImageView btnSelectDate;

    //chart related fields
    private ArrayList<String> timeLabels;

    private LineDataSet lightDataSet;
    private LineDataSet bedDataSet;

    private ArrayList<Entry> lightDataEntry;
    private ArrayList<Entry> bedDataEntry;
    private ArrayList<Entry> noiseDataEntry;

    private LineChart lightChart;
    private LineChart bedChart;
    private LineChart noiseChart;

    private Calendar rightNow;

    private boolean databaseErrorFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view);

        //connect to the database
        rootRef = FirebaseDatabase.getInstance().getReference();
        allHwRef = rootRef.child("hardwares");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        if (getIntent().hasExtra("KID")) {
            KID = getIntent().getExtras().get("KID").toString();
            kidRef = rootRef.child("Users").child(currentUserID).child("kids").child(KID);
        }

//        mFireBaseBtn = (Button) findViewById(R.id.mFireBaseBtn);


//        allHwRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshotIterator : dataSnapshot.getChildren()) {
//
//                    User user = snapshot.getValue(User.class);
//                    System.out.println(user.email);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ChildViewActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
//            }
//        });


        kidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("associatedPID")) {
                    //reset previous status
                    if(PID != null) {
                        rootRef.child("hardwares").child(PID).child("status").setValue("UNASSOCIATED");
                    }

                    PID = dataSnapshot.child("associatedPID").getValue().toString();
                    Toast.makeText(ChildViewActivity.this, "Child Associated with Product" + PID, Toast.LENGTH_SHORT).show();

                    PID = dataSnapshot.child("associatedPID").getValue().toString();
                    rootRef.child("hardwares").child(PID).child("status").setValue(dataSnapshot.child("kidName").getValue());
                }
                else {
                    Toast.makeText(ChildViewActivity.this, "No Product Associated with this Child", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChildViewActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
//        tv_ls = findViewById(R.id.tv_ls);
//        tv_mcp = findViewById(R.id.tv_mcp);

        //connect to UI element
        childName = findViewById(R.id.cv_child_name);
        associateButton = findViewById(R.id.btn_cv_associate);
        childImage = findViewById(R.id.cv_child_image);

        lightChart = findViewById(R.id.light_graph);
        bedChart = findViewById(R.id.bed_graph);
        noiseChart = findViewById(R.id.noise_graph);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);



        //chart related initilizations

//        rightNow = Calendar.getInstance();
//        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
//
//        timeLabels = new ArrayList<>();
//        for(int i = 0; i <= 9; i++) {
//            if(currentHourIn24Format-10)
//        }

        timeLabels = new ArrayList<>();
        timeLabels.add("9 hr ago");
        timeLabels.add("8 hr ago");
        timeLabels.add("7 hr ago");
        timeLabels.add("6 hr ago");
        timeLabels.add("5 hr ago");
        timeLabels.add("4 hr ago");
        timeLabels.add("3 hr ago");
        timeLabels.add("2 hr ago");
        timeLabels.add("1 hr ago");
        timeLabels.add("now");
        initializeGraphEntry();

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarIntent = new Intent(ChildViewActivity.this, CalendarActivity.class);
                calendarIntent.putExtra("KID", KID);
                startActivity(calendarIntent);
            }
        });

        //this is problematic if coming from a view other than home fragment
        rootRef.child("Users").child(currentUserID).child("kids").child(KID).addValueEventListener(new ValueEventListener() {
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
                Intent AddHardwareIntent = new Intent(ChildViewActivity.this, AddHardwareActivity.class);

//                if(PID != null) {
//                    AddHardwareIntent.putExtra("currentPID", PID);
//                }
                AddHardwareIntent.putExtra("KID", KID);
                startActivity(AddHardwareIntent);
            }
        });

        //value change listen from the database
//        hardwareRef.child("LS_data").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                // no hardware is associated
//                if (dataSnapshot.getValue() == null) {
//                    Toast.makeText(ChildViewActivity.this, "No Product Associated with this Child", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //TODO set realtime data
////                    tv_ls.setText("Lighting in the Room: " + dataSnapshot.getValue().toString());
//                    try{
//                        Float.parseFloat(dataSnapshot.getValue().toString());
//                    }catch (NumberFormatException e) {
//                        e.printStackTrace();
//                        Log.e("ChildViewActivity", "Light Sensor Fault" +e.toString());
//                        //TODO set realtime data
////                        tv_ls.setText("Light Sensor Fault: disconnected");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });



//        hardwareRef.child("MCP_data").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                if (dataSnapshot.getValue() == null) {
////                    Toast.makeText(ChildViewActivity.this, "No Product Associated with this Child", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //TODO set realtime data
////                    tv_mcp.setText("Noise in the Room: " + dataSnapshot.getValue().toString());
//                    try{
//                        Float.parseFloat(dataSnapshot.getValue().toString());
//                    }catch (NumberFormatException e) {
//                        e.printStackTrace();
//                        Log.e("ChildViewActivity", "Microphone Fault" +e.toString());
//                        //TODO set realtime data
////                        tv_mcp.setText("Microphone Fault: disconnected");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        hardwareRef.child("ptData").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue() == null) {
////                    Toast.makeText(ChildViewActivity.this, "No Product Associated with this Child", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    for(int i = 0; i <= 9; i++) {
//                        try{
//                            presEntries.set(i, new Entry(Float.parseFloat(dataSnapshot.child("hour"+Integer.toString(i)).child("pres").getValue().toString()), i));
//                        }catch (NumberFormatException e) {
//                            e.printStackTrace();
//                            Log.e("ChildViewActivity", "Pressure Sensor Fault" +e.toString());
//                            Toast.makeText(ChildViewActivity.this, "Pressure Sensor Fault: " +e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                        try{
//                            tempEntries.set(i, new BarEntry(Float.parseFloat(dataSnapshot.child("hour"+Integer.toString(i)).child("temp").getValue().toString()), i));
//                        }catch (NumberFormatException e) {
//                            e.printStackTrace();
//                            Log.e("ChildViewActivity", "Temperature Sensor Fault" +e.toString());
//                            Toast.makeText( ChildViewActivity.this, "Temperature Sensor Fault: " +e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    LineDataSet presLinedateset = new LineDataSet(presEntries, "Hours");
//                    LineDataSet tempLinedateset = new LineDataSet(tempEntries, "Hours");
//                    presLinedateset.setColors(ColorTemplate.COLORFUL_COLORS);
//                    tempLinedateset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//                    BarData presdata = new BarData(timeLabels, presLinedateset);
//                    BarData tempdata = new BarData(timeLabels, tempLinedateset);
//
//                    presChart.setData(presdata);
//                    presChart.setDescription("Pressure Data");
//                    presChart.setDescriptionTextSize(16f);
//                    presChart.invalidate();
//                    presChart.animateY(1500);
//
//                    tempChart.setData(tempdata);
//                    tempChart.setDescription("Temperature Data");
//                    tempChart.setDescriptionTextSize(16f);
//                    tempChart.invalidate();
//                    tempChart.animateY(1500);
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    void initializeGraphEntry() {

        lightDataEntry = new ArrayList<>();
        bedDataEntry = new ArrayList<>();
        noiseDataEntry = new ArrayList<>();

        for(int i = 0; i <= 23; i++) {
            lightDataEntry.add(new Entry(0, i));
        }

        bedDataEntry = new ArrayList<>();
        for(int i = 0; i <= 23; i++) {
            bedDataEntry.add(new Entry(0, i));
        }

        noiseDataEntry = new ArrayList<>();
        for(int i = 0; i <= 23; i++) {
            noiseDataEntry.add(new Entry(0, i));
        }
    }
}
