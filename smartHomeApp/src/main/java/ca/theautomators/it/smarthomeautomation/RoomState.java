package ca.theautomators.it.smarthomeautomation;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class RoomState {

    private static RoomState INSTANCE = null;
    private int numRooms;
    private ArrayList<String> roomNames = new ArrayList<>();
    private boolean roomNameChanged = false;
    private ArrayList<Integer> roomIds;

    //TODO add room icon to state

    private RoomState(){

        NavigationView navView = MainActivity.getNavigationView();
        Menu menu = navView.getMenu();
        roomIds = MainActivity.getRoomList();
        numRooms = roomIds.size();

        for(int i = 0; i < roomIds.size(); i++){

            MenuItem menuItem = menu.findItem(roomIds.get(i));
            roomNames.add((String)menuItem.getTitle());
        }
    }

    public static RoomState getInstance(){

        if(INSTANCE == null){

            synchronized (RoomState.class){

                INSTANCE = new RoomState();
            }
        }

        return(INSTANCE);
    }

    public void changeRoomName(String name, int index){

        roomNames.set(index, name);
        roomNameChanged = true;
    }

    public ArrayList<String> getRoomNames(){

        return roomNames;
    }

    public void setRoomNameChanged(boolean bit){

        roomNameChanged = bit;
    }

    public boolean getRoomNameChanged(){

        return roomNameChanged;
    }

    public int getNumRooms(){
        return numRooms;
    }

    public ArrayList<Integer> getRoomIds(){

        return roomIds;
    }
}

