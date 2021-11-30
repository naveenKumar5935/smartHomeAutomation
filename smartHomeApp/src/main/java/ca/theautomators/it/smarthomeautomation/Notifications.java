/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Notifications {

    private int notificationId;
    private String value, type;
    private NotificationCompat.Builder builder;
    private Context context;

    public Notifications(){}

    public Notifications(String value, String type){

        this.value = value;
        this.type = type;

        context = MainActivity.getMainActivityContext();
        builder = MainActivity.getBuilder();

        switch(type){
            case "TEMP":
                buildTempNotification();
                break;
            case "RFID":
                buildRFIDnotification();
                break;
            case "SMOKE":
                buildSmokeAlarmnotification();
                break;
            case "HUMID":
                buildHumidnotification();
            case "MOTION":
                buildMotionnotification();
        }


    }

    private void triggerNotification(){

        notificationId = 1;

        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        notification.notify(notificationId, builder.build());

    }

    private void buildTempNotification(){

        value = value.split(":")[0];

        if(Integer.valueOf(value) < 10){
            //send low temp notification
            builder.setContentText("Low Temperature Warning!");
            //Last step must be to call this method
            triggerNotification();
        }
        else if(Integer.valueOf(value) > 25){
            //send high temp notification
            builder.setContentText("High Temperature Warning!");
            triggerNotification();
        }
    }

    private void buildRFIDnotification(){
            Log.e("value",value);
            //send high temp notification
            builder.setContentText(value);
            triggerNotification();

    }

    private void buildSmokeAlarmnotification(){
        value = value.split(":")[1];

        if(Integer.valueOf(value) == 1){
            builder.setContentText("Smoke detected");
            triggerNotification();
        }
    }
    private void buildHumidnotification(){
        if(Integer.valueOf(value) <10){
            //send high smoke notification
            builder.setContentText("Humidity level is very low");
            triggerNotification();
        }
    }

    private void buildMotionnotification(){
        value = value.split(":")[0];

        if(Integer.valueOf(value) < 12){
            //send low temp notification
            builder.setContentText("Motion detected!");
            //Last step must be to call this method
            triggerNotification();
        }
        else if(Integer.valueOf(value) > 5){
            //send high temp notification
            builder.setContentText("Motion detected!");
            triggerNotification();
        }

    }

    public int getNotificationId(){
        return notificationId;
    }
}
