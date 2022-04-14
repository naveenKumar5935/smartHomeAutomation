/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.database.IgnoreExtraProperties;

import ca.theautomators.it.smarthomeautomation.ui.settings.AutomationActivity;
import io.paperdb.Paper;

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
                break;
            case "PIR":
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

        if(Integer.valueOf(value) < 5){
            //send low temp notification
            builder.setContentText("Low Temperature Warning");
            //Last step must be to call this method
            triggerNotification();
        }
        else if(Integer.valueOf(value) > 35){
            //send high temp notification
            builder.setContentText("High Temperature Warning");
            triggerNotification();
        }
    }

    private void buildRFIDnotification(){

            builder.setContentText(value);
        if(Paper.book().read("rfidNotify","").equals("true")){
            triggerNotification();
        }

    }

    private void buildSmokeAlarmnotification(){
        value = value.split(":")[1];

        if(Integer.valueOf(value)  == 1 ){
            builder.setContentText("Smoke Detected!");
            Intent intent = new Intent(context, AutomationActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent =
                    stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);
            builder.setColor(0xFF0000);
            triggerNotification();
        }
    }
    private void buildHumidnotification(){
        value = value.split(":")[0];

        if(Integer.valueOf(value) < 10){
            builder.setContentText("Humidity Level Very Low");
            triggerNotification();
        }
    }

    private void buildMotionnotification(){
        value = value.split(":")[1];

        if(Integer.valueOf(value)  == 1){
            builder.setContentText("Motion Detected!");
            if(Paper.book().read("motionNotify","").equals("true")){
                triggerNotification();
            }

    }

    }

    public int getNotificationId(){
        return notificationId;
    }
}
