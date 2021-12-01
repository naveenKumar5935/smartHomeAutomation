/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.ui.AboutAppActivity;
import ca.theautomators.it.smarthomeautomation.ui.landing.LandingFragment;
import ca.theautomators.it.smarthomeautomation.ui.settings.SettingsFragment;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private static ArrayList<Integer> menuIds;
    private static ArrayList<String> roomNames;
    private static ArrayList<Drawable> roomIcons;
    private static NavigationView navigationView;
    private static NotificationCompat.Builder builder;
    private static Context context;
    private Notifications tempNotifier;
    ArrayList<String> arrayList ;
    DatabaseReference firebaseDatabase;
    boolean rfidChange=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gettingData();

        firebaseDatabase =  FirebaseDatabase.getInstance().getReference();

        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
           setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        context = this;

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        FirebaseConnect fC = FirebaseConnect.getInstance();
        String[] identifiers = fC.getIdentifiers();
        String notificationIdentifier = "";
        DatabaseReference tempRef;

        label:
        for(int i = 0; i < identifiers.length; i++){
            String type = fC.getDeviceType(identifiers[i]);
            //Add more cases for other notifications
            switch (type) {
                case "TEMP":
                    notificationIdentifier = identifiers[i];
                    buildNotification("Temperature alarm", notificationIdentifier, pendingIntent, fC);
                    break label;
                case "SMOKE":
                    notificationIdentifier = identifiers[i];
                    buildNotification("Smoke alarm", notificationIdentifier, pendingIntent, fC);
                    break label;
                case "HUMID":
                    notificationIdentifier = identifiers[i];
                    buildNotification("Humidity alarm", notificationIdentifier, pendingIntent, fC);
                    break label;

            }
        }

//        if(!notificationIdentifier.isEmpty()){
//            tempRef = fC.getSensorDataRef(notificationIdentifier);
//
//            builder = new NotificationCompat.Builder(this, "NOTIFICATIONS")
//                    .setSmallIcon(R.drawable.thermostat)
//                    .setContentText("Temperature Alarm")
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//            tempRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                    String temperature = snapshot.getValue(String.class);
//                    if(temperature != null)
//                        new Notifications(temperature, "TEMP");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//
//        }

//        firebaseDatabase.child("Devices").child("103").child("DATA").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String smokeValue = snapshot.getValue().toString().split(":")[1];
//                new Notifications(smokeValue,"SMOKE");
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        firebaseDatabase.child("Devices").child("105").child("DATA").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String humidValue = snapshot.getValue().toString().split(":")[0];
//                new Notifications(humidValue,"HUMID");
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//        firebaseDatabase.child("Devices").child("100").child("DATA").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String value = snapshot.getValue().toString();
//                String rfidValue = snapshot.getValue().toString().split(":")[1];
//
//                if(rfidChange){
//                    if(arrayList.contains(rfidValue)){
//                        new Notifications("Rfid scanned","RFID");
//                    }else {
//                        new Notifications("Wrong Rfid scanned","RFID");
//                    }
//                }else {
//                    rfidChange=true;
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();



        RoomState roomState = RoomState.getInstance(this);

        menuIds = roomState.getMenuIds();
        roomNames = roomState.getRoomNames();
        roomIcons = roomState.getRoomIcons();

        if(roomNames.isEmpty() || roomNames == null){

            Intent roomManager = new Intent(MainActivity.this,RoomManagerActivity.class);
            startActivity(roomManager);
        }

        roomState.resetMenuItems();
        RoomBuilder rB = new RoomBuilder(navigationView, savedInstanceState, drawer, getSupportFragmentManager());

        for(int i = 0; i < roomNames.size(); i++){

            rB.buildRoom(roomNames.get(i), roomIcons.get(i), i);
            roomState.changeMenuIds(i, i);
        }

        roomState.save();


        if(roomState.getRoomStateChanged() || !roomState.getStateSaved()){

            for(int i = 0; i < roomState.getNumRooms(); i++){

                setRoomNameAndIcon(roomState.getMenuIds().get(i), roomState.getRoomNames().get(i), roomState.getRoomIcons().get(i));
            }

            roomState.setRoomStateChanged(false);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        fragmentSwitch(R.id.nav_home);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){


            case R.id.Review:
                Intent intent = new Intent(MainActivity.this,ReviewAcitivity.class);
                startActivity(intent);
                break;

            case R.id.AboutApp:
                Intent intent1 = new Intent(MainActivity.this, AboutAppActivity.class);
                startActivity(intent1);
                break;

            case R.id.legal:
                Intent intent2 = new Intent(MainActivity.this, LegalActivity.class);
                startActivity(intent2);
                break;

            case R.id.ReportAnIssue:
                Intent intent3 = new Intent(MainActivity.this, ReportIssue.class);
                startActivity(intent3);
                break;

            case R.id.Exit:

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
                break;

        }

        return super.onOptionsItemSelected(item);

    }


    public static void setRoomNameAndIcon(int id, String title, Drawable icon){

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(id);
        menuItem.setTitle(title);
        menuItem.setIcon(icon);
    }

    public void loadRoomInfo(int id){

        NavigationView navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Menu menu = navView.getMenu();
        MenuItem item = menu.findItem(id);
        toolbar.setTitle(item.getTitle());
    }

    public static ArrayList<Integer> getMenuIds(){
        return menuIds;
    }

    public static NavigationView getNavigationView(){return navigationView;}

    public static Context getMainActivityContext(){

        return context;
    }

    public static NotificationCompat.Builder getBuilder(){
        return builder;
    }


    public void fragmentSwitch(int id){

        Menu menu = navigationView.getMenu();

        switch(id){

            case R.id.nav_settings:
                loadFragment(id, getString(R.string.settings), new SettingsFragment());
                break;
            case R.id.nav_home:
                loadFragment(id, getString(R.string.home), new LandingFragment());
                break;
            default:
                String tag = menu.findItem(id).getTitle().toString();
                loadFragment(id, tag, getSupportFragmentManager().findFragmentByTag(tag));
        }
    }

    private void loadFragment(int id, String title, Fragment fragment){

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        Toolbar toolbarText = findViewById(R.id.toolbar);
        MenuItem navLoad = menu.findItem(id);

        toolbarText.setTitle(title);
        navLoad.setChecked(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment,
                null).setReorderingAllowed(true).commit();

    }

    public void setToolbarTitle(String title){

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    public void gettingData(){
        arrayList = new ArrayList<>();
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference().child("AccessCards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    arrayList.add(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void buildNotification(String alarmType, String notificationIdentifier, PendingIntent pendingIntent, FirebaseConnect fC){

        if(!notificationIdentifier.isEmpty()) {
            DatabaseReference reference = fC.getSensorDataRef(notificationIdentifier);

            builder = new NotificationCompat.Builder(this, "NOTIFICATIONS")
                    .setSmallIcon(R.drawable.thermostat)
                    .setContentText(alarmType)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String sensorData = snapshot.getValue(String.class);
                    if (sensorData != null)
                        new Notifications(sensorData, fC.getDeviceType(notificationIdentifier));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}