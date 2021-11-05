/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password,accessCode;
    Button loginBtn, registerBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.register_email_tb);
        password = findViewById(R.id.register_password_tb);
        accessCode = findViewById(R.id.register_access_tb);
        registerBtn = findViewById(R.id.registerButton);
        loginBtn = findViewById(R.id.registerLoginBtn);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
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

                String getaccess = accessCode.getText().toString();
                PasswordEncryption passwordEncryption = new PasswordEncryption(getpass);
                String encryptedPassword = passwordEncryption.getHashedPassword();
                FirebaseConnect firebaseConnect = FirebaseConnect.getInstance();
                firebaseConnect.setUserData(getemail,encryptedPassword);

                if(firebaseConnect.result==true){
                    Toast.makeText(RegisterActivity.this,"Successfully Signed Up",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


}