package com.apocalyvec.sleepandsound;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    private DatabaseReference hardwareRef;
    private String currentUserID;
    private FirebaseAuth mAuth;

    private String receiverKid;

    //ui fields
    private TextView childName;
    private TextView tv_ls;
    private TextView tv_mcp;
    private Button associateButton;
    private CircleImageView childImage;

    //chart related fields
    private ArrayList<String> timeLabels;

    private ArrayList<BarEntry> presEntries;
    private ArrayList<BarEntry> tempEntries;
    private BarChart presChart;
    private BarChart tempChart;

    private Calendar rightNow;

    private boolean databaseErrorFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view);

        receiverKid = getIntent().getExtras().get("visit_kid").toString();

//        mFireBaseBtn = (Button) findViewById(R.id.mFireBaseBtn);

        //connect to the database
        rootRef = FirebaseDatabase.getInstance().getReference();
        hardwareRef = rootRef.child("hardwares").child("6c5687be-728d-461e-ac30-30c4196d0a0c"); // this needs to be changed, it should look for associated hardware automatically
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        tv_ls = findViewById(R.id.tv_ls);
        tv_mcp = findViewById(R.id.tv_mcp);

        //connect to UI element
        childName = findViewById(R.id.cv_child_name);
        associateButton = findViewById(R.id.btn_cv_associate);
        childImage = findViewById(R.id.cv_child_image);
        presChart = findViewById(R.id.pressure_graph);
        tempChart = findViewById(R.id.temperature_graph);

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




        rootRef.child("Users").child(currentUserID).child("kids").child(receiverKid).addValueEventListener(new ValueEventListener() {
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
                Intent childViewIntent = new Intent(ChildViewActivity.this, AddHardwareActivity.class);
                childViewIntent.putExtra("KID", receiverKid);
                startActivity(childViewIntent);
            }
        });

        //value change listen from the database
        hardwareRef.child("LS_data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                try{
//                    tempEntries.set(i, new BarEntry(Float.parseFloat(dataSnapshot.child("hour"+Integer.toString(i)).child("temp").getValue().toString()), i));
//                }catch (NumberFormatException e) {
//                    e.printStackTrace();
//                    Log.e("ChildViewActivity", "Temperature Sensor Fault" +e.toString());
//                    Toast.makeText( ChildViewActivity.this, "Temperature Sensor Fault: " +e.toString(), Toast.LENGTH_SHORT).show();
//                }

                tv_ls.setText("Lighting in the Room: " + dataSnapshot.getValue().toString());
                try{
                    Float.parseFloat(dataSnapshot.getValue().toString());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("ChildViewActivity", "Light Sensor Fault" +e.toString());
                    tv_ls.setText("Light Sensor Fault: disconnected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        hardwareRef.child("MCP_data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String a = dataSnapshot.getValue().toString();
                Log.e("Noise value", a);

                tv_mcp.setText("Noise in the Room: " + dataSnapshot.getValue().toString());
                try{
                    Float.parseFloat(dataSnapshot.getValue().toString());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("ChildViewActivity", "Microphone Fault" +e.toString());
                    tv_mcp.setText("Microphone Fault: disconnected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        hardwareRef.child("ptData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i = 0; i <= 9; i++) {
                    try{
                        presEntries.set(i, new BarEntry(Float.parseFloat(dataSnapshot.child("hour"+Integer.toString(i)).child("pres").getValue().toString()), i));
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.e("ChildViewActivity", "Pressure Sensor Fault" +e.toString());
                        Toast.makeText(ChildViewActivity.this, "Pressure Sensor Fault: " +e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    try{
                        tempEntries.set(i, new BarEntry(Float.parseFloat(dataSnapshot.child("hour"+Integer.toString(i)).child("temp").getValue().toString()), i));
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.e("ChildViewActivity", "Temperature Sensor Fault" +e.toString());
                        Toast.makeText( ChildViewActivity.this, "Temperature Sensor Fault: " +e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                BarDataSet presbardateset = new BarDataSet(presEntries, "Hours");
                BarDataSet tempbardateset = new BarDataSet(tempEntries, "Hours");
                presbardateset.setColors(ColorTemplate.COLORFUL_COLORS);
                tempbardateset.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData presdata = new BarData(timeLabels, presbardateset);
                BarData tempdata = new BarData(timeLabels, tempbardateset);

                presChart.setData(presdata);
                presChart.setDescription("Pressure Data");
                presChart.setDescriptionTextSize(16f);
                presChart.invalidate();
                presChart.animateY(1500);

                tempChart.setData(tempdata);
                tempChart.setDescription("Temperature Data");
                tempChart.setDescriptionTextSize(16f);
                tempChart.invalidate();

                //tempChart.animateY(1500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void initializeGraphEntry() {
        tempEntries = new ArrayList<>();
        for(int i = 0; i <= 9; i++) {
            tempEntries.add(new BarEntry(0, i));
        }

        presEntries = new ArrayList<>();
        for(int i = 0; i <= 9; i++) {
            presEntries.add(new BarEntry(0, i));
        }
    }
}
