package id.sch.smktelkom_mlg.nextbook;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.nextbook.Fragment.ClassFragment;
import id.sch.smktelkom_mlg.nextbook.Fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Activity MA;
    TextView tvUsernameDrawer, tvEmailDrawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MA = this;

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        changePage(R.id.nav_class);
        navigationView.setCheckedItem(R.id.nav_class);
        setDrawerInfo();
    }

    private void setDrawerInfo() {
        View hView = navigationView.getHeaderView(0); //Mengambil view dari drawer
        tvUsernameDrawer = hView.findViewById(R.id.textViewUsernameDrawer);
        tvUsernameDrawer.setText(Prefs.getString("fullname", null));
        tvEmailDrawer = hView.findViewById(R.id.textViewEmailDrawer);
        tvEmailDrawer.setText(Prefs.getString("email", null));
        CircleImageView ivPP = hView.findViewById(R.id.imageViewHehe);
        Glide.with(MainActivity.this).load(Prefs.getString("pict", null)).into(ivPP);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        changePage(id);
        return true;
    }

    private void changePage(int id) {
        Fragment fragment = null;
        if (id == R.id.nav_class) {
            fragment = new ClassFragment();
            if (Prefs.getString("classid", null) != null)
                setTitle("");
            else
                setTitle("Class");
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            setTitle("Profile");
        } else if (id == R.id.nav_logout) {
            fragment = new ClassFragment();
            navigationView.setCheckedItem(R.id.nav_class);
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure want to logout ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Prefs.clear();

                            Intent b = new Intent(MainActivity.this, SplashActivity.class);
                            startActivity(b);
                            finish();
                        }
                    }).setNegativeButton("Cancel", null).show();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitNow();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
