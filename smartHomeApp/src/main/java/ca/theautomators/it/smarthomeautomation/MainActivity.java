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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ca.theautomators.it.smarthomeautomation.ui.bedroom.BedroomFragment;
import ca.theautomators.it.smarthomeautomation.ui.kitchen.KitchenFragment;
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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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

    public void fragmentControl(int item){

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        Toolbar toolbarText = findViewById(R.id.toolbar);

        switch(item){

            case 1:
                MenuItem navKitchen = menu.findItem(R.id.nav_kitchen);
                toolbarText.setTitle(getString(R.string.kitchen));
                navKitchen.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.landing_layout, new KitchenFragment(),
                        null).setReorderingAllowed(true).commit();
                break;
            case 2:
                MenuItem navBedroom = menu.findItem(R.id.nav_bedroom);
                toolbarText.setTitle(getString(R.string.bedroom));
                navBedroom.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.landing_layout, new BedroomFragment(),
                        null).setReorderingAllowed(true).commit();
                break;
            case 3:
                MenuItem navLivingRoom = menu.findItem(R.id.nav_livingroom);
                toolbarText.setTitle(getString(R.string.living_room));
                navLivingRoom.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.landing_layout, new LivingRoomFragment(),
                        null).setReorderingAllowed(true).commit();
                break;
            case 4:
                MenuItem navSettings = menu.findItem(R.id.nav_settings);
                toolbarText.setTitle(getString(R.string.settings));
                navSettings.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.landing_layout, new SettingsFragment(),
                        null).setReorderingAllowed(true).commit();
                break;

        }
    }

}