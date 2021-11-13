package ca.theautomators.it.smarthomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class ReportIssue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Report an Issue");


        EditText report = findViewById(R.id.issue);
        EditText email = findViewById(R.id.email_issue);
        Button sendFeedback = findViewById(R.id.button_issue);


        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(ReportIssue.this, "Please fill in Email text box", Toast.LENGTH_LONG).show();

                }

                else if (report.getText().toString().isEmpty()) {
                    Toast.makeText(ReportIssue.this, "Please fill in report text box", Toast.LENGTH_LONG).show();
                }

                else  {

                    //Sending data to Database
                    HashMap<String, Object> userReport = new HashMap<>();
                    userReport.put("email",email.getText().toString().trim());
                    userReport.put("report", report.getText().toString());
                    FirebaseDatabase.getInstance().getReference("UserReports").child(email.getText().toString().replace(".","")).setValue(userReport);


                    report.setText("");
                    email.setText("");

                    Toast.makeText(ReportIssue.this, "Thank you for sharing your concern", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}