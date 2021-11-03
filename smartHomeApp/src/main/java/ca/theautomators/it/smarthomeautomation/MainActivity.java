/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ca.theautomators.it.smarthomeautomation.ui.bedroom.BedroomFragment;
import ca.theautomators.it.smarthomeautomation.ui.kitchen.KitchenFragment;
import ca.theautomators.it.smarthomeautomation.ui.landing.LandingFragment;
import ca.theautomators.it.smarthomeautomation.ui.livingroom.LivingRoomFragment;
import ca.theautomators.it.smarthomeautomation.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_bedroom, R.id.nav_home, R.id.nav_livingroom, R.id.nav_kitchen, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle(R.string.alert_title);
        alert.setMessage(R.string.alert_message);
        alert.setPositiveButton(R.string.alert_positive_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setNegativeButton(R.string.alert_negative_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();


    }

    private void loadFragment(int id, String title, Fragment fragment){

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        Toolbar toolbarText = findViewById(R.id.toolbar);
        MenuItem navLoad = menu.findItem(id);

        toolbarText.setTitle(title);
        navLoad.setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.landing_layout, fragment,
                null).setReorderingAllowed(true).commit();

    }

    public void fragmentSwitch(int id){

        switch(id){

            case R.id.nav_kitchen:
                loadFragment(id, getString(R.string.kitchen), new KitchenFragment());
                break;
            case R.id.nav_bedroom:
                loadFragment(id, getString(R.string.bedroom), new BedroomFragment());
                break;
            case R.id.nav_livingroom:
                loadFragment(id, getString(R.string.living_room), new LivingRoomFragment());
                break;
            case R.id.nav_settings:
                loadFragment(id, getString(R.string.settings), new SettingsFragment());
                break;
            case R.id.nav_home:
                loadFragment(id, getString(R.string.home), new LandingFragment());
        }
    }

    public void renameRoom(int id, String title){

        NavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.findItem(id);
        menuItem.setTitle(title);
    }

    public void loadRoomInfo(int id){

        NavigationView navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Menu menu = navView.getMenu();
        MenuItem item = menu.findItem(id);
        toolbar.setTitle(item.getTitle());
    }
}