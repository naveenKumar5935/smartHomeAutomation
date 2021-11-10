package ca.theautomators.it.smarthomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewAcitivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_acitivity);

        mTextView = findViewById(R.id.text);

        RatingBar mRatingBar = findViewById(R.id.ratingBar);
        TextView mRatingScale =findViewById(R.id.tvRatingScale);
        EditText mFeedback =  findViewById(R.id.etFeedback);
        Button mSendFeedback = findViewById(R.id.btnSubmit);
        EditText Name = findViewById(R.id.Name);
        EditText phonenumber = findViewById(R.id.Phoneno);
        EditText email = findViewById(R.id.email);


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
                    mFeedback.setText("");
                    Name.setText(" ");
                    phonenumber.setText(" ");
                    email.setText("");
                    mRatingBar.setRating(0);
                    Toast.makeText(ReviewAcitivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}