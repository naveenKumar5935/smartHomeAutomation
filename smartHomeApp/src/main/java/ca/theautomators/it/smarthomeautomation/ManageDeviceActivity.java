/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ManageDeviceActivity extends AppCompatActivity {

    private String[] identifiers;
    private ArrayList<Device> devices;
    private LinearLayout linearLayout;
    private ArrayList<String> roomNames;
    private ArrayList<Room> rooms;
    private FirebaseConnect fC;
    private boolean loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_device);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fC = FirebaseConnect.getInstance(null);
        loaded = fC.devicesloaded();

//        if(!loaded){
//            AsyncLoader loader = new AsyncLoader();
//            loader.execute();
//        }



        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        SharedPreferences currentUser = ManageDeviceActivity.this.getSharedPreferences("current_user", Context.MODE_PRIVATE);
        String userID = currentUser.getString("current_user", "no_user");
        FirebaseConnect fC = FirebaseConnect.getInstance(userID);

        RoomState rS = RoomState.getInstance(null);

        setTitle(getString(R.string.device_manager_title));

        //Get device identifiers from shared preferences
        identifiers = rS.loadIdentifiers();

        //Get Room names from RoomState
        roomNames = new ArrayList<>();
        roomNames.add(getString(R.string.select_room));
        roomNames.addAll(rS.getRoomNames());

        rooms = new ArrayList<>();

        for(String room : rS.getRoomNames())
            rooms.add(new Room(room));


        //if null, get list from database and save to shared preferences
        if(identifiers[0].isEmpty()){
            identifiers = fC.getIdentifiers();
            rS.saveIdentifiers(identifiers);
        }
        else if(fC.getNumDevices() != identifiers.length){
            identifiers = fC.getIdentifiers();


            if(FirebaseConnect.getInstance(null).getFirebaseConnectivity()){
                rS.saveIdentifiers(identifiers);
            }
        }

        devices = new ArrayList<>();

        //Creates array list of devices using identifiers
        if(identifiers != null){
            for (String identifier : identifiers) {

                devices.add(new Device(identifier));
            }
        }

        linearLayout = findViewById(R.id.devicemanager);
        if(linearLayout != null)
            linearLayout.removeAllViews();

        display();

        Button save = findViewById(R.id.savebutton);

        if(!loaded) {
            save.setEnabled(false);
            save.setBackgroundColor(0x000000);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rS.saveBuiltRooms(rooms);

                Intent intent = new Intent(ManageDeviceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void display(){

        for(int i = 0; i < devices.size(); i++){

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(170, 170);
            params.weight = 1;

            TextView text = new TextView(this);
            text.setText(devices.get(i).getType());
            text.setLayoutParams(params);
            text.setPadding(20, 20, 20, 20);


            ImageView icon = new ImageView(this);
            icon.setImageDrawable(getDrawable(devices.get(i).getType()));
            icon.setLayoutParams(params);

            Spinner spinner = new Spinner(this);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setPadding(20, 20, 20, 20);
            spinner.setBackgroundColor(getColor(R.color.white));
            spinner.setAlpha((float)(0.6));

            spinner.setLayoutParams(params);

            int finalI = i;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position != 0){
                        for (Room room : rooms) {

                            room.removeDevice(devices.get(finalI).getIdentifier());

                            if (room.getTitle().equals(roomNames.get(position))) {
                                room.addDevice(devices.get(finalI).getIdentifier());
                            }
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    for(Room room: rooms){
                        room.removeDevice(devices.get(finalI).getIdentifier());
                    }

                }
            });

            row.addView(icon);
            row.addView(text);
            row.addView(spinner);
            linearLayout.addView(row);
        }

    }

    //An example of the Keep It Simple and Stupid Design principle. This functionality could have easily been incorporated into the display() function
    //But extracting it to it's own method means it can be used elsewhere in the class
    private Drawable getDrawable(String type){

        if(type.equals("RFID")){
            return getDrawable(R.drawable.rfid_nobg);
        }
        else if(type.equals("PIR")){
            return getDrawable(R.drawable.motion_detection_nobg);
        }
        else if(type.equals("SERVO")){
            return getDrawable(R.drawable.door_closed_nobg);
        }
        else if(type.equals("SMOKE")){
            return getDrawable(R.drawable.smoke);
        }
        else if(type.equals("TEMP")){
            return getDrawable(R.drawable.thermostat);
        }
        else if(type.equals("HUMID")){
            return getDrawable(R.drawable.humidity);
        }
        else if(type.equals("LIGHT")){
            return getDrawable(R.drawable.lights_off_nobg);
        }
        else{
            return null;
        }
    }

//    public class AsyncLoader extends AsyncTask<Void, Void, Void> {
//
//        ProgressDialog progressDialog;
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//
//            while(!loaded)
//                loaded = fC.isDevicesLoaded();
//
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute(){
//            loaded = fC.isDevicesLoaded();
//
//            progressDialog = new ProgressDialog(ManageDeviceActivity.this);
//            progressDialog.setMessage("Searching for devices....");
//            progressDialog.setCancelable(false);
//
//            progressDialog.show();
//
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//
//            if(loaded){
//                progressDialog.dismiss();
//                finish();
//                startActivity(getIntent());
//            }
//
//        }
//
//
//    }


}

