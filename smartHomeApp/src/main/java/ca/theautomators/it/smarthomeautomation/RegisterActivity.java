/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    EditText email,password,accessCode;
    Button loginBtn, registerBtn;
    FirebaseAuth auth;
    GoogleSignInButton googleSignUpButton;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.register_email_tb);
        password = findViewById(R.id.register_password_tb);
        accessCode = findViewById(R.id.register_access_tb);
        registerBtn = findViewById(R.id.registerButton);
        loginBtn = findViewById(R.id.registerLoginBtn);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
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
                String getaccess = accessCode.getText().toString().trim();
                if(TextUtils.isEmpty(getaccess)){
                    Toast.makeText(RegisterActivity.this,"Please enter access code",Toast.LENGTH_SHORT).show();
                }else {
                    getAndMatchAccessCode(getaccess,getemail,encryptedPassword);
                }
            }
        });

        googleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accessCodeEntered = accessCode.getText().toString().trim();
                if(TextUtils.isEmpty(accessCodeEntered)){
                    Toast.makeText(RegisterActivity.this,"Please enter access code",Toast.LENGTH_SHORT).show();
                }else {
                    getAndMatchAccessCode(accessCodeEntered,null,null);
                }
            }
        });

    }

    public void getAndMatchAccessCode(String code, String email, String password){

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String accessCodeStored = snapshot.child("UserAccessCode").child("AccessCode").getValue().toString();
                if(accessCodeStored.matches(code)){

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
                                Toast.makeText(RegisterActivity.this,"User already exist",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }else {
                    Toast.makeText(RegisterActivity.this, R.string.invalid_access_code,Toast.LENGTH_SHORT).show();
                }
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
                            User user = new User(email,password);
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this,"Successfully Signed Up",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
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
        //   Log.i("name",account.getDisplayName());

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GoogleSignInAccount gUser = GoogleSignIn.getLastSignedInAccount(RegisterActivity.this);
                String email = gUser.getEmail();

                if (snapshot.child("googleUsers").child(gUser.getId()).exists()) {
                    Toast.makeText(RegisterActivity.this, "Already Signed Up, please log in", Toast.LENGTH_SHORT).show();
                    return;


                } else {

                    HashMap<String, Object> userdatamap = new HashMap<>();
                    userdatamap.put("email", email);
                    userdatamap.put("name", gUser.getGivenName());

                    FirebaseDatabase.getInstance().getReference("googleUsers").child(gUser.getId()).setValue(userdatamap);
                    Toast.makeText(RegisterActivity.this,"Successfully Signed Up",Toast.LENGTH_SHORT).show();

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}