/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseConnect {

    private final DatabaseReference deviceRef;

    private static FirebaseConnect INSTANCE= null;

    private FirebaseConnect(){

        deviceRef = FirebaseDatabase.getInstance().getReference().child("Devices");

    }

    public static FirebaseConnect getInstance(){

        if(INSTANCE == null){

            synchronized (FirebaseConnect.class){

                INSTANCE = new FirebaseConnect();
            }
        }

        return(INSTANCE);
    }


    public void setUserFeedback(String name, String email, String phone, String feedback, float rating,String modelNo){

        HashMap<String, Object> userFeedback = new HashMap<>();
        userFeedback.put("name",name);
        userFeedback.put("email", email);
        userFeedback.put("phone", phone);
        userFeedback.put("feedback",feedback);
        userFeedback.put("rating",rating);
        userFeedback.put("Device",modelNo);

        FirebaseDatabase.getInstance().getReference("UserFeedback").child(phone).setValue(userFeedback);

    }


    public void sendControlData(String data, String identifier){

        deviceRef.child(identifier).child("DATA").setValue(data);
    }

    public DatabaseReference getSensorData(String identifier){

        return deviceRef.child(identifier).child("DATA");

    }

    public int getNumDevices(){

        final int[] numDevices = {0};

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               numDevices[0] = (int)snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return numDevices[0];
    }

    public String getDeviceType(String identifier){

        final String[] type = {""};

        deviceRef.child(identifier).child("TYPE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                type[0] = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return type[0];
    }

    public String[] getIdentifiers(){

        int numChildren = getNumDevices();

        String[] identifiers = new String[numChildren];

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int count = 0;

                for(DataSnapshot snap : snapshot.getChildren()){

                    if(count < numChildren){

                        identifiers[count] = snap.getKey();
                        count++;

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return identifiers;
    }



}
