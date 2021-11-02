/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ca.theautomators.it.smarthomeautomation.databinding.ActivityRegisterBinding;

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

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail = email.getText().toString();

                if (!getemail.contains("@") || !getemail.contains(".")){
                    email.setError("Please type correct email");
                    return;

                }
                String getpass = password.getText().toString();
                String getaccess = accessCode.getText().toString();
                PasswordEncryption passwordEncryption = new PasswordEncryption(getpass);
                String encryptedPassword = passwordEncryption.getHashedPassword();

                signUpFirebase(getemail,encryptedPassword);
            }
        });



    }

    public void signUpFirebase(String email, String encryptedPassword){
        auth.createUserWithEmailAndPassword(email,encryptedPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Users user = new Users(email,encryptedPassword);
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this,"done",Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(RegisterActivity.this,"failed adding to database",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else {
                            Toast.makeText(RegisterActivity.this,"failed creating user",Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }


}