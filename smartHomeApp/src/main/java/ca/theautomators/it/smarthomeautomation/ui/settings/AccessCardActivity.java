package ca.theautomators.it.smarthomeautomation.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;

public class AccessCardActivity extends AppCompatActivity {

    EditText accessET;
    Button accessBtn;
    ListView accessList;
    int countcards=0;
    ArrayAdapter adapter;
    ArrayList<String> arrayList;
    ArrayList<String> keylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_card);
        accessET = findViewById(R.id.accessCard_tb);
        accessBtn = findViewById(R.id.accessBtn);
        accessList = findViewById(R.id.access_listview);

        arrayList = new ArrayList<>();
        keylist = new ArrayList<>();
        adapter = new ArrayAdapter(AccessCardActivity.this, android.R.layout.simple_list_item_1,arrayList);
        accessList.setAdapter(adapter);

        gettingData();

        accessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AccessCardActivity.this);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setTitle(R.string.alert_title);
                alert.setMessage("You want to delete it");
                alert.setPositiveButton(R.string.alert_positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("AccessCards").child(keylist.get(i)).removeValue();
                        gettingData();
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton(R.string.alert_negative_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }
        });



        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String count = String.valueOf(arrayList.size()+1);
                if(accessET.getText().toString().trim()!=null){

                    if(arrayList.contains(accessET.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Access Card already there",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FirebaseDatabase.getInstance().getReference().child("AccessCards").child(accessET.getText().toString().trim()).setValue(accessET.getText().toString().trim());
                   gettingData();
                    adapter.notifyDataSetChanged();
                }

            }
        });




    }

    public void gettingData(){
        adapter.clear();
        FirebaseDatabase.getInstance().getReference().child("AccessCards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countcards = (int)snapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    keylist.add(dataSnapshot.getKey());
                    arrayList.add(dataSnapshot.getValue().toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}