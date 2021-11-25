/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

import io.paperdb.Paper;


public class SplashScreenActivity extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Paper.init(this);
        if(Paper.book().read("darkMode", "").equals("ON")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        FirebaseConnect fC = FirebaseConnect.getInstance();
        createTempAlarmNotificationChannel();

        handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* DO NOT ADD NEW INTENT LINE WHEN TESTING LOGIN FUNCTIONALITY, JUST SWITCH WHICH LINE IS COMMENTED OUT*/
//                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class); //<--- Uncomment this line when working on Login
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class); //<--- Uncomment this line when you don't want login page to load
                startActivity(intent);
                finish();
            }
        },3000);

    }

    private void createTempAlarmNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.temp_alarm);
            String description = getString(R.string.temp_alarm_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("TEMPERATURE", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}