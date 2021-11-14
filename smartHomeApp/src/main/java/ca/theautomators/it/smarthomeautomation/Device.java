/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import com.google.firebase.database.DatabaseReference;

public class Device {

    private final String type;
    private final String identifier;

    FirebaseConnect fC;

    //This class uses a dependency injection, which is a Creational design pattern
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
