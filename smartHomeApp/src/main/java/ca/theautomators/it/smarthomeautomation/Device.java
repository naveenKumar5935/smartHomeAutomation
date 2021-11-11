package ca.theautomators.it.smarthomeautomation;

import com.google.firebase.database.DatabaseReference;

public class Device {

    private String type;
    private String identifier;

    FirebaseConnect fC;

    public Device(String identifier){

        fC = FirebaseConnect.getInstance();

        this.identifier = identifier;
        type = fC.getDeviceType(identifier);
    }

    public DatabaseReference getSensorData(String identifier){

        return fC.getSensorData(identifier);
    }

    public void sendControlData(String controlData){

        fC.sendControlData(controlData, identifier);
    }

    public String getType(){

        return type;
    }

    public String getIdentifier(){

        return identifier;
    }


}
