package ca.theautomators.it.smarthomeautomation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ca.theautomators.it.smarthomeautomation.R;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        TextView ntextview =findViewById(R.id.textView2);
        ImageView imageView = findViewById(R.id.imageView2);
    }
}