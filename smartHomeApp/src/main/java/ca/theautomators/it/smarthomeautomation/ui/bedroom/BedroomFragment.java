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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import ca.theautomators.it.smarthomeautomation.R;

public class BedroomFragment extends Fragment {

    private String bedroomData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bedroom, container, false);
        bedroomData = "Temp: 21 degrees C\nHumidity: 30%\nLights: ON";

        TextView dataText = (TextView) root.findViewById(R.id.bedroom_data);
        dataText.setText(bedroomData);
        return root;
    }
}