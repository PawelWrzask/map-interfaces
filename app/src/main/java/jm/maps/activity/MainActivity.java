package jm.maps.activity;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import jm.maps.R;
import jm.maps.view.FragmentDiscover;
import jm.maps.view.FragmentHistory;
import jm.maps.view.FragmentMapsActivity;
import jm.maps.view.FragmentNear;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentMapsActivity()).commit();
            navigationView.setCheckedItem(R.id.nav_my_location);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentMapsActivity()).commit();
                break;
            case R.id.nav_near_me:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentNear()).commit();
                break;
            case R.id.discover:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentDiscover()).commit();
                break;
            case R.id.beenhere:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHistory()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}