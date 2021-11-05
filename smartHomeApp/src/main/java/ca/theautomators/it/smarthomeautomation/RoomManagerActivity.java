package ca.theautomators.it.smarthomeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RoomManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_manager);

        RoomState roomState = RoomState.getInstance();

        //TODO Add option for user to change room icon

        ArrayList<String> roomNames = roomState.getRoomNames();
        ArrayList<Integer> roomIds = roomState.getRoomIds();
        EditText[] editedRoomNames = new EditText[roomState.getNumRooms()];

        LinearLayout linearLayout = findViewById(R.id.roommanager);

        TextView text = new TextView(this);
        text.setText("Please edit room names below:\n\n");
        text.setTextSize(20);
        linearLayout.addView(text);

        for(int i = 0; i < roomState.getNumRooms(); i++){

            EditText editText = new EditText(this);
            editText.setHint(roomNames.get(i));
            editText.setPadding(20, 20, 20, 20);
            linearLayout.addView(editText);

            editedRoomNames[i] = editText;

        }

        Button button = findViewById(R.id.savebutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < roomState.getNumRooms(); i++){

                    if(editedRoomNames[i].getText().toString().compareTo("") != 0)
                        roomState.changeRoomName(editedRoomNames[i].getText().toString(), i);

                }

                Intent intent = new Intent(RoomManagerActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });








    }
}