/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import java.io.Serializable;
import java.util.ArrayList;

public class Room {

    private ArrayList<String> devices;
    private String title;

    public Room(String title){

        this.title = title;
        devices = new ArrayList<>();
    }

    public void addDevice(String identifier){

        devices.add(identifier);
    }

    public void removeDevice(String identifier){

        for(int i = 0; i < devices.size(); i++){

            if(devices.get(i).equals(identifier)){

                devices.remove(devices.get(i));
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getDeviceIdentifierList() {
        return devices;
    }
}
