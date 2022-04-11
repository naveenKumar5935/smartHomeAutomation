/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import ca.theautomators.it.smarthomeautomation.FirebaseConnect;
import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.Room;
import ca.theautomators.it.smarthomeautomation.RoomState;
import top.defaults.colorpicker.ColorPickerPopup;

public class RoomFragment extends Fragment {

    private Room thisRoom;
    private LinearLayout deviceControllers;
    private View root;
    private ArrayList<SwitchCompat> controls;
    private ArrayList<String> dataList;
    private String title;
    private boolean smokeDetected;
    private RoomState rS;
    FirebaseConnect fC;
    private char hexValues[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        root = inflater.inflate(R.layout.fragment_room, container, false);

        controls = new ArrayList<>();
        dataList = new ArrayList<>();

        rS = RoomState.getInstance(null);
        fC = FirebaseConnect.getInstance(null);


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

        if((thisRoom.getDeviceIdentifierList().size() > 0) && fC.getFirebaseConnectivity()){
            buildRoom();
        }
        else if((thisRoom.getDeviceIdentifierList().size() > 0) && !fC.getFirebaseConnectivity()){
            TextView roomData = root.findViewById(R.id.room_data);
            ArrayList<String> dataList = rS.loadRoomData(thisRoom.getTitle());

            String data = "";

            for (String line : dataList) {
                data += line;
            }

            data += getString(R.string.connection_lost_no_data);

            roomData.setText(data);

            roomData.setMovementMethod(new ScrollingMovementMethod());
        }
        else{
            TextView roomData = root.findViewById(R.id.room_data);
            roomData.setText(R.string.no_sensors);
        }




        return root;
    }

    private void buildRoom(){

        ArrayList<String> lightList = new ArrayList<>();
        String pirIdentifier = null;

        for(String device : thisRoom.getDeviceIdentifierList()){

            Device dev = new Device(device);

            if(dev.getType() != null){

                if(dev.getType().equals("LIGHT"))
                    lightList.add(dev.getIdentifier());

                if(dev.getType().equals("PIR"))
                    pirIdentifier = dev.getIdentifier();

//                if (!(dev.getType().equals("HUMID") || dev.getType().equals("TEMP"))) {
//
//                    deviceControllers.addView(buildController(dev));
//
//                }

                if(dev.getType().equals("SERVO") || dev.getType().equals("LIGHT"))
                    deviceControllers.addView(buildController(dev));

                if (!(dev.getType().equals("SERVO"))) {

                    if (FirebaseConnect.getInstance(null).getFirebaseConnectivity()) {
                        buildSensorReceiver(dev);
                    }

                }
            }
        }

        if(pirIdentifier != null)
            fC.linkLightsToMotion(pirIdentifier, lightList);

    }

    private LinearLayout buildController(Device device){

        final String[] currentVal = {"1:" + rS.getHex(device)};

        device.getSensorData().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentVal[0] = "1:" + snapshot.getValue().toString().split(":")[1];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(170, 170);
        params.weight = 1;

        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(getDrawable(device.getType()));
        icon.setLayoutParams(params);

        SeekBar intensity = new SeekBar(getContext());
        Button colour = new Button(getContext());

        SwitchCompat control = new SwitchCompat(getContext());
        control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(device.getType().equals("LIGHT")){
                    String hex = rS.getHex(device);
                    if(isChecked){
                        device.sendControlData("1:" + hex);
                        intensity.setEnabled(true);
                        colour.setClickable(true);
                        intensity.setProgress(getIntensity(hex));
                        colour.setBackgroundColor(Color.parseColor("#" + hex));
                        icon.setImageDrawable(getDrawable("LIGHTON"));
                    }
                    else{
                        device.sendControlData("0:" + hex);
                        intensity.setEnabled(false);
                        colour.setClickable(false);
                        icon.setImageDrawable(getDrawable("LIGHT"));
                    }
                }
//                else if(device.getType().equals("SMOKE")){
//                    if(isChecked && !smokeDetected){
//                        device.sendControlData("1:0");
//                    }
//                    else if(isChecked){
//                        device.sendControlData("1:1");
//                    }
//                    else{
//                        device.sendControlData("0:0");
//                        smokeDetected = false;
//                    }
//                }
//                else if(device.getType().equals("RFID")){
//                    if(isChecked){
//                        device.sendControlData("1:0");
//                    }
//                    else{
//                        device.sendControlData("0:0");
//                    }
//                }
//                else if(device.getType().equals("PIR")){
//                    if(isChecked){
//                        device.sendControlData("1:0");
//                    }
//                    else{
//                        device.sendControlData("0:0");
//                    }
//                }
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


        if(device.getType().equals("LIGHT")){
            //Create slider and colour picker
            LinearLayout vert = new LinearLayout(getContext());
            vert.setOrientation(LinearLayout.VERTICAL);

            intensity.setMax(255);

            String currentHex = currentVal[0].split(":")[1];
            intensity.setProgress(getIntensity(currentHex));
            if(!control.isChecked())
                intensity.setEnabled(false);

            intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    String intensityDelta = colourIntensity(rS.getHex(device), progress);
                    colour.setBackgroundColor(Color.parseColor("#" + intensityDelta));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    if(control.isChecked()){

                        String intensityDelta = colourIntensity(rS.getHex(device), seekBar.getProgress());
                        device.sendControlData("1:" + intensityDelta);
                        colour.setBackgroundColor(Color.parseColor("#" + intensityDelta));
                        rS.saveHex(device, intensityDelta);
                    }

                }
            });

            colour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ColorPickerPopup.Builder(getContext())
                            .initialColor(Color.parseColor("#" + currentHex))
                    .enableBrightness(true)
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(v, new ColorPickerPopup.ColorPickerObserver() {
                        @Override
                        public void onColorPicked(int color) {

                            if(control.isChecked()){
                                String hex = String.format("%06X", (0xFFFFFF & color));
                                device.sendControlData("1:" + hex);
                                colour.setBackgroundColor(color);
                                intensity.setProgress(getIntensity(hex));
                                rS.saveHex(device, hex);
                            }
                        }
                    });
                }
            });

            row.addView(icon);
            row.addView(control);
            row.addView(colour);
            vert.addView(row);
            vert.addView(intensity);
            return vert;
        }
        else {
            row.addView(icon);
            row.addView(control);
            return row;
        }
    }

    private void buildSensorReceiver(Device device){

            device.getSensorData().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String data = "";
                    dataList = rS.loadRoomData(thisRoom.getTitle());


                    String dataRead = snapshot.getValue(String.class);
                    TextView roomData = root.findViewById(R.id.room_data);
                    roomData.setMovementMethod(new ScrollingMovementMethod());
                    final int scrollAmount = roomData.getLayout().getLineTop(roomData.getLineCount()) - roomData.getHeight();
                    if (scrollAmount > 0)
                        roomData.scrollTo(0, scrollAmount);
                    else
                        roomData.scrollTo(0, 0);

                    if (device.getType().equals("LIGHT")) {
//                        data += "Lights: ";

                        if (dataRead.split(":")[0].equals("0")) {
//                            data += "OFF";
                            setControlState(false, "LIGHT");
                        } else {
//                            data += "ON";
                            setControlState(true, "LIGHT");
                        }
                    } else if (device.getType().equals("SMOKE")) {

                        if (dataRead.equals("0:0")) {
                            data += "Smoke detector: OFF";
                            setControlState(false, "SMOKE");
                        } else if (dataRead.split(":")[0].equals("1")) {
                            data += "Smoke detector: ON";
                            setControlState(true, "SMOKE");
                        } else if (dataRead.split(":")[1].equals("1")) {
                            data += "Smoke Alarm On!";
                            smokeDetected = true;
                            setControlState(true, "SMOKE");
                        }
                    } else if (device.getType().equals("RFID")) {

                        if (dataRead.equals("0:0")) {
                            data += "RFID: OFF (Locked)";
                            setControlState(false, "RFID");
                        } else if (dataRead.split(":")[0].equals("1") && dataRead.split(":")[1].equals("0")) {
                            data += "RFID: ON";
                            setControlState(true, "RFID");
                        } else if (dataRead.split(":")[0].equals("1") && !dataRead.split(":")[1].equals("0")) {
                            data += "RFID scanned: " + dataRead.split(":")[1];
                        }
                    } else if (device.getType().equals("PIR")) {

                        if (dataRead.equals("0:0")) {
                            data += "Motion Detector: OFF";
                            setControlState(false, "PIR");
                        } else if (dataRead.equals("1:0")) {
                            data += "Motion Detector: ON";
                            setControlState(true, "PIR");
                        } else if (dataRead.equals("1:1")) {
                            data += "Motion detected in: " + title + "!";
                        }
                    } else if (device.getType().equals("TEMP") || device.getType().equals("HUMID")) {
                        String[] tempArr = dataRead.split(":");
                        String temp = device.getType().equals("TEMP") ? "Temperature: " : "Humidity: ";
                        for (String string : tempArr) {
                            temp += string;
                        }

                        data += temp;
                    }

                    data += " - " + getCurrentTime() + "\n";

                    if(dataList.size() == 0){
                        dataList.add(data);
                    }

                    for(int i = 0; i < dataList.size(); i++){

                        if(dataList.get(i).equals(data)){
                            dataList.remove(i);
                        }
                    }

                    if(!device.getType().equals("LIGHT")) {
                        dataList.add(data);

                        data = "";

                        for (String line : dataList) {
                            data += line;
                        }
                        roomData.setText(data);

                        rS.saveRoomData(thisRoom.getTitle(), dataList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private Drawable getDrawable(String type){

        Context context= getContext();


        if(context != null){
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
                case "COLOUR":
                    return ContextCompat.getDrawable(getContext(), R.drawable.colour_button);
                default:
                    return null;
            }
        }

        return null;
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

    public String colourIntensity(String currentHex, int intensity){

        int currentIntensity = getIntensity(currentHex);
        int delta;

        if(intensity > currentIntensity) {
            delta = intensity - currentIntensity;

            for(int i = 0; i < delta; i++)
                currentHex = colourIntensityWorker(currentHex, true);
        }
        else {
            delta = currentIntensity - intensity;
            for(int i = 0; i < delta; i++)
                currentHex = colourIntensityWorker(currentHex, false);
        }

        return currentHex;
    }

    public int[] hexToIntArray(String hex){

        int[] result = new int[6];

        for(int i = 0; i < hex.toCharArray().length; i++){

            if(Character.isDigit(hex.charAt(i))){
                result[i] = Character.getNumericValue(hex.charAt(i));
            }
            else{
                switch(hex.charAt(i)){
                    case 'A':
                        result[i] = 10;
                        break;
                    case 'B':
                        result[i] = 11;
                        break;
                    case 'C':
                        result[i] = 12;
                        break;
                    case 'D':
                        result[i] = 13;
                        break;
                    case 'E':
                        result[i] = 14;
                        break;
                    case 'F':
                        result[i] = 15;
                }
            }
        }

        return result;
    }

    public String colourIntensityWorker(String currentHex, boolean increase){

        currentHex = currentHex.toUpperCase();
        String result = "";

        if(currentHex.length() != 6)
            return "888888";

        int[] convertedHex = hexToIntArray(currentHex);

        if(increase){
            for(int i = 1; i < convertedHex.length; i += 2){

                if(convertedHex[i] < 15){
                    convertedHex[i]++;
                }
                else{
                    if(convertedHex[i-1] == 15){
                        break;
                    }
                    convertedHex[i-1]++;
                    convertedHex[i] = 0;
                }
            }
        }
        else{
            for(int i = 1; i < convertedHex.length; i += 2){

                if(convertedHex[i] > 0){
                    convertedHex[i]--;
                }
                else{
                    if(convertedHex[i-1] == 0){
                        break;
                    }
                    convertedHex[i-1]--;
                    convertedHex[i] = 15;
                }
            }
        }

        for(int i = 0; i < convertedHex.length; i++){
            result += hexValues[convertedHex[i]];
        }

        return result;
    }

    public int getIntensity(String hex){

        int[] convertedHex = hexToIntArray(hex);

        int R = (convertedHex[0] * 16) + convertedHex[1];
        int G = (convertedHex[2] * 16) + convertedHex[3];
        int B = (convertedHex[4] * 16) + convertedHex[5];

        if(R > G && R > B)
            return R;
        else if(G > R && G > B)
            return G;
        else
            return B;
    }

}