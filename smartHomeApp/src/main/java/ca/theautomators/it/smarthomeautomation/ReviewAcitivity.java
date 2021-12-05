/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;

public class ReviewAcitivity extends AppCompatActivity {
    private TextView mTextView;
    private FirebaseConnect firebaseConnect;
    private RatingBar mRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_acitivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.review_heading));

        mTextView = findViewById(R.id.text);

        Paper.init(this);
        if(Paper.book().read(getString(R.string.orientationSwitch),"").matches(getString(R.string.selected))){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

       mRatingBar = findViewById(R.id.ratingBar);
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
                        mRatingScale.setText(R.string.verybad);
                        break;
                    case 2:
                        mRatingScale.setText(R.string.Needsomeimprovement);
                        break;
                    case 3:
                        mRatingScale.setText(R.string.Good);
                        break;
                    case 4:
                        mRatingScale.setText(R.string.Great);
                        break;
                    case 5:
                        mRatingScale.setText(R.string.AwesomeIlikedtheservice);
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                float rating = mRatingBar.getRating();

                if (rating < 1 ){
                    Toast.makeText(ReviewAcitivity.this, R.string.choose_rating, Toast.LENGTH_LONG).show();
                }

                else if (Name.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewAcitivity.this, R.string.fill_in_name, Toast.LENGTH_LONG).show();

                }

                else if(Name.getText().toString().trim().length() < 3){
                    Name.setError(getString(R.string.nameerror));
                }

                else if (phonenumber.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewAcitivity.this, R.string.fill_in_phone, Toast.LENGTH_LONG).show();

                }


                else if(phonenumber.getText().toString().trim().length() < 10){
                    phonenumber.setError(getString(R.string.phonenoerror));
                }


                else if(!email.getText().toString().trim().contains("@") || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Toast.makeText(ReviewAcitivity.this, R.string.enter_valid_email, Toast.LENGTH_LONG).show();

                }
                else if(mFeedback.getText().toString().isEmpty()){
                    Toast.makeText(ReviewAcitivity.this, R.string.fill_in_feedback, Toast.LENGTH_LONG).show();

                }

                else  {

                    String name = Name.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String phone = phonenumber.getText().toString().trim();
                    String feedback = mFeedback.getText().toString();
                    String modelNo = getDeviceName();

                    firebaseConnect.setUserFeedback(name,email1,phone,feedback,rating,modelNo);

                    mFeedback.setText("");
                    Name.setText("");
                    phonenumber.setText("");
                    email.setText("");
                    mRatingBar.setRating(0);
                    new ProgressTask().execute();
                }
            }
        });

    }

    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        private int progressStatus=0;
        private Handler handler = new Handler();
        private ProgressDialog pd = new ProgressDialog(ReviewAcitivity.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setMessage(getString(R.string.loading));
            pd.setTitle(getString(R.string.progress_dialog));
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            pd.setCancelable(true);
            pd.show();
            onPostExecute();
        }

        @Override
        protected Void doInBackground(Void...args){
            progressStatus = 0;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(progressStatus < 100){
                        progressStatus +=1;

                        try{
                            Thread.sleep(20);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pd.setProgress(progressStatus);
                                if(progressStatus == 100){
                                    pd.dismiss();
                                }
                            }
                        });
                    }
                }
            }).start();

            return null;
        }

        protected void onPostExecute(){
            Toast.makeText(ReviewAcitivity.this, R.string.thanks_for_sharing_feedback, Toast.LENGTH_SHORT).show();

        }
}

    //Get device model number and store in the database
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

    public void setRatingBarForTest(float star){
        mRatingBar.setRating(star);
    }

}