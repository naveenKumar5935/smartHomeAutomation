/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseConnect {

    static FirebaseAuth auth;
    boolean result;
    private Context context;

    private static FirebaseConnect INSTANCE= null;

    private FirebaseConnect(){

        auth=FirebaseAuth.getInstance();
    }

    public void setContext(Context context){
        this.context=context;
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


    }

    /*TODO add getters for devices and device data
    These stub routines are just a starting point, if you think some of these tasks can be
    combined then delete the redundant ones
     */

    public void sendControlData(String data){

    }

    public String getSensorData(String identifier){

        return "Data";
    }

    public int getNumDevices(){

        return 0;
    }

    public String getDeviceType(String identifier){

        return "Type";
    }

    public String[] getIdentifiers(){

        String[] identifiers = new String[5];

        return identifiers;
    }



}
