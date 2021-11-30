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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.FirebaseConnect;
import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.Room;
import ca.theautomators.it.smarthomeautomation.RoomState;

public class LandingFragment extends Fragment {

    private View root;
    private ArrayList<Room> rooms;
    private ArrayList<AppCompatButton> buttons;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_landing, container, false);

        RoomState rS = RoomState.getInstance(null);
        FirebaseConnect fC = FirebaseConnect.getInstance();

        rooms = rS.loadBuiltRooms();

        if(!(rooms.isEmpty())){
//            buildLayout();
        }



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

    private void buildLayout(){

        LinearLayout buttonList = root.findViewById(R.id.button_list);
        int numRows;

        if(rooms.size() % 2 == 0){

            numRows = (rooms.size() / 2) + 1;

            addRow(buttonList, numRows);

        }
        else{

            numRows = (rooms.size() + 1) / 2;

            addRowWithSettings(buttonList, numRows);

        }


    }

        private LinearLayout addRow(LinearLayout buttonList, int numRows){

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        int counter = 0;

        for(int i = 0; i < (numRows - 1); i++){

            for(int j = 0; j < 2; j++){

                row.addView(addRoomButton(rooms.get(counter)));
                counter++;
            }

            buttonList.addView(row);
        }

        row.addView(addSettingsButton());
        buttonList.addView(row);

        return null;
    }

    private LinearLayout addRowWithSettings(LinearLayout buttonList, int numRows){

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        return null;
    }

    private AppCompatButton addRoomButton(Room room){

        return null;
    }

    private AppCompatButton addSettingsButton(){

        return null;
    }


}










