/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Device {

    private final String type;
    private final String identifier;
    private String data;

    FirebaseConnect fC;

    //This class uses a dependency injection, which is a Creational design pattern
    public Device(String identifier){

        fC = FirebaseConnect.getInstance();

        data = "";
        this.identifier = identifier;
        type = fC.getDeviceType(identifier);
    }

    private String getSensorData(){

        fC.getSensorDataRef(identifier).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long dataRead = snapshot.getValue(long.class);
                data = String.valueOf(dataRead);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return data;
    }

    public void sendControlData(String controlData){

        fC.sendControlData(controlData, identifier);
    }

    public String getType(){

        return type;
    }

    public String getIdentifier(){

        return identifier;
    }

    public String getData(){

        if(data.isEmpty()){

            return "No Data Available";
        }

        return data;
    }


}
