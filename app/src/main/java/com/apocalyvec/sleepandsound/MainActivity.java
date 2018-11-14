package com.apocalyvec.sleepandsound;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private CoordinatorLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.apocalyvec.sleepandsound.R.layout.activity_main);

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

    public void goToChild(View view) {
        Intent intent = new Intent(this, ChildViewActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}
