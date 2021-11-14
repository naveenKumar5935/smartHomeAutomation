/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ca.theautomators.it.smarthomeautomation.R;
import io.paperdb.Paper;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("About App");
        Paper.init(this);

        if(Paper.book().read("orientationSwitch","").matches("selected")){

           setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        TextView ntextview =findViewById(R.id.textView2);
        //ImageView imageView = findViewById(R.id.imageView2);
        ImageView imageView = findViewById(R.id.imageView2);
    }
}