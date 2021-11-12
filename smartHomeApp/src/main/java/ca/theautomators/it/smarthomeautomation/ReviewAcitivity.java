package ca.theautomators.it.smarthomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;

public class ReviewAcitivity extends AppCompatActivity {
    private TextView mTextView;
    FirebaseConnect firebaseConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_acitivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Review");

        mTextView = findViewById(R.id.text);

        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        RatingBar mRatingBar = findViewById(R.id.ratingBar);
        TextView mRatingScale =findViewById(R.id.tvRatingScale);
        EditText mFeedback =  findViewById(R.id.etFeedback);
        Button mSendFeedback = findViewById(R.id.btnSubmit);
        EditText Name = findViewById(R.id.Name);
        EditText phonenumber = findViewById(R.id.Phoneno);
        EditText email = findViewById(R.id.email);

        firebaseConnect = FirebaseConnect.getInstance();


        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome, I liked the service");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Name.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewAcitivity.this, "Please fill in Name text box", Toast.LENGTH_LONG).show();

                }

                else if(Name.getText().toString().trim().length() < 3){
                    Name.setError("Minimum 3 words");
                }

                else if (phonenumber.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewAcitivity.this, "Please fill in phone number text box", Toast.LENGTH_LONG).show();

                }


                else if(phonenumber.getText().toString().trim().length() < 10){
                    phonenumber.setError("Invalid");
                }

                else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewAcitivity.this, "Please fill in Email text box", Toast.LENGTH_LONG).show();

                }

                else if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewAcitivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();

                }

                else  {
                    String name = Name.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String phone = phonenumber.getText().toString().trim();
                    String feedback = mFeedback.getText().toString();
                    float rating = mRatingBar.getRating();
                    String modelNo = getDeviceName();

                    firebaseConnect.setUserFeedback(name,email1,phone,feedback,rating,modelNo);


                    mFeedback.setText("");
                    Name.setText("");
                    phonenumber.setText("");
                    email.setText("");
                    mRatingBar.setRating(0);
                    Toast.makeText(ReviewAcitivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                 //   Toast.makeText(ReviewAcitivity.this, getDeviceName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}