package ca.theautomators.it.smarthomeautomation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class RoomState {

    private static RoomState INSTANCE = null;
    private int numRooms;
    private ArrayList<String> roomNames;
    private boolean roomStateChanged, stateSaved;
    private ArrayList<Integer> roomIds;
    private ArrayList<Drawable> roomIcons;
    private int[] drawableId;
    private Context context;
    private MenuItem menuItem;
    private SharedPreferences state;


    private RoomState(Context context){

        if(context != null)
            this.context = context;

        state = context.getSharedPreferences("room_state", Context.MODE_PRIVATE);
        roomStateChanged = false;
        stateSaved = false;

        loadState();

        NavigationView navView = MainActivity.getNavigationView();
        Menu menu = navView.getMenu();


        for(int i = 0; i < roomIds.size(); i++){

            menuItem = menu.findItem(roomIds.get(i));
            roomNames.add((String)menuItem.getTitle());
            roomIcons.add(menuItem.getIcon());
        }

        //saveState();
    }

    public static RoomState getInstance(@Nullable Context context){

        if(INSTANCE == null){

            synchronized (RoomState.class){

                INSTANCE = new RoomState(context);
            }
        }

        return(INSTANCE);
    }

    public void changeRoomName(String name, int index){

        roomNames.set(index, name);
        roomStateChanged = true;
        saveState();
    }

    public void changeRoomIcons(Drawable icon, int index){

        roomIcons.set(index, icon);
        roomStateChanged = true;
        saveState();
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

    public boolean getStateSaved(){

        return stateSaved;
    }

    public int getNumRooms(){
        return numRooms;
    }

    public ArrayList<Integer> getRoomIds(){

        return roomIds;
    }

    public int[] getDrawableId(){
        return drawableId;
    }

    private void loadState(){

        //TODO This function will need to be modified once add and remove functionality developed

        stateSaved = state.getBoolean("stateSaved", false);


        if(stateSaved){

            numRooms = state.getInt("numRooms", 0);
            drawableId = new int[numRooms];

            roomNames = new ArrayList<>();
            roomIds = new ArrayList<>();
            roomIcons = new ArrayList<>();

            for(int i = 0; i < numRooms; i++) {
                roomNames.add(state.getString("roomName_" + i, null));
                roomIds.add(state.getInt("roomId_" + i, 0));
                roomIcons.add(context.getResources().getDrawable(context.getResources().getIdentifier(state.getString("roomIcon_" + i, "home"),
                        "drawable", context.getPackageName())));
            }

        }
        else {

            roomNames = new ArrayList<>();
            roomIds = new ArrayList<>();
            roomIcons = new ArrayList<>();

            roomIds.clear();
            roomIds.add(R.id.nav_bedroom);
            roomIds.add(R.id.nav_kitchen);
            roomIds.add(R.id.nav_livingroom);

            numRooms = roomIds.size();
            drawableId = new int[numRooms];

            drawableId[0] = R.drawable.bedroom;
            drawableId[1] = R.drawable.kitchen;
            drawableId[2] = R.drawable.living_room;

        }
    }

    private void saveState(){

        SharedPreferences.Editor editor = state.edit();
        editor.putInt("numRooms", numRooms);
        editor.putBoolean("stateSaved", stateSaved);

        for(int i = 0; i < numRooms; i++){

            editor.putInt("roomId_" + i, roomIds.get(i));
            if(drawableId[i] != 0)
                editor.putString("roomIcon_" + i, context.getResources().getResourceEntryName(drawableId[i]));
            editor.putString("roomName_" + i, roomNames.get(i));
        }

        editor.apply();
        stateSaved = true;

    }

}

