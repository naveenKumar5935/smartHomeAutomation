package ca.theautomators.it.smarthomeautomation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AddDeviceActivity extends AppCompatActivity {

    private int numDevices;
    private String[] identifiers;
    private ArrayList<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        FirebaseConnect fC = FirebaseConnect.getInstance();
        devices = new ArrayList<>();

        numDevices = fC.getNumDevices();
        identifiers = fC.getIdentifiers();
        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        for(int i = 0; i < numDevices; i++){

            devices.add(new Device(identifiers[i]));
        }


        //TODO add functionality to display devices and link them to generic room fragment
    }
}