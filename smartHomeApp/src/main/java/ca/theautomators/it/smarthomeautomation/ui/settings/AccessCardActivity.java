package ca.theautomators.it.smarthomeautomation.ui.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;

public class AccessCardActivity extends AppCompatActivity {

    EditText accessET;
    Button accessBtn, scannerAccessBtn;
    ListView accessList;
    int countcards=0;
    ArrayAdapter adapter;
    ArrayList<String> arrayList;
    ArrayList<String> keylist;
    FirebaseUser currentFirebaseUser;
    String matchrfid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.access_card_title));

        accessET = findViewById(R.id.accessCard_tb);
        accessBtn = findViewById(R.id.accessBtn);
        accessList = findViewById(R.id.access_listview);
        scannerAccessBtn = findViewById(R.id.accessScannerBtn);
       currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


        arrayList = new ArrayList<>();
        keylist = new ArrayList<>();
        adapter = new ArrayAdapter(AccessCardActivity.this, android.R.layout.simple_list_item_1,arrayList);
        accessList.setAdapter(adapter);

        gettingData();

        accessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gettingData();
                AlertDialog.Builder alert = new AlertDialog.Builder(AccessCardActivity.this);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.setTitle(R.string.alert_title);
                alert.setMessage(R.string.do_you_want_to_delete);
                alert.setPositiveButton(R.string.alert_positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("AccessCards").child(keylist.get(i)).removeValue();
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



        scannerAccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showProgressDialog(true);
                ProgressDialog progressDialog = new ProgressDialog(AccessCardActivity.this);
                progressDialog.setMessage("Searching for RFID card....");
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.dismiss();
                    }
                });
                progressDialog.show();

                FirebaseDatabase.getInstance().getReference().child("Devices").child("100").child("DATA").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rfidValue = snapshot.getValue().toString().split(":")[1];
                            Log.e("rfidchange",rfidValue);
                        if(!rfidValue.matches("00000")){

                            rfidAlertVerify(rfidValue);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(accessET.getText().toString().trim()!=null){

                    if(arrayList.contains(accessET.getText().toString())){
                        Toast.makeText(getApplicationContext(), R.string.access_card_exists,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("AccessCards").child(accessET.getText().toString().trim()).setValue(accessET.getText().toString().trim());
                   gettingData();
                    adapter.notifyDataSetChanged();
                }
                    accessET.setText("");
            }
        });
    }

    public void showProgressDialog(boolean x){


    }

    public void rfidAlertVerify(String rfidValue){

        if(matchrfid.matches(rfidValue)){
            return;
        }
        matchrfid=rfidValue;
        AlertDialog.Builder alert = new AlertDialog.Builder(AccessCardActivity.this);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle("Rfid Found");
        alert.setMessage(rfidValue);
        alert.setPositiveButton("Correct", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("AccessCards").child(rfidValue).setValue(rfidValue);
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

    }

    public void gettingData(){
        adapter.clear();
        keylist.clear();
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("AccessCards").addListenerForSingleValueEvent(new ValueEventListener() {
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