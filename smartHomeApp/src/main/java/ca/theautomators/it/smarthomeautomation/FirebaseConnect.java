/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseConnect {

    private DatabaseReference deviceRef;
    private int numDevices;
    private String[] identifiers;
    private String[] deviceTypes;
    private boolean firebaseConnectivity;
    private String currentUser;

    private static FirebaseConnect INSTANCE= null;

    //This class uses a Singleton style Creational design pattern
    private FirebaseConnect(String currentUser){

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if(currentUser != null)
            this.currentUser = currentUser;
        else
            System.out.println("*****************NO USER*********************");

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseConnectivity = snapshot.getValue(Boolean.class);
                if(firebaseConnectivity){
                    Log.d(TAG, "connected");

                    start();

                }
                else{
                    Log.d(TAG, "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "Listener was cancelled");

            }
        });
    }

    private void start(){

        deviceRef = FirebaseDatabase.getInstance().getReference().child("/Users/" + currentUser + "/devices");
        loadNumDevices();
    }


    public static FirebaseConnect getInstance(String currentUser){

        System.out.println("**********Current User**********: " + currentUser);

        if(INSTANCE == null){

            synchronized (FirebaseConnect.class){

                INSTANCE = new FirebaseConnect(currentUser);
            }
        }

        return(INSTANCE);
    }

    public boolean getFirebaseConnectivity(){

        return firebaseConnectivity;
    }


    public void setUserFeedback(String name, String email, String phone, String feedback, float rating,String modelNo){

        HashMap<String, Object> userFeedback = new HashMap<>();
        userFeedback.put("name",name);
        userFeedback.put("email", email);
        userFeedback.put("phone", phone);
        userFeedback.put("feedback",feedback);
        userFeedback.put("rating",rating);
        userFeedback.put("Device",modelNo);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        FirebaseDatabase.getInstance().getReference("UserFeedback").child(currentFirebaseUser.getUid()).setValue(userFeedback);

    }


    public void sendControlData(String data, String identifier){

        deviceRef.child(identifier).child("DATA").setValue(data);
        deviceRef.child(identifier).child("DATA").setValue(data);
        deviceRef.child(identifier).child("DATA").setValue(data);
        deviceRef.child(identifier).child("DATA").setValue(data);
        deviceRef.child(identifier).child("DATA").setValue(data);
    }

    public DatabaseReference getSensorDataRef(String identifier){

        if(getFirebaseConnectivity()){
            return deviceRef.child(identifier).child("DATA");
        }
        else{
            return null;
        }
    }

    public DatabaseReference getUserRef(){
        return deviceRef;
    }

    public void linkLightsToMotion(String pirIdentifier, ArrayList<String> lightIdentifierList){

        for(int i = 0; i < lightIdentifierList.size(); i++){

            deviceRef.child(pirIdentifier).child("LIGHTS").child("LIGHT" + i).setValue(lightIdentifierList.get(i));
        }
    }

    public void loadNumDevices(){

        deviceRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete()) {
                    numDevices = (int) task.getResult().getChildrenCount();
                    loadIdentifiers();
                }
                else
                    Log.e("firebase", "Error getting data", task.getException());
            }
        });
    }

    private void loadDeviceTypes(){

        deviceTypes = new String[numDevices];

        for(int i = 0; i < numDevices; i++){

            int finalI = i;
            deviceRef.child(identifiers[i]).child("TYPE").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    deviceTypes[finalI] = task.getResult().getValue().toString();
                }
            });
        }
    }

    public String getDeviceType(String identifier){

        int index = Integer.parseInt(identifier) - 100;

        if(deviceTypes.length > 0){
            return deviceTypes[index];
        }
        else{
            return "NULL";
        }
    }

    public void loadIdentifiers(){

        identifiers = new String[numDevices];

        deviceRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int count = 0;
                for(DataSnapshot snap: task.getResult().getChildren()){
                    identifiers[count] = snap.getKey();
                    count++;
                }
                loadDeviceTypes();
            }
        });
    }

    public String[] getIdentifiers(){

        return identifiers;
    }

    public int getNumDevices(){
        return numDevices;
    }


}
