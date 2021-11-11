/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */

package ca.theautomators.it.smarthomeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

                String getaccess = accessCode.getText().toString().trim();
                PasswordEncryption passwordEncryption = new PasswordEncryption(getpass);
                String encryptedPassword = passwordEncryption.getHashedPassword();
                    getAndMatchAccessCode(getaccess,getemail,encryptedPassword);
            }
        });



    }

    public void getAndMatchAccessCode(String code, String email, String password){

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String accessCodeStored = snapshot.child("UserAccessCode").child("AccessCode").getValue().toString();
                if(accessCodeStored.matches(code)){
                    setUserData(email,password);
                }else {
                    Toast.makeText(RegisterActivity.this,"Invalid Access Code",Toast.LENGTH_SHORT).show();
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
                                            }else {
                                            }
                                        }
                                    });
                        }else {
                        }
                    }
                });


    }


}