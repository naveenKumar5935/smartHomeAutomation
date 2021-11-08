package ca.theautomators.it.smarthomeautomation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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