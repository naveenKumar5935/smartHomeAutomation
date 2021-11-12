/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    EditText email, password;
    Button logInButton, signUpButton;
    CheckBox rememberMe;
    GoogleSignInButton googleSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;

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
                FirebaseSignInWithGoogle();
            }
        }

        //Log.d("user",userEmail);

        if(!TextUtils.isEmpty(userEmail)){
          getUserData(userEmail,userpassword);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rememberMe.isChecked()){
                    Paper.book().write("googleSignIn","checked");
                }else {
                    Paper.book().write("googleSignIn","unchecked");
                }
                    googleSignInRequest();
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
                if (!getemail.contains("@") || !getemail.contains(".")){
                    email.setError(getString(R.string.email_error));
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


                getUserData(getemail,encryptedPassword);

            }
        });


    }


    public void getUserData(String email, String password){

        // analysing if user is already registered or login credentials are right or wrong
        auth.signInWithEmailAndPassword(email,password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("exception",e.toString());
                if(e.toString().contains("invalid")){
                    Toast.makeText(LoginActivity.this,"Please check your credentials",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(e.toString().contains("no user")){
                    Toast.makeText(LoginActivity.this,"Please Create an account",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this,"Successfully Logged In",Toast.LENGTH_SHORT).show();
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
            Log.i("name",account.getDisplayName());

            // Signed in successfully, show authenticated UI.
            FirebaseSignInWithGoogle();
        } catch (ApiException e) {

            FirebaseSignInWithGoogle();
        }
    }


    public void FirebaseSignInWithGoogle(){
        //   Log.i("name",account.getDisplayName());

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GoogleSignInAccount gUser = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

                if (snapshot.child("googleUsers").child(gUser.getId()).exists()) {
                    Toast.makeText(LoginActivity.this, "Welcome Back " + gUser.getGivenName(), Toast.LENGTH_SHORT).show();
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




}