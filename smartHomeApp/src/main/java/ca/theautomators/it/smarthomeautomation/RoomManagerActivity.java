/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.paperdb.Paper;

public class RoomManagerActivity extends AppCompatActivity {


    private ArrayList<String> roomNames, displayedRooms;
    private ArrayList<EditText> editTexts;
    private RoomState roomState;
    private int numRooms, totalRows;
    private LinearLayout linearLayout;
    private boolean deleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_manager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.room_manager_title));
        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        FirebaseConnect fC = FirebaseConnect.getInstance();

        roomState = RoomState.getInstance(null);

        deleted = false;

        //Store current state of room names and create displayed room names for editing
        roomNames = roomState.getRoomNames();
        displayedRooms = new ArrayList<>(roomNames);
        editTexts = new ArrayList<>();
        numRooms = roomState.getNumRooms();
        totalRows = numRooms;

        linearLayout = findViewById(R.id.roommanager);

        linearLayout = buildLayout();

        display();

        Button save = findViewById(R.id.savebutton);
        ImageButton add = findViewById(R.id.addButton);
        ImageButton delete = findViewById(R.id.deleteButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                displayedRooms = new ArrayList<>();

                for(int i = 1; i < linearLayout.getChildCount(); i++){

                    LinearLayout temp = (LinearLayout) linearLayout.getChildAt(i);

                    if(temp.getChildAt(0) instanceof EditText){

                        String roomName = ((EditText) temp.getChildAt(0)).getText().toString();

                        if(!roomName.isEmpty())
                            displayedRooms.add(roomName);
                    }

                    if(temp.getChildAt(1) instanceof Spinner){
                        String roomName = ((EditText) temp.getChildAt(0)).getText().toString();
                        int drawableID = getDrawableId(((Spinner) temp.getChildAt(1)).getSelectedItemPosition());
                        if(drawableID != -1)
                            roomState.saveRoomIcon(drawableID, roomName);
                    }
                }

                if(displayedRooms.size() > totalRows){

                    int delta = displayedRooms.size() - totalRows;

                    for(int i = 0; i < delta; i++){

                        displayedRooms.remove(displayedRooms.size() - 1);
                    }
                }

                roomNames = displayedRooms;
                roomState.saveRoomNames(displayedRooms);

                ArrayList<Room> rooms = roomState.loadBuiltRooms();
                ArrayList<Room> toRemove = new ArrayList<>();

                if (rooms.size() > 0) {
                    for (Room room : rooms) {
                        if (!roomNames.contains(room.getTitle())) {
                            toRemove.add(room);
                        }
                    }

                    //check from rooms that need to be added
                    for (int i = 0; i < roomNames.size(); i++) {

                        boolean found = false;

                        for (int j = 0; j < rooms.size(); j++) {

                            if(rooms.get(j).getTitle() != null){
                                if (rooms.get(j).getTitle().equals(roomNames.get(i))) {
                                    found = true;
                                    break;
                                }
                            }
                        }

                        if (!found) {
                            rooms.add(new Room(roomNames.get(i)));
                        }
                    }

                } else {
                    for (String roomName : roomNames) {

                        rooms.add(new Room(roomName));
                    }
                }

                roomState.save();

                AlertDialog.Builder noRooms = new AlertDialog.Builder(RoomManagerActivity.this);

                if(roomState.getNumRooms() == 0){

                    noRooms.setIcon(R.drawable.home);
                    noRooms.setTitle(R.string.no_rooms_added);
                    noRooms.setMessage(R.string.add_at_least_one_room);
                    noRooms.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });

                    noRooms.show();

                }
                else{
                    AlertDialog.Builder option = new AlertDialog.Builder(RoomManagerActivity.this);
                    AlertDialog.Builder alert = new AlertDialog.Builder(RoomManagerActivity.this);

                    if (FirebaseConnect.getInstance().getFirebaseConnectivity()) {

                        option.setIcon(R.drawable.home);
                        option.setTitle(R.string.room_saved);
                        option.setMessage(R.string.would_you_like_to_save_sensors);
                        option.setPositiveButton(R.string.alert_positive_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(RoomManagerActivity.this, ManageDeviceActivity.class);
                                startActivity(intent);
                            }
                        });

                        option.setNegativeButton(R.string.alert_negative_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.setIcon(R.drawable.home);
                                alert.setTitle(R.string.dont_forget);
                                alert.setMessage(R.string.save_sensors_any_time_instructions);
                                alert.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ArrayList<Room> rooms = roomState.loadBuiltRooms();
                                        ArrayList<Room> toRemove = new ArrayList<>();

                                        if (rooms.size() > 0) {
                                            for (Room room : rooms) {
                                                if (!roomNames.contains(room.getTitle())) {
                                                    toRemove.add(room);
                                                }
                                            }

                                            //check from rooms that need to be added
                                            for (int i = 0; i < roomNames.size(); i++) {

                                                boolean found = false;

                                                for (int j = 0; j < rooms.size(); j++) {

                                                    if(rooms.get(j).getTitle() != null){
                                                        if (rooms.get(j).getTitle().equals(roomNames.get(i))) {
                                                            found = true;
                                                            break;
                                                        }
                                                    }
                                                }

                                                if (!found) {
                                                    rooms.add(new Room(roomNames.get(i)));
                                                }
                                            }

                                        } else {
                                            for (String roomName : roomNames) {

                                                rooms.add(new Room(roomName));
                                            }
                                        }

                                        rooms.removeAll(toRemove);
                                        roomState.saveBuiltRooms(rooms);

                                        Intent intent = new Intent(RoomManagerActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                alert.show();
                            }
                        });

                        option.show();
                    } else {
                        option.setIcon(android.R.drawable.ic_dialog_alert);
                        option.setTitle(R.string.no_sensors_found);
                        option.setMessage(R.string.double_check_sensors);
                        option.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ArrayList<Room> rooms = roomState.loadBuiltRooms();
                                ArrayList<Room> toRemove = new ArrayList<>();

                                //Check for rooms that need to be deleted
                                for (Room room : rooms) {
                                    if (!roomNames.contains(room.getTitle())) {
                                        toRemove.add(room);
                                    }
                                }

                                //check from rooms that need to be added
                                for (int i = 0; i < roomNames.size(); i++) {

                                    boolean found = false;

                                    for (int j = 0; j < rooms.size(); j++) {

                                        if (rooms.get(j).getTitle().equals(roomNames.get(i))) {
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (!found) {
                                        rooms.add(new Room(roomNames.get(i)));
                                    }
                                }

                                rooms.removeAll(toRemove);
                                roomState.saveBuiltRooms(rooms);

                                Intent intent = new Intent(RoomManagerActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                        option.show();
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalRows < 10){
                        if (displayedRooms.size() > 0)
                            linearLayout.addView(addRow(linearLayout.getChildCount() - 1));
                        else
                            linearLayout.addView(addRow(null));

                        totalRows++;
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalRows > 0){
                    totalRows--;
                    linearLayout = buildLayout();

                    display();
                }
            }
        });
    }

    private LinearLayout buildLayout(){

        if(linearLayout != null)
            linearLayout.removeAllViews();

        TextView text = new TextView(this);
        text.setText(R.string.edit_room_name_and_type);
        text.setTextSize(18);
        linearLayout.addView(text);

        return linearLayout;
    }

    private int getDrawableId(int selection){

        int resource;

        switch(selection){

            case 1:
                resource = R.drawable.bedroom;
                break;
            case 2:
                resource = R.drawable.bathroom;
                break;
            case 3:
                resource = R.drawable.child_room;
                break;
            case 4:
                resource = R.drawable.kitchen;
                break;
            case 5:
                resource = R.drawable.living_room;
                break;
            case 6:
                resource = R.drawable.garage;
                break;
            case 7:
                resource = R.drawable.office;
                break;
            case 8:
                resource = R.drawable.game_room;
                break;
            case 9:
                resource = R.drawable.backyard;
                break;
            case 10:
                resource = R.drawable.balcony;
                break;
            case 11:
                resource = R.drawable.music_room;
                break;
            default:
                resource = -1;

        }

        return resource;
    }

    private void display(){

        numRooms = displayedRooms.size();

        for(int i = 0; i < totalRows; i++) {
            //add row to layout
            if(i < numRooms){
                linearLayout.addView(addRow(i));
            }
            else{

                linearLayout.addView((addRow(null)));
            }
        }
    }

    private LinearLayout addRow(@Nullable Integer index){

        //Row layout setup
        LinearLayout entryRow = new LinearLayout(this);
        entryRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        //Text entry setup
        EditText editText = new EditText(this);
        if(index == null){
            editText.setHint(R.string.enter_room_name);
        }
        else{
            if(index < displayedRooms.size())
                editText.setText(displayedRooms.get(index));
            else
                editText.setHint(R.string.enter_room_name);
        }
        editText.setPadding(20, 20, 20, 20);
        editText.setLayoutParams(params);

        //drop down menu setup
        Spinner spinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPadding(20, 20, 20, 20);
        spinner.setLayoutParams(params);

        //add text entry and spinner to row
        entryRow.addView(editText);
        entryRow.addView(spinner);

        return entryRow;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RoomManagerActivity.this, MainActivity.class);
        startActivity(intent);
    }
}