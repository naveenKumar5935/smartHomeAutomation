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

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private EditText email,password,accessCode, fullname, phone,confirmPassword;
    private Button loginBtn, registerBtn;
    private FirebaseAuth auth;
    private GoogleSignInButton googleSignUpButton;
    private GoogleSignInClient mGoogleSignInClient;
    private Boolean connection=false;
    CheckBox rememberMe;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Paper.init(this);
        if(Paper.book().read("orientationSwitch","").matches("selected")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        email = findViewById(R.id.register_email_tb);
        password = findViewById(R.id.register_password_tb);
        accessCode = findViewById(R.id.register_access_tb);
        registerBtn = findViewById(R.id.registerButton);
        loginBtn = findViewById(R.id.registerLoginBtn);
        fullname = findViewById(R.id.register_fullname_et);
        phone = findViewById(R.id.register_phone_et);
        confirmPassword = findViewById(R.id.register_confirmpassword_et);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);
        auth = FirebaseAuth.getInstance();
        rememberMe = findViewById(R.id.registerRememberMe);
        currentUser = auth.getCurrentUser();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Tovuti.from(RegisterActivity.this).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                if(isConnected){
                    connection=true;
                }else {
                    connection=false;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail = email.getText().toString().trim();
                String getpass = password.getText().toString().trim();
                String getConfirmPassword = confirmPassword.getText().toString().trim();
                if(!getpass.matches(getConfirmPassword)){
                    confirmPassword.setError(getString(R.string.password_error_r));
                    return;
                }

                if (!emailCheck(getemail)){
                    email.setError(getString(R.string.email_error));
                    return;
                }

                if(checkPassword(getpass)){

                }else {
                    password.setError(getString(R.string.password_error_register));
                    return;
                }
                PasswordEncryption passwordEncryption = new PasswordEncryption(getpass);
                String encryptedPassword = passwordEncryption.getHashedPassword();
                String getaccess = accessCode.getText().toString().trim();
                if(TextUtils.isEmpty(getaccess)){
                    Toast.makeText(RegisterActivity.this, R.string.enter_access,Toast.LENGTH_SHORT).show();
                }else {

                    if(connection){
                        getAndMatchAccessCode(getaccess,getemail,encryptedPassword);
                    }else {
                        Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accessCodeEntered = accessCode.getText().toString().trim();
                if(TextUtils.isEmpty(accessCodeEntered)){
                    Toast.makeText(RegisterActivity.this,R.string.enter_access,Toast.LENGTH_SHORT).show();
                }else {
                    if(connection){
                        getAndMatchAccessCode(accessCodeEntered,null,null);
                    }else {
                        Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();

                    }

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

    public void getAndMatchAccessCode(String code, String email, String password){

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String accessCodeStored = snapshot.child("UserAccessCode").child("AccessCode").getValue().toString();
//                if(accessCodeStored.matches(code)){

                    if(email==null){
                        googleSignInRequest();
                        return;
                    }

                    auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean check = task.getResult().getSignInMethods().isEmpty();

                            if(check){
                                setUserData(email,password);
                            }else {
                                Toast.makeText(RegisterActivity.this, R.string.user_already_Exist,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


//                }else {
//                    Toast.makeText(RegisterActivity.this, R.string.invalid_access_code,Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setUserData(String email, String password){

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String getphone = phone.getText().toString();
                            String getname = fullname.getText().toString();
                            String code = accessCode.getText().toString().trim();
                            User user = new User(email,password,getname,getphone,code);
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, R.string.successful_signedup,Toast.LENGTH_SHORT).show();

                                                if(rememberMe.isChecked()){
                                                    Paper.book().write("useremail",email);
                                                    Paper.book().write("userpassword",password);
                                                }else {
                                                    Paper.book().write("useremail","");
                                                    Paper.book().write("userpassword","");
                                                }

                                                HashMap<String,String> pi = new HashMap<>();
                                                pi.put("Devices","");
                                                pi.put("Code","");

                                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                            }
                                        }
                                    });

                        }else {
                        }
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
            FirebaseSignUpWithGoogle();
        } catch (ApiException e) {

            FirebaseSignUpWithGoogle();
        }
    }


    public void FirebaseSignUpWithGoogle(){

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GoogleSignInAccount gUser = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
                String email = gUser.getEmail();

                if (snapshot.child("googleUsers").child(gUser.getId()).exists()) {
                    Toast.makeText(RegisterActivity.this, R.string.already_signed_up, Toast.LENGTH_SHORT).show();
                    return;


                } else {

                    HashMap<String, Object> userdatamap = new HashMap<>();
                    userdatamap.put("email", email);
                    userdatamap.put("name", gUser.getGivenName());

                    FirebaseDatabase.getInstance().getReference("googleUsers").child(gUser.getId()).setValue(userdatamap);
                    Toast.makeText(RegisterActivity.this, R.string.success,Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
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