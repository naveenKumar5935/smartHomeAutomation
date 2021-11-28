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
    private String temperature;
    private NotificationCompat.Builder builder;
    private Context context;

    public Notifications(){}

    public Notifications(String temperature){

        this.temperature = temperature;
        temperature = temperature.split(":")[0];
        context = MainActivity.getMainActivityContext();
        builder = MainActivity.getBuilder();

        if(Integer.valueOf(temperature) < 10){
            //send low temp notification
            builder.setContentText("Low Temperature Warning!");
            triggerNotification();
        }
        else if(Integer.valueOf(temperature) > 25){
            //send high temp notification
            builder.setContentText("High Temperature Warning!");
            triggerNotification();
        }

    }

    private void triggerNotification(){

        notificationId = 1;

        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        notification.notify(notificationId, builder.build());

    }

    public int getNotificationId(){
        return notificationId;
    }
}
