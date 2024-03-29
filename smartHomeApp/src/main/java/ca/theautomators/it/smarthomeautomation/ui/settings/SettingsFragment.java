/*
 Naveen Kumar N01355935 Section-RNA
 Gaganajeet Hanny N01350705 Section-RNA
 Sukhmanpreet Kaur N01355022 Section-RNA
 James Ricci N00411900 Section-RNA
 */
package ca.theautomators.it.smarthomeautomation.ui.settings;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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

import ca.theautomators.it.smarthomeautomation.LoginActivity;
import ca.theautomators.it.smarthomeautomation.MainActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.RoomManagerActivity;
import io.paperdb.Paper;


public class SettingsFragment extends Fragment {

    Button logoutBtn,accessCardBtn;
    Boolean connection;
    View view;
    FirebaseAuth auth;
    StorageReference storageReference;
    GoogleSignInAccount gUser;
    FloatingActionButton fab;
    ImageView profileImg;
    Button bgButton, automationBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),gso);


      View view =  inflater.inflate(R.layout.fragment_settings,container,false);

        logoutBtn = view.findViewById(R.id.settingLogoutBtn);
        accessCardBtn = view.findViewById(R.id.accessCardButton);
        automationBtn = view.findViewById(R.id.automationBtn);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        profileImg = view.findViewById(R.id.settingProfileImg);
        bgButton = view.findViewById(R.id.chooseBackgroundBtn);
        Paper.init(getActivity());
        fab = view.findViewById(R.id.settingfab);

        gUser = GoogleSignIn.getLastSignedInAccount(getActivity());

        if(gUser!=null){

            storageReference.child(gUser.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImg);
                }
            });

        }

        if(auth.getCurrentUser()!=null){
            storageReference.child(auth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImg);
                }
            });
        }

        Button manageRooms = (Button) view.findViewById(R.id.manageRooms);

        accessCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getActivity().getApplicationContext(),AccessCardActivity.class);
            getActivity().startActivity(intent);

            }
        });

        automationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), AutomationActivity.class);
                startActivity(intent);

//                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("devices").child("101").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            Intent intent = new Intent(getActivity().getApplicationContext(),Automation.class);
//                            startActivity(intent);
//                        }else {
//                            Toast.makeText(getActivity(), "Check your devices", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


            }
        });


        Tovuti.from(getContext()).monitor(new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                try{
                    if (isConnected) {
                        connection = true;
                        fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_wifi_24));
                    } else {
                        connection = false;
                        Drawable wifiOffImg = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_signal_wifi_off_24);
                        if (wifiOffImg != null) {
                            fab.setImageDrawable(wifiOffImg);
                        }
                    }
                }
                catch(IllegalStateException ex){
                    ex.printStackTrace();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connection){
                    Toast.makeText(getActivity().getApplicationContext(), R.string.internet_connected,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.internet_not_connected,Toast.LENGTH_SHORT).show();
                }
            }
        });


        bgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               permissionHandle();

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
                startActivity(intent);
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MainActivity)getActivity()).fragmentSwitch(R.id.nav_home);
                    return true;
                }

                return false;
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 45 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            profileImg.setImageURI(uri);

            if(auth.getCurrentUser()!=null){
                storageReference.child(auth.getCurrentUser().getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.image_uploaded,Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.img_failef,Toast.LENGTH_LONG).show();
                            }
                        });
            }else {
                storageReference.child(String.valueOf(gUser.getId())).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.img_upload,Toast.LENGTH_LONG).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), R.string.img_upl,Toast.LENGTH_LONG).show();
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
                        Snackbar.make(getView(), R.string.permission_granted,Snackbar.LENGTH_LONG).show();
                        chooseImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar.make(getView(), R.string.permission_denied,Snackbar.LENGTH_LONG).show();
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
