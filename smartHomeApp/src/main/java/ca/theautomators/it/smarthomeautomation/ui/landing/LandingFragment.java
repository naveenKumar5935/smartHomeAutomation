/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation.ui.landing;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.RoomState;

public class LandingFragment extends Fragment {

    private String kitchenData, bedroomData, livingRoomData;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Temp dummy data***********************************************************
        kitchenData = getString(R.string.kitchen_data);
        bedroomData = getString(R.string.bedroom_data);
        livingRoomData = getString(R.string.living_room_data);
        //**************************************************************************

        root = inflater.inflate(R.layout.fragment_landing, container, false);

        RoomState roomState = RoomState.getInstance(null);

        Button kitchenButton = (Button) root.findViewById(R.id.button_kitchen);
        //TODO temporary set text, will be removed once add and remove room functionality developed
        kitchenButton.setText(roomState.getRoomNames().get(1));
        setButtonData(kitchenButton, kitchenData);

        Button bedroomButton = (Button) root.findViewById(R.id.button_bedroom);
        //TODO temporary set text, will be removed once add and remove room functionality developed
        bedroomButton.setText(roomState.getRoomNames().get(0));
        setButtonData(bedroomButton, bedroomData);

        Button livingRoomButton = (Button) root.findViewById(R.id.button_living_room);
        //TODO temporary set text, will be removed once add and remove room functionality developed
        livingRoomButton.setText(roomState.getRoomNames().get(2));
        setButtonData(livingRoomButton, livingRoomData);

        Button settingsButton = (Button) root.findViewById(R.id.button_settings);

        kitchenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_kitchen);
            }
        });

        bedroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_bedroom);
            }
        });

        livingRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_livingroom);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_settings);
            }
        });


        return root;
    }

    private void setButtonData(Button button, String data){

        CharSequence buttonTitle = button.getText();
        int start = buttonTitle.length();
        int end = start + data.length() + 2;

        SpannableStringBuilder span = new SpannableStringBuilder(buttonTitle);
        span.insert(buttonTitle.length(), "\n\n" + data);
        span.setSpan(new RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(span);
    }
}