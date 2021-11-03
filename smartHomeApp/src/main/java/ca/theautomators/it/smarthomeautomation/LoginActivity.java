/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button logInButton, signUpButton;
    CheckBox rememberMe;
    FirebaseAuth auth;

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
        auth = FirebaseAuth.getInstance();

        // Signing in remember me user
        String userEmail="";
        userEmail = Paper.book().read("useremail");
        String userpassword="";
        userpassword = Paper.book().read("userpassword");

        if(!TextUtils.isEmpty(userEmail)){
            signInUser(userEmail,userpassword);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


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
                }

                signInUser(getemail,encryptedPassword);
            }
        });


    }

    public void signInUser(String email, String encryptedPassword){

        auth.signInWithEmailAndPassword(email,encryptedPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this,"Successfully logged in",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}