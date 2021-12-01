/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.theautomators.it.smarthomeautomation.Device;
import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.Room;
import ca.theautomators.it.smarthomeautomation.RoomState;

public class RoomFragment extends Fragment {

    private Room thisRoom;
    private LinearLayout deviceControllers;
    private View root;
    private String data;
    private ArrayList<SwitchCompat> controls;
    private String title;
    private boolean smokeDetected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        root = inflater.inflate(R.layout.fragment_room, container, false);

        controls = new ArrayList<>();

        RoomState rS = RoomState.getInstance(null);

        title = "";
        smokeDetected = false;

        ArrayList<Room> rooms = rS.loadBuiltRooms();

        //Load room data from arguments
        Bundle bundle = this.getArguments();
        if (bundle != null) {

                title = bundle.getString("title", "Room");
        }

        ((MainActivity)getActivity()).setToolbarTitle(title);

        String sensorTitle = title + " " + getString(R.string.sensor_data);
        TextView roomTitle = root.findViewById(R.id.room_data_title);
        roomTitle.setText(sensorTitle);

        for(Room room : rooms){

            if(room.getTitle().equals(title)){
                thisRoom = room;
            }
        }

        deviceControllers = root.findViewById(R.id.controls);

        data = "";

        buildRoom();


        return root;
    }

    private void buildRoom(){

        for(String device : thisRoom.getDeviceIdentifierList()){

            Device dev = new Device(device);

            if(!(dev.getType().equals("HUMID") || dev.getType().equals("TEMP"))){

                deviceControllers.addView(buildController(dev));
            }

            if(!(dev.getType().equals("SERVO"))){

                buildSensorReceiver(dev);
            }
        }
    }

    private LinearLayout buildController(Device device){

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(170, 170);
        params.weight = 1;

        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(getDrawable(device.getType()));
        icon.setLayoutParams(params);

        SwitchCompat control = new SwitchCompat(getContext());
        control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(device.getType().equals("LIGHT")){
                    if(isChecked){
                        device.sendControlData("1:000000");
                        icon.setImageDrawable(getDrawable("LIGHTON"));
                    }
                    else{
                        device.sendControlData("0:000000");
                        icon.setImageDrawable(getDrawable("LIGHT"));
                    }
                }
                else if(device.getType().equals("SMOKE")){
                    if(isChecked && !smokeDetected){
                        device.sendControlData("1:0");
                    }
                    else if(isChecked && smokeDetected){
                        device.sendControlData("1:1");
                    }
                    else{
                        device.sendControlData("0:0");
                        smokeDetected = false;
                    }
                }
                else if(device.getType().equals("RFID")){
                    if(isChecked){
                        device.sendControlData("1:0");
                    }
                    else{
                        device.sendControlData("0:0");
                    }
                }
                else if(device.getType().equals("PIR")){
                    if(isChecked){
                        device.sendControlData("1:0");
                    }
                    else{
                        device.sendControlData("0:0");
                    }
                }
                else if(device.getType().equals("SERVO")){
                    if(isChecked){
                        device.sendControlData("1:0");
                        icon.setImageDrawable(getDrawable("SERVOOPEN"));
                    }
                    else{
                        device.sendControlData("0:0");
                        icon.setImageDrawable(getDrawable("SERVO"));
                    }
                }
            }
        });
        control.setLayoutParams(params);

        control.setText(device.getType());
        controls.add(control);

        row.addView(icon);
        row.addView(control);

        return row;
    }

    private void buildSensorReceiver(Device device){

        device.getSensorData().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                data = "";
                String dataRead = snapshot.getValue(String.class);
                TextView roomData = root.findViewById(R.id.room_data);
                roomData.setMovementMethod(new ScrollingMovementMethod());
                final int scrollAmount = roomData.getLayout().getLineTop(roomData.getLineCount()) - roomData.getHeight();
                if (scrollAmount > 0)
                    roomData.scrollTo(0, scrollAmount);
                else
                    roomData.scrollTo(0, 0);

                if(device.getType().equals("LIGHT")){
                    data += "Lights: ";

                    if(dataRead.split(":")[0].equals("0")){
                        data += "OFF";
                        setControlState(false, "LIGHT");
                    }
                    else{
                        data += "ON";
                        setControlState(true, "LIGHT");
                    }
                }
                else if(device.getType().equals("SMOKE")){

                    if(dataRead.equals("0:0")){
                        data += "Smoke detector: OFF";
                        setControlState(false, "SMOKE");
                    }
                    else if(dataRead.split(":")[0].equals("1")){
                        data += "Smoke detector: ON";
                        setControlState(true, "SMOKE");
                    }
                    else if(dataRead.split(":")[1].equals("1")){
                        data += "Smoke Alarm On!";
                        smokeDetected = true;
                        setControlState(true, "SMOKE");
                    }
                }
                else if(device.getType().equals("RFID")){

                    if(dataRead.equals("0:0")){
                        data += "RFID: OFF (Locked)";
                        setControlState(false, "RFID");
                    }
                    else if(dataRead.split(":")[0].equals("1") && dataRead.split(":")[1].equals("0")){
                        data += "RFID: ON";
                        setControlState(true, "RFID");
                    }
                    else if(dataRead.split(":")[0].equals("1") && !dataRead.split(":")[1].equals("0")){
                        data += "RFID scanned: " + dataRead.split(":")[1];
                    }
                }
                else if(device.getType().equals("PIR")){

                    if(dataRead.equals("0:0")){
                        data += "Motion Detector: OFF";
                        setControlState(false, "PIR");
                    }
                    else if(dataRead.equals("1:0")){
                        data += "Motion Detector: ON";
                        setControlState(true, "PIR");
                    }
                    else if(dataRead.equals("1:1")){
                        data += "Motion detected in: " + title + "!";
                    }
                }
                else if(device.getType().equals("TEMP") || device.getType().equals("HUMID")){
                    String[] tempArr = dataRead.split(":");
                    String temp = device.getType().equals("TEMP") ? "Temperature: " : "Humidity: ";
                    for(String string : tempArr){
                        temp += string;
                    }

                    data += temp;
                }

                data += " - " + getCurrentTime() + "\n";
                roomData.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Drawable getDrawable(String type){

        switch (type) {
            case "RFID":
                return ContextCompat.getDrawable(getContext(), R.drawable.rfid_nobg);
            case "PIR":
                return ContextCompat.getDrawable(getContext(), R.drawable.motion_detection_nobg);
            case "SERVO":
                return ContextCompat.getDrawable(getContext(), R.drawable.door_closed_nobg);
            case "SERVOOPEN":
                return ContextCompat.getDrawable(getContext(), R.drawable.door_open_nobg);
            case "SMOKE":
                return ContextCompat.getDrawable(getContext(), R.drawable.smoke);
            case "TEMP":
                return ContextCompat.getDrawable(getContext(), R.drawable.thermostat);
            case "HUMID":
                return ContextCompat.getDrawable(getContext(), R.drawable.humidity);
            case "LIGHT":
                return ContextCompat.getDrawable(getContext(), R.drawable.lights_off_nobg);
            case "LIGHTON":
                return ContextCompat.getDrawable(getContext(), R.drawable.lights_on_nobg);
            default:
                return null;
        }
    }

    private void setControlState(boolean state, String name){

        for(SwitchCompat control : controls){

            if(name.equals(control.getText())){
                control.setChecked(state);
            }
        }
    }

    private String getCurrentTime(){

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = new Date();

        return formatter.format(date);
    }
}