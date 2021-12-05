/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class ReportIssue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.reporttitle));


        EditText report = findViewById(R.id.issue);
        EditText email = findViewById(R.id.email_issue);
        Button sendFeedback = findViewById(R.id.button_issue);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            email.setText(currentUserEmail);
        }else {
            GoogleSignInAccount gUser = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if(gUser!=null){
                String currentUserEmail = gUser.getEmail();
                email.setText(currentUserEmail);
            }
        }



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
                    userReport.put(getString(R.string.reportemail),email.getText().toString().trim());
                    userReport.put(getString(R.string.report), report.getText().toString());
                    FirebaseDatabase.getInstance().getReference("UserReports").child(email.getText().toString().replace(".","")).setValue(userReport);


                    report.setText("");
                    email.setText("");

                    Toast.makeText(ReportIssue.this, "Thank you for sharing your concern", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public boolean emailCheck(String email){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return true;
        }else {
            return false;
        }
    }
}