/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui.bedroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.theautomators.it.smarthomeautomation.R;

public class BedroomFragment extends Fragment {

    private String bedroomData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        View root = inflater.inflate(R.layout.fragment_bedroom, container, false);
        bedroomData = getString(R.string.bedroom_data);

        TextView dataText = (TextView) root.findViewById(R.id.bedroom_data);
        dataText.setText(bedroomData);

        ImageView lightBulb = (ImageView) root.findViewById(R.id.light_icon);

//        lightBulb.


        return root;
    }

}