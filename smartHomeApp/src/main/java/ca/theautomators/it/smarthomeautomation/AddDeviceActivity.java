package ca.theautomators.it.smarthomeautomation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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

        for(int i = 0; i < numDevices; i++){

            devices.add(new Device(identifiers[i]));
        }


        //TODO add functionality to display devices and link them to generic room fragment
    }
}