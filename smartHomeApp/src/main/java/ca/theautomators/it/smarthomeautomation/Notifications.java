/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Notifications {

    private int notificationId;
    private String value, type;
    private NotificationCompat.Builder builder;
    private Context context;

    //Must keep default constructor to protect from null pointer exception
    public Notifications(){}

    public Notifications(String value, String type){

        this.value = value;
        this.type = type;

        context = MainActivity.getMainActivityContext();
        builder = MainActivity.getBuilder();

        switch(type){
            case "TEMP":
                buildTempNotification();
            //Add more switch cases here for different types of sensor notifications
        }


    }

    private void triggerNotification(){

        notificationId = 1;

        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        notification.notify(notificationId, builder.build());

    }

    //Add more methods for different notifications based on this one, except customize them per type
    private void buildTempNotification(){

        //Keep the colon in the string so that firebase send the value as a string and not a long
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

    public int getNotificationId(){
        return notificationId;
    }
}
