/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.SplittableRandom;

public class FirebaseConnect {

    FirebaseAuth auth;
    boolean result;

    private static FirebaseConnect INSTANCE= null;

    private FirebaseConnect(){
        auth=FirebaseAuth.getInstance();
    }

    public static FirebaseConnect getInstance(){

        if(INSTANCE == null){

            synchronized (FirebaseConnect.class){

                INSTANCE = new FirebaseConnect();
            }
        }

        return(INSTANCE);
    }

    public User getUserInfo(String email, String password){

        //TODO add get username and get password from database functionality

        User user = new User("email retrieved from firebase", "password retrieved from database");

        return user;
    }

    public void registerLoginStatus(boolean r){

        result = r;


    }

    public void setUserData(String email, String password){



        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(email,password);
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                registerLoginStatus(true);
                                            }else {
                                                registerLoginStatus(false);
                                            }
                                        }
                                    });
                        }else {
                          registerLoginStatus(false);
                        }
                    }
                });

    }

    //TODO add getters for devices and device data


}
