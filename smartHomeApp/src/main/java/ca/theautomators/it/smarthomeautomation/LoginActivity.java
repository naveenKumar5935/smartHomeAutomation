/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    EditText email, password;
    Button logInButton, signUpButton;
    CheckBox rememberMe;
    GoogleSignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Paper.init(LoginActivity.this);
        email = findViewById(R.id.login_email_tb);
        password = findViewById(R.id.login_password_tb);
        logInButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.loginSignupBtn);
        rememberMe = findViewById(R.id.loginRememberMe);
        googleSignInButton = findViewById(R.id.googleSignInButton);

        // Firebase Singleton
        FirebaseConnect firebaseConnect = FirebaseConnect.getInstance();

        // Signing in remember me user
        String userEmail="";
        userEmail = Paper.book().read("useremail");
        String userpassword="";
        userpassword = Paper.book().read("userpassword");

        //Log.d("user",userEmail);

        if(!TextUtils.isEmpty(userEmail)){
            firebaseConnect.getUserData(userEmail,userpassword);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    googleSignInRequest();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail = email.getText().toString().trim();
                String getpass = password.getText().toString().trim();
                if (!getemail.contains("@") || !getemail.contains(".")){
                    email.setError("Please type correct email");
                    return;
                }
                if(getpass.length()<8){
                    password.setError("Minimum length should be 8");
                    return;
                }
                PasswordEncryption passwordEncryption = new PasswordEncryption(getpass);

                String encryptedPassword = passwordEncryption.getHashedPassword();

                if(rememberMe.isChecked()){
                    Paper.book().write("useremail",getemail);
                    Paper.book().write("userpassword",encryptedPassword);
                }else {
                    Paper.book().write("useremail","");
                    Paper.book().write("userpassword","");
                }


                firebaseConnect.getUserData(getemail,encryptedPassword);

//                if(firebaseConnect.result==true){
//                    Toast.makeText(LoginActivity.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    startActivity(intent);
//
//                    finish();
//                }
            }
        });


    }

    private void googleSignInRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.i("name",account.getDisplayName());

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {

            updateUI(null);
        }
    }



    private void updateUI(GoogleSignInAccount account) {

        FirebaseConnect firebaseConnect = FirebaseConnect.getInstance();
        firebaseConnect.setContext(LoginActivity.this);
        firebaseConnect.FirebaseAuthWithGoogle(account);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);


    }

    public String[] accessCheck(){
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(LoginActivity.this);

        alert.setTitle("Access Code");
        alert.setMessage("Please enter the access code");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        final String[] valueEntered = new String[1];

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                valueEntered[0] = value;
                // Do something with value!
                Log.i("input",input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

        return valueEntered;


    }
}