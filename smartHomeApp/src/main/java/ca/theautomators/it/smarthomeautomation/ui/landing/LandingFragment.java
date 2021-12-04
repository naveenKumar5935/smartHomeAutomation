/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation.ui.landing;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.Device;
import ca.theautomators.it.smarthomeautomation.FirebaseConnect;
import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.Room;
import ca.theautomators.it.smarthomeautomation.RoomState;

public class LandingFragment extends Fragment{

    private View root;
    private ArrayList<Room> rooms;
    private ArrayList<Button> buttons;
    private LinearLayout buttonList;
    private FirebaseConnect fC;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_landing, container, false);

        RoomState rS = RoomState.getInstance(null);
        fC = FirebaseConnect.getInstance();
        buttons = new ArrayList<>();
        buttonList = root.findViewById(R.id.landing_button_list);

        rooms = rS.loadBuiltRooms();

        if(!(rooms.isEmpty())){

            buildLayout();

            if(fC.getFirebaseConnectivity()){
                setButtonSensorReceivers();
            }
        }


        ArrayList<MenuItem> menuItems = rS.getMenuItems();

        for(int i = 0; i  < buttons.size(); i++){
            setButtonClickListener(buttons.get(i), menuItems);
        }



        return root;
    }

    private void setButtonClickListener(Button button, ArrayList<MenuItem> menuItems){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < menuItems.size(); i++){
                    if(button.getText().toString().split("\n")[0].equals(menuItems.get(i).getTitle())){
                        ((MainActivity)getActivity()).fragmentSwitch(menuItems.get(i).getItemId());
                    }
                }
            }
        });
    }

    private void setButtonSensorReceivers(){

        for(int i = 0; i < rooms.size(); i++){

            ArrayList<Device> devices = new ArrayList<>();

            if(rooms.get(i).getDeviceIdentifierList().size() > 0){

                for(String device : rooms.get(i).getDeviceIdentifierList()){

                    devices.add(new Device(device));
                }

                ArrayList<String> buttonData = new ArrayList<>();

                for(Device device : devices){

                    int finalI = i;
                    device.getSensorData().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String dataRead = snapshot.getValue(String.class);
                            String data = "";

                            switch(device.getType()){

                                case "LIGHT":
                                    data += "Lights: ";
                                    data += dataRead.split(":")[0].equals("0") ? "OFF" : "ON";
                                    rooms.get(finalI).setData("LIGHT", data);
                                    setButtonData(buttons.get(finalI), rooms.get(finalI));
                                    break;

                                case "SMOKE":
                                    if(dataRead.equals("0:0"))
                                        data += "Smoke Detector: OFF";
                                    else if (dataRead.split(":")[0].equals("1"))
                                        data += "Smoke Detector: ON";
                                    else if (dataRead.split(":")[1].equals("1"))
                                        data += "Smoke Alarm On!";
                                    rooms.get(finalI).setData("SMOKE", data);
                                    setButtonData(buttons.get(finalI), rooms.get(finalI));
                                    break;

                                case "RFID":
                                    if(dataRead.equals("0:0"))
                                        data += "RFID: OFF (Locked)";
                                    else if(dataRead.split(":")[0].equals("1") && dataRead.split(":")[1].equals("0"))
                                        data += "RFID: ON";
                                    else if(dataRead.split(":")[0].equals("1") && !dataRead.split(":")[1].equals("0"))
                                        data += "RFID scanned: " + dataRead.split(":")[1];
                                    rooms.get(finalI).setData("RFID", data);
                                    setButtonData(buttons.get(finalI), rooms.get(finalI));
                                    break;

                                case "PIR":
                                    if(dataRead.equals("0:0"))
                                        data += "Motion Detector: OFF";
                                    else if(dataRead.equals("1:0"))
                                        data += "Motion Detector: ON";
                                    else if(dataRead.equals("1:1"))
                                        data += "Motion Detected!";
                                    rooms.get(finalI).setData("PIR", data);
                                    setButtonData(buttons.get(finalI), rooms.get(finalI));
                                    break;

                                case "TEMP":
                                case "HUMID":
                                    data += device.getType().equals("TEMP") ? "TEMP: " : "HUMID: ";
                                    data += dataRead.replace(":", "");
                                    rooms.get(finalI).setData(device.getType().equals("TEMP") ? "TEMP" : "HUMID", data);
                                    setButtonData(buttons.get(finalI), rooms.get(finalI));
                                    break;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        }
    }

    private void setButtonData(Button button, Room room){

        CharSequence buttonTitle = room.getTitle();

        int start = buttonTitle.length();
        int end = start + room.getData().length() + 2;

        SpannableStringBuilder span = new SpannableStringBuilder(buttonTitle);
        span.insert(buttonTitle.length(), "\n\n" + room.getData());
        span.setSpan(new RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(span);

    }

    private void buildLayout(){

        int numRows;

        if(rooms.size() % 2 == 0){

            numRows = (rooms.size() / 2) + 1;

            addRows(buttonList, numRows);

            LinearLayout row = newRow();
            row.addView(addSettingsButton());
            buttonList.addView(row);

        }
        else{

            numRows = (rooms.size() + 1) / 2;

            addRowWithSettingsInline(buttonList, numRows);

        }


    }

    private void addRows(LinearLayout buttonList, int numRows){

        int counter = 0;

        for(int i = 0; i < (numRows - 1); i++){

            LinearLayout row = newRow();

            for(int j = 0; j < 2; j++){

                row.addView(addRoomButton(rooms.get(counter)));
                counter++;
            }

            buttonList.addView(row);
        }



    }

    private void addRowWithSettingsInline(LinearLayout buttonList, int numRows){

        int counter = 0;

        for(int i = 0; i < (numRows - 1); i++){

            LinearLayout row = newRow();

            for(int j = 0; j < 2; j++){

                row.addView(addRoomButton(rooms.get(counter)));
                counter++;
            }

            buttonList.addView(row);
        }

        LinearLayout row = newRow();
        row.addView(addRoomButton(rooms.get(rooms.size() - 1)));
        row.addView(addSettingsButton());
        buttonList.addView(row);

    }

    private Button addRoomButton(Room room){


        int dimen = convertToDP(150, root.getContext());
        int margin = convertToDP(10, root.getContext());

        Button button = new Button(getContext());
        button.setText(room.getTitle());
        button.setBackground(getResources().getDrawable(R.drawable.ripple));
        button.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimen, dimen);
        params.weight = 1;
        params.setMargins(margin, margin, margin, margin);
        button.setLayoutParams(params);



        buttons.add(button);

        return button;
    }

    private Button addSettingsButton(){

        int dimen = convertToDP(150, root.getContext());
        int margin = convertToDP(10, root.getContext());

        Button settingsButton = new Button(getContext());
        settingsButton.setText(getString(R.string.settings));
        settingsButton.setBackground(getResources().getDrawable(R.drawable.ripple));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimen, dimen);
        params.weight = 1;
        params.setMargins(margin, margin, margin, margin);
        settingsButton.setLayoutParams(params);
        settingsButton.setTextColor(getResources().getColor(R.color.white));

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_settings);
            }
        });

        return settingsButton;
    }

    private LinearLayout newRow(){

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        row.setLayoutParams(params);

        return row;
    }

    public static int convertToDP(int dp, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * scale);
    }

}










