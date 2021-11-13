package ca.theautomators.it.smarthomeautomation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ManageDeviceActivity extends AppCompatActivity {

    private String[] identifiers;
    private ArrayList<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        FirebaseConnect fC = FirebaseConnect.getInstance();
        RoomState rS = RoomState.getInstance(null);

        //Get device identifiers from shared preferences
        identifiers = rS.loadIdentifiers();

        //if null, get list from database and save to shared preferences
        if(identifiers == null){

            identifiers = fC.getIdentifiers();
            rS.saveIdentifiers(identifiers);
        }
        //compares shared pref identifiers with database, if new devices found, gets new list
        else if(fC.checkNewDevices(identifiers)){

            identifiers = fC.getIdentifiers();
            rS.saveIdentifiers(identifiers);
        }

        devices = new ArrayList<>();
        //Creates array list of devices using identifiers
        for(int i = 0; i < identifiers.length; i++){

            devices.add(new Device(identifiers[i]));
        }




        //TODO add functionality to display devices and link them to generic room fragment
    }
}