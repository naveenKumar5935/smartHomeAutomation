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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import ca.theautomators.it.smarthomeautomation.LoginActivity;
import ca.theautomators.it.smarthomeautomation.R;
import ca.theautomators.it.smarthomeautomation.RoomManagerActivity;
import ca.theautomators.it.smarthomeautomation.databinding.FragmentSettingsBinding;
import io.paperdb.Paper;


public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    Button logoutBtn;
    View view;
    FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(container != null){

            container.removeAllViews();
        }

        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        view = root;
        logoutBtn = view.findViewById(R.id.settingLogoutBtn);
        auth = FirebaseAuth.getInstance();
        Paper.init(getActivity());

        Button manageRooms = (Button) root.findViewById(R.id.manageRooms);

        binding.chooseBackgroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               permissionHandle();

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
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

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 45 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            binding.settingProfileImg.setImageURI(uri);
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

