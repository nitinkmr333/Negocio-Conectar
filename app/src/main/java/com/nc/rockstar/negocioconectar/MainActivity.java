package com.nc.rockstar.negocioconectar;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getFragmentManager();

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView userName;
    private TextView userEmail;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, new homeActivity()).commit();
                    return true;
                case R.id.navigation_ads:
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, new adsActivity()).commit();
                    return true;
                case R.id.navigation_community:
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, new communityActivity()).commit();
                    return true;
                case R.id.navigation_chatbot:
                    //startActivity(new Intent(MainActivity.this, chatbotActivity.class));
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, new chatbotActivity()).commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();


        if (id == R.id.navigation_profile) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, new profileActivity()).commit();

        } else if (id == R.id.navigation_info) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, new infoActivity()).commit();

        } else if (id == R.id.navigation_news) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, new newsActivity()).commit();

        } else if (id == R.id.navigation_logout) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, loginActivity.class));
        }

        user = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, new homeActivity()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigation = (BottomNavigationView) findViewById(R.id.navigationBottom);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //userEmail = findViewById(R.id.toolbar_user_email);
        //userName = findViewById(R.id.toolbar_user_name);
        //updateUserInfo();

    }

    public void updateUserInfo(){
        TextView userView = (TextView) findViewById (R.id.toolbar_user_name);
        TextView emailView = (TextView) findViewById (R.id.toolbar_user_email);
        userView.setText(getUserName());
        emailView.setText(getUserEmail());
    }


    public String getUserName(){
        return user.getDisplayName().toString().trim();
    }

    public String getUserEmail(){
        return user.getEmail().toString().trim();
    }


}
