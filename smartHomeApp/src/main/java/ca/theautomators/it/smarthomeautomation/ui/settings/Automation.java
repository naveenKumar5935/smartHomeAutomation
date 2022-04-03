package ca.theautomators.it.smarthomeautomation.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.theautomators.it.smarthomeautomation.R;

public class Automation extends AppCompatActivity {
    Switch motionSwitch, rfidSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automation);

        motionSwitch = findViewById(R.id.motionSwitch);
        rfidSwitch = findViewById(R.id.rfidSwitch);

        FirebaseDatabase.getInstance().getReference().child("Devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             String rfidState = snapshot.child("100").child("DATA").getValue().toString().split(":")[0];
                String motionState = snapshot.child("101").child("DATA").getValue().toString().split(":")[0];

                if(rfidState.matches("0")){
                    rfidSwitch.setChecked(false);
                }else {
                    rfidSwitch.setChecked(true);
                }

                if(motionState.matches("0")){
                    motionSwitch.setChecked(false);
                }else {
                    motionSwitch.setChecked(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rfidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String value = "";
                if(b){
                    value="1:00000";
                }else {
                    value="0:00000";
                }

                FirebaseDatabase.getInstance().getReference().child("Devices").child("100").child("DATA").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Automation.this, "Rfid is OFF", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        motionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String value = "";
                if(b){
                    value="1:0";
                }else {
                    value="0:0";
                }

                FirebaseDatabase.getInstance().getReference().child("Devices").child("101").child("DATA").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        
                    }
                });
            }
        });



    }
}