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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RoomManagerActivity extends AppCompatActivity {


    private ArrayList<String> roomNames;
    private ArrayList<Drawable> roomIcons;
    private ArrayList<Integer> roomIds;
    private int[] drawableId;
    private Drawable[] editedRoomIcons;
    EditText[] editedRoomNames;
    private RoomState roomState;
    private int numRows;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_manager);

        roomState = RoomState.getInstance(null);

        roomNames = roomState.getRoomNames();
        roomIcons = roomState.getRoomIcons();
        roomIds = roomState.getRoomIds();

        drawableId = roomState.getDrawableId();
        numRows = roomState.getNumRooms();

        editedRoomNames = new EditText[numRows];
        editedRoomIcons = new Drawable[numRows];

        linearLayout = findViewById(R.id.roommanager);

        linearLayout = buildLayout();

        display();

        Button save = findViewById(R.id.savebutton);
        ImageButton add = findViewById(R.id.addButton);
        ImageButton delete = findViewById(R.id.deleteButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < roomState.getNumRooms(); i++){

                    if(editedRoomNames[i].getText().toString().compareTo("") != 0)
                        roomState.changeRoomName(editedRoomNames[i].getText().toString(), i);

                    if(editedRoomIcons[i] != null)
                        roomState.changeRoomIcons(editedRoomIcons[i], i);

                    //TODO modify room set accordingly

                }

                Intent intent = new Intent(RoomManagerActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numRows < 10){
                    linearLayout.addView(addRow(null));
                    numRows++;
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numRows--;
                linearLayout = buildLayout();
                display();
            }
        });

    }

    private LinearLayout buildLayout(){

        if(linearLayout != null)
            linearLayout.removeAllViews();

        TextView text = new TextView(this);
        text.setText(R.string.edit_room_name_and_type);
        text.setTextSize(20);
        linearLayout.addView(text);

        return linearLayout;
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

    private void display(){

        for(int i = 0; i < numRows; i++) {
            //add row to layout
            if(i < roomState.getNumRooms()){
                linearLayout.addView(addRow(i));
            }
            else{

                linearLayout.addView((addRow(null)));
            }
        }
    }

    private LinearLayout addRow(@Nullable Integer index){

        //Row layout setup
        LinearLayout entryRow = new LinearLayout(this);
        entryRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        //Text entry setup
        EditText editText = new EditText(this);
        if(index == null){
            editText.setHint("Enter room name");
        }
        else{
            editText.setHint(roomNames.get(index));
        }
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Integer resource = getDrawableId(position);

                if(resource != null) {

                    if(index == null){
                        //TODO add new icons entry to roomstate
                    }
                    else{
                        editedRoomIcons[index] = getResources().getDrawable(resource.intValue());
                        drawableId[index] = resource.intValue();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //add text entry and spinner to row
        entryRow.addView(editText);
        entryRow.addView(spinner);

        //save changes
        if(index != null){
            editedRoomNames[index] = editText;
        }

        return entryRow;
    }

    public void deleteRow(){


    }


}