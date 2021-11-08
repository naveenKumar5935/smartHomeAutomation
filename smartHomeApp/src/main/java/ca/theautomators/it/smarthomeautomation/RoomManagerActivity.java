package ca.theautomators.it.smarthomeautomation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RoomManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_manager);

        RoomState roomState = RoomState.getInstance(null);

        ArrayList<String> roomNames = roomState.getRoomNames();
        int[] drawableId = roomState.getDrawableId();

        EditText[] editedRoomNames = new EditText[roomState.getNumRooms()];
        Drawable[] editedRoomIcons = new Drawable[roomState.getNumRooms()];

        LinearLayout linearLayout = findViewById(R.id.roommanager);

        TextView text = new TextView(this);
        text.setText(R.string.edit_room_name_and_type);
        text.setTextSize(20);
        linearLayout.addView(text);

        for(int i = 0; i < roomState.getNumRooms(); i++){

            //Row layout setup
            LinearLayout entryRow = new LinearLayout(this);
            entryRow.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;

            //Text entry setup
            EditText editText = new EditText(this);
            editText.setHint(roomNames.get(i));
            editText.setPadding(20, 20, 20, 20);
            editText.setLayoutParams(params);

            //drop down menu setup
            Spinner spinner = new Spinner(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.rooms, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setPadding(20, 20, 20, 20);
            spinner.setLayoutParams(params);

            int finalI = i;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Integer resource = getDrawableId(position);

                    if(resource != null) {
                        editedRoomIcons[finalI] = getResources().getDrawable(resource.intValue());
                        drawableId[finalI] = resource.intValue();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //add text entry and spinner to row
            entryRow.addView(editText);
            entryRow.addView(spinner);

            //add row to layout
            linearLayout.addView(entryRow);

            //save changes
            editedRoomNames[i] = editText;

        }

        Button save = findViewById(R.id.savebutton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < roomState.getNumRooms(); i++){

                    if(editedRoomNames[i].getText().toString().compareTo("") != 0)
                        roomState.changeRoomName(editedRoomNames[i].getText().toString(), i);

                    if(editedRoomIcons[i] != null)
                        roomState.changeRoomIcons(editedRoomIcons[i], i);

                }

                Intent intent = new Intent(RoomManagerActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private Integer getDrawableId(int selection){

        Integer resource;

        switch(selection){

            case 1:
                resource = R.drawable.bedroom;
                break;
            case 2:
                resource = R.drawable.bathroom;
                break;
            case 3:
                resource = R.drawable.child_room;
                break;
            case 4:
                resource = R.drawable.kitchen;
                break;
            case 5:
                resource = R.drawable.living_room;
                break;
            case 6:
                resource = R.drawable.garage;
                break;
            case 7:
                resource = R.drawable.office;
                break;
            case 8:
                resource = R.drawable.game_room;
                break;
            case 9:
                resource = R.drawable.backyard;
                break;
            case 10:
                resource = R.drawable.balcony;
                break;
            case 11:
                resource = R.drawable.music_room;
                break;
            default:
                resource = -1;

        }

        if(resource.intValue() != -1) {
            return resource;
        }
        else
            return null;
    }


    //TODO add functionality
    private void createRoom(){

    }

    private void deleteRoom(){

    }

}