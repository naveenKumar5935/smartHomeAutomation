/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class RoomState extends AsyncTask<Void, Void, Void> {

    private static RoomState INSTANCE = null;
    private int numRooms;
    private ArrayList<String> roomNames;
    private boolean roomStateChanged, stateSaved;
    private ArrayList<Integer> menuIds;
    private ArrayList<Drawable> roomIcons;
    private int[] drawableIds;
    private Context context;
    private ArrayList<MenuItem> menuItems;
    private SharedPreferences state;
    private String[] identifiers;
    NavigationView navView;
    Menu menu;


    private RoomState(Context context){

        if(context != null)
            this.context = context;

        state = context.getSharedPreferences("room_state", Context.MODE_PRIVATE);
        roomStateChanged = false;
        stateSaved = false;

        loadState();

        navView = MainActivity.getNavigationView();
        menu = navView.getMenu();

        menuItems = new ArrayList<>();


//        for(int i = 0; i < menuIds.size(); i++){
//
//            menuItem = menu.findItem(menuIds.get(i));
//            roomNames.add((String)menuItem.getTitle());
//            roomIcons.add(menuItem.getIcon());
//        }

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
        doInBackground();
    }

    public void changeRoomIcons(Drawable icon, int index){

        roomIcons.set(index, icon);
        roomStateChanged = true;
        doInBackground();
    }

    public void addMenuItem(MenuItem item){

        menuItems.add(item);
    }

    public void resetMenuItems(){
        menuItems.clear();
    }

    public void uncheckOtherItems(MenuItem item){

        for(MenuItem  index: menuItems){

            if(!(item.getItemId() == index.getItemId())){

                index.setChecked(false);
            }

        }
    }

    public ArrayList<String> getRoomNames(){

        return roomNames;
    }

    public ArrayList<Drawable> getRoomIcons(){

        return roomIcons;
    }

    public ArrayList<Integer> getMenuIds(){

        return menuIds;
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

        numRooms = roomNames.size();

        return numRooms;
    }


    public int[] getDrawableIds(){


        if(drawableIds.length != getNumRooms()){
            drawableIds = new int[getNumRooms()];
        }

        return drawableIds;
    }

    private void loadState(){

        //TODO This function will need to be modified once add and remove functionality developed

        stateSaved = state.getBoolean("stateSaved", false);


        if(stateSaved){

            numRooms = state.getInt("numRooms", 0);
            drawableIds = new int[numRooms];

            roomNames = new ArrayList<>();
            menuIds = new ArrayList<>();
            roomIcons = new ArrayList<>();

            for(int i = 0; i < numRooms; i++) {
                roomNames.add(state.getString("roomName_" + i, null));
                menuIds.add(state.getInt("roomId_" + i, 0));
                roomIcons.add(context.getResources().getDrawable(context.getResources().getIdentifier(state.getString("roomIcon_" + i, "home"),
                        "drawable", context.getPackageName())));
            }

        }
        else {

            if(roomNames == null){

                roomNames = new ArrayList<>();
                menuIds = new ArrayList<>();
                roomIcons = new ArrayList<>();
            }


            numRooms = getNumRooms();
            drawableIds = new int[numRooms];

        }
    }

    private void saveState(){

        SharedPreferences.Editor editor = state.edit();
        editor.putInt("numRooms", numRooms);
        editor.putBoolean("stateSaved", stateSaved);

        for(int i = 0; i < numRooms; i++){

            editor.putInt("roomId_" + i, menuIds.get(i));
            if(drawableIds[i] != 0)
                editor.putString("roomIcon_" + i, context.getResources().getResourceEntryName(drawableIds[i]));
            editor.putString("roomName_" + i, roomNames.get(i));
        }

        editor.apply();
        stateSaved = true;

    }

    public void save(){

        doInBackground();
    }



    @Override
    protected Void doInBackground(Void... voids) {

        saveState();
        stateSaved = true;

        return null;
    }

    public void saveIdentifiers(String[] identifiers){

        StringBuilder identifierMerge = new StringBuilder();

        for(int i = 0; i < identifiers.length; i++){

            if(i == identifiers.length -1){

                identifierMerge.append(identifiers[i]);
                break;
            }

            identifierMerge.append(identifiers[i]).append(":");
        }

        SharedPreferences.Editor editor = state.edit();
        editor.putString("identifiers", identifierMerge.toString());
        editor.apply();

    }

    public String[] loadIdentifiers(){

        return state.getString("identifiers", "").split(":");
    }
}

