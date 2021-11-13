/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui.settings;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import ca.theautomators.it.smarthomeautomation.LoginActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.RoomManagerActivity;
import ca.theautomators.it.smarthomeautomation.databinding.FragmentSettingsBinding;
import io.paperdb.Paper;


public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    Button logoutBtn,accessCardBtn;
    Switch orientationSwitch;

    View view;
    FirebaseAuth auth;
    StorageReference storageReference;
    GoogleSignInAccount gUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),gso);





        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        view = root;
        logoutBtn = view.findViewById(R.id.settingLogoutBtn);
        orientationSwitch = view.findViewById(R.id.orientationSwitch);
        accessCardBtn = view.findViewById(R.id.accessCardButton);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Paper.init(getActivity());

        gUser = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(gUser!=null){
            Log.e("abc",gUser.getDisplayName());

            storageReference.child(gUser.getGivenName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(binding.settingProfileImg);
                }
            });

        }

        if(auth.getCurrentUser()!=null){
            storageReference.child(auth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(binding.settingProfileImg);
                }
            });
        }

        Button manageRooms = (Button) root.findViewById(R.id.manageRooms);
        Switch darkMode = (Switch)root.findViewById(R.id.darkModeSwitch);

        accessCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                final EditText input = new EditText(getActivity());
                input.setHint("hint");
                alertDialog.setTitle("title");
                alertDialog.setMessage("Enter card number");
                alertDialog.setView(input);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!input.getText().toString().matches("")){
                            HashMap<String, Object> accessCard = new HashMap<>();
                            accessCard.put("Access Card",input.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference("AccessCards").setValue(accessCard);

                        }
                    }
                });
                alertDialog.show();
            }
        });

        binding.chooseBackgroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               permissionHandle();

            }
        });

        if(Paper.book().read("orientationSwitch","").matches("selected")){
            orientationSwitch.setChecked(true);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(Paper.book().read("darkMode", "").equals("ON")){
            darkMode.setChecked(true);
        }
        else{
            darkMode.setChecked(false);
        }

        orientationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Paper.book().write("orientationSwitch","selected");
                }else {
                    Paper.book().write("orientationSwitch","unselected");
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                googleSignInClient.signOut();
                Paper.book().write("useremail","");
                Paper.book().write("userpassword","");

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);

            }
        });

        manageRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomManagerActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Paper.book().write("darkMode", "ON");
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Paper.book().write("darkMode", "OFF");
                }
            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 45 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            binding.settingProfileImg.setImageURI(uri);

            if(auth.getCurrentUser()!=null){
                storageReference.child(auth.getCurrentUser().getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(),"Image Upload failed",Toast.LENGTH_LONG).show();
                            }
                        });
            }else {
                storageReference.child(String.valueOf(gUser.getGivenName())).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(),"Image Upload failed",Toast.LENGTH_LONG).show();
                            }
                        });
            }


        }



    }

    void permissionHandle(){

        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Snackbar.make(view, R.string.permission_granted,Snackbar.LENGTH_LONG).show();
                        chooseImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar.make(view, R.string.permission_denied,Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,45);

    }


}

