/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
        setTitle(getString(R.string.legal_title));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                    Uri uri = Uri.parse(getString(R.string.terms_link));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                else if ( "Google Privacy Policy".equals(value)){
                    Uri uri = Uri.parse(getString(R.string.privacy_link));
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent1);
                }

                else if ( "Open Source Compliance".equals(value)){
                    Uri uri = Uri.parse(getString(R.string.source_code_link));
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent2);
                }

            }

        });

    }

}