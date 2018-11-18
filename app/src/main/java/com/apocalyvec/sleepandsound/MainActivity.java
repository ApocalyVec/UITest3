package com.apocalyvec.sleepandsound;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apocalyvec.sleepandsound.AccountActivity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private CoordinatorLayout content;
    private ArrayList<String> childrenArray;
    private DatabaseReference mDatabaseUserChildren;
    public ArrayAdapter<String> arrayAdapter;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.apocalyvec.sleepandsound.R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(com.apocalyvec.sleepandsound.R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(com.apocalyvec.sleepandsound.R.id.drawer_layout);
        NavigationView navigationView = findViewById(com.apocalyvec.sleepandsound.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar, com.apocalyvec.sleepandsound.R.string.navigation_drawer_open, com.apocalyvec.sleepandsound.R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);

        toogle.syncState();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(com.apocalyvec.sleepandsound.R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(com.apocalyvec.sleepandsound.R.id.nav_home);

        }

        content = findViewById(com.apocalyvec.sleepandsound.R.id.content);

        // connect to Firebase
        mDatabaseUserChildren = FirebaseDatabase.getInstance().getReference().child("Users").child("User_1").child("Hardware");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, childrenArray);

        mDatabaseUserChildren.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case com.apocalyvec.sleepandsound.R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(com.apocalyvec.sleepandsound.R.id.fragment_container, new HomeFragment()).commit();
//                FloatingActionButton hfab = (FloatingActionButton) Inflater.inflate()
//                content.addView("layout");
                break;
            case com.apocalyvec.sleepandsound.R.id.nav_store:
                getSupportFragmentManager().beginTransaction().replace(com.apocalyvec.sleepandsound.R.id.fragment_container, new StoreFragment()).commit();
                break;
            case com.apocalyvec.sleepandsound.R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(com.apocalyvec.sleepandsound.R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case com.apocalyvec.sleepandsound.R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(com.apocalyvec.sleepandsound.R.id.fragment_container, new AboutFragment()).commit();
                break;
            case com.apocalyvec.sleepandsound.R.id.nav_help:
                Toast.makeText(this, "Help Yourself...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout:
                firebaseAuth.signOut();
                finish();
                Intent newIntent = new Intent(this, LoginActivity.class);
                startActivity(newIntent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    public void goToViewChild(View view) {
        Intent intent = new Intent(this, ChildViewActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToAddChild(View view) {
        Intent intent = new Intent(this, AddChildActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
