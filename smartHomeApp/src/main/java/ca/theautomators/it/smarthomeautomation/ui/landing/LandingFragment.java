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

public class LandingFragment extends Fragment {

    private String kitchenData, bedroomData, livingRoomData;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Temp dummy data***********************************************************
        kitchenData = "\n\n\nSmoke: Negative\nLights: ON";
        bedroomData = "\n\n\nTemp: 21 degrees C\nHumidity: 30%\nLights: ON";
        livingRoomData = "\n\n\nMotion: Negative\nDoor: Closed\nRFID: Active\nLights: ON";
        //**************************************************************************

        root = inflater.inflate(R.layout.fragment_landing, container, false);

        Button kitchenButton = (Button) root.findViewById(R.id.button_kitchen);
        setButtonData(kitchenButton, kitchenData);

        Button bedroomButton = (Button) root.findViewById(R.id.button_bedroom);
        setButtonData(bedroomButton, bedroomData);

        Button livingRoomButton = (Button) root.findViewById(R.id.button_living_room);
        setButtonData(livingRoomButton, livingRoomData);

        Button settingsButton = (Button) root.findViewById(R.id.button_settings);

        kitchenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentControl(1);
            }
        });

        bedroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentControl(2);
            }
        });

        livingRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentControl(3);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).fragmentControl(4);
            }
        });


        return root;
    }

    private void setButtonData(Button button, String data){

        CharSequence buttonTitle = button.getText();
        int start = buttonTitle.length();
        int end = start + data.length();

        SpannableStringBuilder span = new SpannableStringBuilder(buttonTitle);
        span.insert(buttonTitle.length(), data);
        span.setSpan(new RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(span);
    }
}