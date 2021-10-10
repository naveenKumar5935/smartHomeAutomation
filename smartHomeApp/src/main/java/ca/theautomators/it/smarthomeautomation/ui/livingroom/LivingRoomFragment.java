/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui.livingroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.theautomators.it.smarthomeautomation.R;

public class LivingRoomFragment extends Fragment {

    private String livingRoomData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        View root = inflater.inflate(R.layout.fragment_livingroom, container, false);
        livingRoomData = livingRoomData = "Motion: Negative\nDoor: Closed\nRFID: Active\nLights: ON";

        TextView dataText = root.findViewById(R.id.living_room_data);
        dataText.setText(livingRoomData);
        return root;
    }
}