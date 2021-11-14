/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

import ca.theautomators.it.smarthomeautomation.Device;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.RoomState;

public class RoomFragment extends Fragment {

    Device device;
    String identifier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(container != null){

            container.removeAllViews();
        }

        RoomState roomState = RoomState.getInstance(null);

        View root = inflater.inflate(R.layout.fragment_room, container, false);

        return root;
    }
}