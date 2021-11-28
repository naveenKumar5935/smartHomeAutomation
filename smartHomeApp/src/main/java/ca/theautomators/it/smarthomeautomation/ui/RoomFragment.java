/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.Device;
import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.Room;
import ca.theautomators.it.smarthomeautomation.RoomState;

public class RoomFragment extends Fragment {

    private Room thisRoom;
    private LinearLayout deviceControllers;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        root = inflater.inflate(R.layout.fragment_room, container, false);

        RoomState rS = RoomState.getInstance(null);

        String title = "";

        //Load room data from arguments
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString("title", "Room");
        }

        ((MainActivity)getActivity()).setToolbarTitle(title);

        String sensorTitle = title + " " + getString(R.string.sensor_data);
        TextView roomTitle = root.findViewById(R.id.room_data_title);
        roomTitle.setText(sensorTitle);

        ArrayList<Room> rooms = rS.loadBuiltRooms();

        for(Room room : rooms){

            if(room.getTitle().equals(title)){
                thisRoom = room;
            }
        }

        deviceControllers = root.findViewById(R.id.controls);

        buildRoom();


        return root;
    }

    private void buildRoom(){

        for(String device : thisRoom.getDeviceIdentifierList()){

            Device dev = new Device(device);

            if(!(dev.getType().equals("HUMID") || dev.getType().equals("TEMP"))){

                deviceControllers.addView(buildController(dev));
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

        TextView text = new TextView(getContext());
        text.setText(device.getType());
        text.setLayoutParams(params);
        text.setPadding(20, 20, 20, 20);

        SwitchCompat control = new SwitchCompat(getContext());
        control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(device.getType().equals("LIGHT")){
                    if(isChecked){
                        device.sendControlData("1:000000");
                    }
                    else{
                        device.sendControlData("0:000000");
                    }
                }
                else{
                    if(isChecked){
                        device.sendControlData("1");
                    }
                    else{
                        device.sendControlData("0");
                    }
                }
            }
        });
        control.setLayoutParams(params);

        row.addView(icon);
        row.addView(text);
        row.addView(control);

        return row;
    }

    private Drawable getDrawable(String type){

        switch (type) {
            case "RFID":
                return ContextCompat.getDrawable(getContext(), R.drawable.rfid_nobg);
            case "PIR":
                return ContextCompat.getDrawable(getContext(), R.drawable.motion_detection_nobg);
            case "SERVO":
                return ContextCompat.getDrawable(getContext(), R.drawable.door_closed_nobg);
            case "SMOKE":
                return ContextCompat.getDrawable(getContext(), R.drawable.smoke);
            case "TEMP":
                return ContextCompat.getDrawable(getContext(), R.drawable.thermostat);
            case "HUMID":
                return ContextCompat.getDrawable(getContext(), R.drawable.humidity);
            case "LIGHT":
                return ContextCompat.getDrawable(getContext(), R.drawable.lights_off_nobg);
            default:
                return null;
        }
    }
}