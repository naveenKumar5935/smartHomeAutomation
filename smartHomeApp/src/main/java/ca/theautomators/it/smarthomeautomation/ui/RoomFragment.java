/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
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
    private final char[] hexValues = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

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


        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_home);
                    return true;
                }

                return false;
            }
        });

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

                if(dev.getType().equals("SERVO") || dev.getType().equals("LIGHT"))
                    deviceControllers.addView(buildController(dev));

                if (FirebaseConnect.getInstance(null).getFirebaseConnectivity()) {
                    buildSensorReceiver(dev);
                }


            }
        }

        if(pirIdentifier != null)
            fC.linkLightsToMotion(pirIdentifier, lightList);

    }

    @SuppressLint("SetTextI18n")
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

        LinearLayout.LayoutParams colourParams = new LinearLayout.LayoutParams(150, 150);
        colourParams.setMargins(50, 0, 30, 0);

        colour.setLayoutParams(colourParams);

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
                        colour.setBackgroundColor(0x000000);
                        icon.setImageDrawable(getDrawable("LIGHT"));
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

        if(device.getType().equals("LIGHT"))
            control.setText("Light " + (Integer.parseInt(device.getIdentifier()) - 105));
        else
            control.setText("Door");

        controls.add(control);


        if(device.getType().equals("LIGHT")){
            //Create slider and colour picker
            LinearLayout vert = new LinearLayout(getContext());
            vert.setOrientation(LinearLayout.VERTICAL);

            String currentHex = currentVal[0].split(":")[1];
            intensity.setProgress(getIntensity(currentHex));
            if(!control.isChecked())
                intensity.setEnabled(false);

            intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    device.setLightIntensity(progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {


                }
            });

            if(!control.isChecked()) {
                colour.setClickable(false);
                colour.setBackgroundColor(0x000000);
            }
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
                                device.setLightIntensity(intensity.getProgress());
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
                    roomData.scrollTo(0, Math.max(scrollAmount, 0));

                    switch (device.getType()) {
                        case "LIGHT":

                            setControlState(!dataRead.split(":")[0].equals("0"), "Light " + (Integer.parseInt(device.getIdentifier()) - 105));

                            break;
                        case "SMOKE":

                            if (dataRead.equals("0:0")) {
                                data += "Smoke detector: OFF";
                            } else if (dataRead.split(":")[0].equals("1") && dataRead.split(":")[1].equals("0")) {
                                data += "Smoke detector: ON";
                            } else if (dataRead.split(":")[1].equals("1")) {
                                data += "Smoke Alarm On!";
                                smokeDetected = true;
                            }
                            break;
                        case "RFID":

                            if (dataRead.equals("0:0")) {
                                data += "RFID: OFF (Locked)";
                            } else if (dataRead.split(":")[0].equals("1") && dataRead.split(":")[1].equals("0")) {
                                data += "RFID: ON";
                            } else if (dataRead.split(":")[0].equals("1") && !dataRead.split(":")[1].equals("00000")) {
                                data += "RFID scanned: " + dataRead.split(":")[1];
                            }
                            break;
                        case "PIR":

                            switch (dataRead) {
                                case "0:0":
                                    data += "Motion Detector: OFF";
                                    break;
                                case "1:0":
                                    data += "Motion Detector: ON";
                                    break;
                                case "1:1":
                                    data += "Motion detected in: " + title;
                                    break;
                            }
                            break;
                        case "SERVO":

                            setControlState(!dataRead.equals("0:0"), "Door");
                            break;
                        case "TEMP":
                        case "HUMID":
                            String[] tempArr = dataRead.split(":");
                            String temp = device.getType().equals("TEMP") ? "Temperature: " : "Humidity: ";
                            for (String string : tempArr) {
                                temp += string;
                            }

                            data += temp;
                            break;
                    }

                    if(!data.isEmpty())
                        data += " - " + getCurrentTime() + "\n";

                    if(dataList.size() == 0 && !data.isEmpty()){
                        dataList.add(data);
                    }

                    for(int i = 0; i < dataList.size(); i++){

                        if(dataList.get(i).equals(data)){
                            dataList.remove(i);
                        }
                    }

                    if(!device.getType().equals("LIGHT") && !data.isEmpty()) {
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

    public int getIntensity(String hex){

        double intensity = 0;

        int[] convertedHex = hexToIntArray(hex);

        int R = (convertedHex[0] * 16) + convertedHex[1];
        int G = (convertedHex[2] * 16) + convertedHex[3];
        int B = (convertedHex[4] * 16) + convertedHex[5];

        if(R > G && R > B)
            intensity = R;
        else if(G > R && G > B)
            intensity = G;
        else
            intensity = B;

        return (int)((intensity / 255) * 100);
    }


}