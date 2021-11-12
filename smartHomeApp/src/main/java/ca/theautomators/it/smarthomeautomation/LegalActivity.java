package ca.theautomators.it.smarthomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.paperdb.Paper;

public class LegalActivity extends AppCompatActivity {

    ListView lv;
    String[] tutorials
            = { "Google Terms of Service", "Google Privacy Policy", "Open Source Compliance" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Legal");

        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        lv = findViewById(R.id.simpleListView);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tutorials);
        lv.setAdapter(arr);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) lv.getItemAtPosition(position);

                if ("Google Terms of Service".equals(value)) {
                    Uri uri = Uri.parse("https://policies.google.com/terms?hl=en-US");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                else if ( "Google Privacy Policy".equals(value)){
                    Uri uri = Uri.parse("https://policies.google.com/privacy?hl=en-US");
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent1);
                }

                else if ( "Open Source Compliance".equals(value)){
                    Uri uri = Uri.parse("https://github.com/naveenKumar5935/smartHomeAutomation.git");
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent2);
                }

            }


        });





    }

}