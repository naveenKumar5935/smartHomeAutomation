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
import android.view.MenuItem;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RoomState extends AsyncTask<Void, Void, Void> {

    private static RoomState INSTANCE = null;
    private int numRooms;
    private ArrayList<String> roomNames;
    private boolean roomStateChanged, stateSaved;
    private ArrayList<Integer> menuIds;
    private ArrayList<Drawable> roomIcons;
    private ArrayList<Integer> drawableIds;
    private Context context;
    private ArrayList<MenuItem> menuItems;
    private SharedPreferences state;

    private RoomState(Context context){

        if(context != null)
            this.context = context;

        state = context.getSharedPreferences("room_state", Context.MODE_PRIVATE);
        roomStateChanged = false;
        stateSaved = state.getBoolean("stateSaved", false);

        loadState();

        //to help with setting dynamically created menu items as checked or not checked when selected
        menuItems = new ArrayList<>();

    }

    public static RoomState getInstance(@Nullable Context context){

        if(INSTANCE == null){

            synchronized (RoomState.class){

                INSTANCE = new RoomState(context);
            }
        }

        return(INSTANCE);
    }

    public void saveRoomNames(ArrayList<String> roomNames){

        this.roomNames = new ArrayList<>(roomNames);
        roomStateChanged = true;
        numRooms = roomNames.size();
        doInBackground();
    }

    public void saveRoomIcon(int iconId, String roomName){

        SharedPreferences.Editor editor = state.edit();
        editor.putInt(roomName, iconId);
        editor.apply();

        roomStateChanged = true;
        doInBackground();
    }

    public void setMenuIds(int id, int index){

        if(menuIds.size() <= index)
            menuIds.add(id);
        else
            menuIds.set(index, id);

        doInBackground();
        roomStateChanged = true;
    }

    public void addMenuItem(MenuItem item){

        if(!menuItems.contains(item)){
            menuItems.add(item);
        }
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

    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
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

    private void loadState(){

        stateSaved = state.getBoolean("stateSaved", false);

        if(stateSaved){

            numRooms = state.getInt("numRooms", 0);

            drawableIds = new ArrayList<>();
            roomNames = new ArrayList<>();
            menuIds = new ArrayList<>();
            roomIcons = new ArrayList<>();

            for(int i = 0; i < numRooms; i++) {
                roomNames.add(state.getString("roomName_" + i, null));
                menuIds.add(state.getInt("roomId_" + i, 0));

                roomIcons.add(context.getDrawable(state.getInt(roomNames.get(i), R.drawable.bedroom)));
            }

        }
        else {

            if(roomNames == null){

                roomNames = new ArrayList<>();
                menuIds = new ArrayList<>();
                roomIcons = new ArrayList<>();
                drawableIds = new ArrayList<>();
            }


            numRooms = getNumRooms();

        }
    }

    private void saveState(){

        SharedPreferences.Editor editor = state.edit();

        numRooms = roomNames.size();

        for(int i = 0; i < numRooms; i++){

            if(menuIds.size() == numRooms)
                editor.putInt("roomId_" + i, menuIds.get(i));
            if(i < drawableIds.size()) {
                editor.putString("roomIcon_" + i, context.getResources().getResourceEntryName(drawableIds.get(i)));
            }

            editor.putString("roomName_" + i, roomNames.get(i));
        }

        stateSaved = true;

        editor.putInt("numRooms", numRooms);
        editor.putBoolean("stateSaved", stateSaved);
        editor.apply();

        loadState();

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

    public void saveBuiltRooms(ArrayList<Room> rooms){

        SharedPreferences.Editor editor = state.edit();

        for(int i = 0; i < rooms.size(); i++){

            String builtRoom = rooms.get(i).getTitle();
            ArrayList<String> identifierList = rooms.get(i).getDeviceIdentifierList();

            for(int j = 0; j < identifierList.size(); j++){

                builtRoom += ":" + identifierList.get(j);
            }

            editor.putString("builtRoom_" + i, builtRoom);
        }

        editor.putInt("numBuiltRooms", rooms.size());
        editor.apply();
    }

    public ArrayList<Room> loadBuiltRooms(){

        ArrayList<Room> rooms = new ArrayList<>();

        for(int i = 0; i < state.getInt("numBuiltRooms", 0); i++){

            String temp = state.getString("builtRoom_" + i, "_x_");
            String[] tempSplit = temp.split(":");

            Room tempRoom = new Room(tempSplit[0]);

            for(int j = 1; j < tempSplit.length; j++){

                tempRoom.addDevice(tempSplit[j]);
            }

            rooms.add(tempRoom);
        }

        return rooms;
    }

    public void saveRoomData(String roomName, ArrayList<String> data){

        SharedPreferences.Editor editor = state.edit();

        for(int i = 0; i < data.size(); i++){

            editor.putString(roomName + "_data_row_" + i, data.get(i));
        }

        editor.putInt(roomName + "num_data_rows", data.size());

        editor.apply();
    }

    public ArrayList<String> loadRoomData(String roomName){

        ArrayList<String> data = new ArrayList<>();

        int numRows = state.getInt(roomName + "num_data_rows", 0);

        for(int i = 0; i < numRows; i++){

            data.add(state.getString(roomName + "_data_row_" + i, "").trim() + "\n");
        }

        return data;
    }
}

