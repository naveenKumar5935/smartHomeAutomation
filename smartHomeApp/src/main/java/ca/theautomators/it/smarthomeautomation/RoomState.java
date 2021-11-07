package ca.theautomators.it.smarthomeautomation;

import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class RoomState {

    private static RoomState INSTANCE = null;
    private int numRooms;
    private ArrayList<String> roomNames;
    private boolean roomStateChanged;
    private ArrayList<Integer> roomIds;
    private ArrayList<Drawable> roomIcons;

    //TODO Save state to shared preferences

    private RoomState(){

        roomStateChanged = false;
        roomNames  = new ArrayList<>();
        roomIcons = new ArrayList<>();

        NavigationView navView = MainActivity.getNavigationView();
        Menu menu = navView.getMenu();
        roomIds = MainActivity.getRoomList();
        numRooms = roomIds.size();

        for(int i = 0; i < roomIds.size(); i++){

            MenuItem menuItem = menu.findItem(roomIds.get(i));
            roomNames.add((String)menuItem.getTitle());
            roomIcons.add(menuItem.getIcon());
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
        roomStateChanged = true;
    }

    public void changeRoomIcons(Drawable icon, int index){

        roomIcons.set(index, icon);
        roomStateChanged = true;
    }

    public ArrayList<String> getRoomNames(){

        return roomNames;
    }

    public ArrayList<Drawable> getRoomIcons(){

        return roomIcons;
    }

    public void setRoomStateChanged(boolean bit){

        roomStateChanged = bit;
    }


    public boolean getRoomStateChanged(){

        return roomStateChanged;
    }

    public int getNumRooms(){
        return numRooms;
    }

    public ArrayList<Integer> getRoomIds(){

        return roomIds;
    }
}

