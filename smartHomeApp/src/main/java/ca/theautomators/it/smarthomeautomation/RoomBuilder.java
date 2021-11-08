package ca.theautomators.it.smarthomeautomation;

import java.util.ArrayList;

public class RoomBuilder {

    private static ArrayList<Device> devices;
    private static String[] identifiers;

    public static void createRooms(ArrayList<Device> deviceList, String[] identifierList){

        devices = deviceList;
        identifiers = identifierList;
    }

    public static void deleteRoom(int id){

        createRooms(devices, identifiers);
    }

    public void addNavItem(int id){

    }

    public void addControls(int id){


    }

    public void setSensorData(String sensorData){

    }
}
