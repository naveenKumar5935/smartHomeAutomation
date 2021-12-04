/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    EditText email, password;
    Button logInButton, signUpButton;
    CheckBox rememberMe;
    GoogleSignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;
    Boolean connection=false;

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
        auth = FirebaseAuth.getInstance();

        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // Signing in remember me user
        String userEmail="";
        userEmail = Paper.book().read("useremail","");
        String userpassword="";
        userpassword = Paper.book().read("userpassword","");
        String googleRememberMeCheck="";
        googleRememberMeCheck=Paper.book().read("googleSignIn","");

        if(googleRememberMeCheck.matches("checked")){
            GoogleSignInAccount gUser = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if(gUser!=null){
                if(connection){
                    FirebaseSignInWithGoogle();
                }else {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }

        //Log.d("user",userEmail);

        if(!TextUtils.isEmpty(userEmail)){
            if(connection){
                getUserData(userEmail,userpassword);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        }

        Tovuti.from(LoginActivity.this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                if(isConnected){
                    connection=true;
                }else {
                    connection=false;
                }
            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rememberMe.isChecked()){
                    Paper.book().write("googleSignIn","checked");
                }else {
                    Paper.book().write("googleSignIn","unchecked");
                }

                if(connection){
                    googleSignInRequest();
                }else {
                    Toast.makeText(getApplicationContext(), R.string.check_connection,Toast.LENGTH_SHORT).show();
                }

            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail = email.getText().toString().trim();
                String getpass = password.getText().toString().trim();
                if (!emailCheck(getemail)){
                    email.setError(getString(R.string.email_error));
                    return;
                }

                if(checkPassword(getpass)){

                }else {
                    password.setError(getString(R.string.login_error));
                    Toast.makeText(LoginActivity.this, R.string.password_error,Toast.LENGTH_SHORT).show();
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

                    if(connection){
                        getUserData(getemail,encryptedPassword);
                    }else {
                        Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
                    }

            }
        });


    }

    public boolean checkPassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&=?])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(password);
        if(matcher.matches()){
            return true;
        }else {
            return false;
        }
    }

    public boolean emailCheck(String email){
        Pattern  pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return true;
        }else {
            return false;
        }
    }


    public void getUserData(String email, String password){

        // analysing if user is already registered or login credentials are right or wrong
        auth.signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("exception",e.toString());
                if(e.toString().contains("invalid")){
                    Toast.makeText(LoginActivity.this, R.string.toast_login_check,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(e.toString().contains("no user")){
                    Toast.makeText(LoginActivity.this, R.string.Please_create_can_acc,Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, R.string.successlogin_toast,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);

                finish();

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

            FirebaseSignInWithGoogle();
        } catch (ApiException e) {
        }
    }


    public void FirebaseSignInWithGoogle(){

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GoogleSignInAccount gUser = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

                if (snapshot.child("googleUsers").child(gUser.getId()).exists()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.wlcm_back) + gUser.getGivenName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(LoginActivity.this, R.string.sign_up_toast,Toast.LENGTH_SHORT).show();

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle(R.string.alert_title);
        alert.setMessage(R.string.alert_message);
        alert.setPositiveButton(R.string.alert_positive_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.setNegativeButton(R.string.alert_negative_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

    }





}