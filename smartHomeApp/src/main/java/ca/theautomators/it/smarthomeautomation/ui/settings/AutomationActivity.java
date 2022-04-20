package ca.theautomators.it.smarthomeautomation.ui.settings;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.theautomators.it.smarthomeautomation.FirebaseConnect;
import ca.theautomators.it.smarthomeautomation.R;
import io.paperdb.Paper;

public class AutomationActivity extends AppCompatActivity {
    Switch motionSwitch, rfidSwitch, rfidNotify, motionNotify;
    ImageView alarmIndicate;
    Button alarmReset;
    DatabaseReference smokeAlarmDataReference;
    String currentFirebaseUser;
    FirebaseConnect fC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fC = FirebaseConnect.getInstance(null);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        motionSwitch = findViewById(R.id.motionSwitch);
        rfidSwitch = findViewById(R.id.rfidSwitch);
        motionNotify = findViewById(R.id.motionNotificationSwitch);
        rfidNotify = findViewById(R.id.rfidNotificationSwitch);
        alarmIndicate = findViewById(R.id.alarmIndicate);
        alarmReset = findViewById(R.id.alarmResetBtn);
        currentFirebaseUser = FirebaseAuth.getInstance().getUid();

        smokeAlarmDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser).child("devices").child("103").child("DATA");




        FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser).child("devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(AutomationActivity.this, "Please Check your hardware", Toast.LENGTH_LONG).show();
                    return;
                }
                if(snapshot.hasChildren()) {
                    String rfidState = snapshot.child("100").child("DATA").getValue().toString().split(":")[0];
                    String motionState = snapshot.child("101").child("DATA").getValue().toString().split(":")[0];

                    if (rfidState.matches("0")) {
                        rfidSwitch.setChecked(false);
                    } else {
                        rfidSwitch.setChecked(true);
                    }

                    if (motionState.matches("0")) {
                        motionSwitch.setChecked(false);
                    } else {
                        motionSwitch.setChecked(true);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(Paper.book().read("rfidNotify","").equals("true")){
            rfidNotify.setChecked(true);
        }else {
            rfidNotify.setChecked(false);
        }

        if(Paper.book().read("motionNotify","").equals("true")){
            motionNotify.setChecked(true);
        }else {
            motionNotify.setChecked(false);
        }

        smokeAlarmDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(AutomationActivity.this, "Please Check your hardware", Toast.LENGTH_LONG).show();
                    return;
                }
                String smokeStatus = snapshot.getValue().toString().split(":")[0];

                if(smokeStatus.matches("1")){
                    alarmIndicate.setBackgroundResource(R.drawable.greenlight);
                }else {
                    alarmIndicate.setBackgroundResource(R.drawable.redlight);
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

                FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser).child("devices").child("100").child("DATA").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AutomationActivity.this, "Rfid is OFF", Toast.LENGTH_SHORT).show();
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

                FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser).child("devices").child("101").child("DATA").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        
                    }
                });
            }
        });

        rfidNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Paper.book().write("rfidNotify","true");
                }else {
                    Paper.book().write("rfidNotify","false");
                }
            }
        });

        motionNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Paper.book().write("motionNotify","true");
                }else {
                    Paper.book().write("motionNotify","false");
                }
            }
        });

        if(fC.getFirebaseConnectivity()){
            alarmReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smokeAlarmDataReference.setValue("0:0").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressDialog progressDialog = new ProgressDialog(AutomationActivity.this);
                            progressDialog.setMessage("Resetting the alarm..");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            Runnable progressRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    progressDialog.cancel();

                                    //   smokeAlarmDataReference.setValue("1:0");

                                }
                            };

                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 5000);

                        }
                    });
                }
            });
        }

    }
}