package ca.theautomators.it.smarthomeautomation;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TemperatureNotifier {

    private int notificationId;
    private long temperature;
    private NotificationCompat.Builder builder;
    private Context context;

    public TemperatureNotifier(){}

    public TemperatureNotifier(long temperature){

        this.temperature = temperature;
        context = MainActivity.getMainActivityContext();
        builder = MainActivity.getBuilder();

        if(temperature < 10){
            //send low temp notification
            builder.setContentText("Low Temperature Warning!");
            triggerNotification();
        }
        else if(temperature > 25){
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
