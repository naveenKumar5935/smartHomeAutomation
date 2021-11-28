package ca.theautomators.it.smarthomeautomation.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import ca.theautomators.it.smarthomeautomation.R;

public class AccessCardActivity extends AppCompatActivity {

    EditText accessET;
    Button accessBtn;
    ListView accessList;
    int countcards=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_card);
        accessET = findViewById(R.id.accessCard_tb);
        accessBtn = findViewById(R.id.accessBtn);
        accessList = findViewById(R.id.access_listview);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(AccessCardActivity.this, android.R.layout.simple_list_item_1,arrayList);
        accessList.setAdapter(adapter);

        accessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                String v = arrayList.get(i);
                FirebaseDatabase.getInstance().getReference(v).removeValue();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("AccessCards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               countcards = (int)snapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    arrayList.add(dataSnapshot.getValue().toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String count = String.valueOf(countcards+1);
                if(accessET.getText().toString().trim()!=null){
                    FirebaseDatabase.getInstance().getReference().child("AccessCards").child(count).setValue(accessET.getText().toString().trim());
                    adapter.notifyDataSetChanged();
                }

            }
        });




    }
}