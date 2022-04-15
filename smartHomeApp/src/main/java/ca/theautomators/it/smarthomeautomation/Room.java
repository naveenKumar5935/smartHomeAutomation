/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import java.util.ArrayList;

public class Room {

    private ArrayList<String> devices;
    private String title, rfid, smoke, light, pir, temp, humid;

    public Room(String title){

        this.title = title;
        devices = new ArrayList<>();
        rfid = smoke = light = pir = temp = humid = null;
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

        if(!devices.isEmpty())
            return devices;
        else
            return null;
    }

    public void setData(String type, String data){

        switch(type){
            case "SMOKE":
                smoke = data;
                break;
            case "RFID":
                rfid = data;
                break;
            case "LIGHT":
                light = data;
                break;
            case "PIR":
                pir = data;
                break;
            case "TEMP":
                temp = data;
                break;
            case "HUMID":
                humid = data;

        }
    }

    public String getData(){

        String data = "";

        if(rfid != null){
            data += rfid + "\n";
        }
        if(smoke != null){
            data += smoke + "\n";
        }
        if(light != null){
            data += light + "\n";
        }
        if(pir != null){
            data += pir + "\n";
        }
        if(temp != null){
            data += temp + "\n";
        }
        if(humid != null){
            data += humid + "\n";
        }

        return data;
    }
}
