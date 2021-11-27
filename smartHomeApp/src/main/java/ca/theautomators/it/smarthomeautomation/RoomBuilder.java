package ca.theautomators.it.smarthomeautomation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import ca.theautomators.it.smarthomeautomation.ui.RoomFragment;

public class RoomBuilder{

    NavigationView navView;
    Bundle savedInstanceState;
    DrawerLayout drawer;
    FragmentManager fragMan;
    RoomState rS;

    public RoomBuilder(NavigationView navView, Bundle savedInstanceState, DrawerLayout drawer, FragmentManager fragMan){

        this.navView = navView;
        this.savedInstanceState = savedInstanceState;
        this.drawer = drawer;
        this.fragMan = fragMan;
        rS = RoomState.getInstance(null);

    }

    public void buildRoom(String roomName, Drawable roomIcon, int navId){

        Menu menu = navView.getMenu();
        MenuItem home = menu.findItem(R.id.nav_home);
        MenuItem settings = menu.findItem(R.id.nav_settings);

        ArrayList<MenuItem> items = new ArrayList<>();

        if(savedInstanceState == null){

            fragMan.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.nav_host_fragment, RoomFragment.class, null)
                    .addToBackStack(roomName)
                    .commit();
        }

        menu.add(R.id.drawer_list, navId, 0, roomName).setCheckable(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                RoomFragment rF = new RoomFragment();

                FragmentTransaction fT = fragMan.beginTransaction();
                fT.replace(R.id.nav_host_fragment, rF);
                fT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fT.commit();


                drawer.closeDrawer(Gravity.LEFT);
                home.setChecked(false);
                settings.setChecked(false);
                item.setChecked(true);
                rS.uncheckOtherItems(item);

                return false;
            }
        });

        MenuItem current = menu.findItem(navId);
        rS.addMenuItem(current);
        current.setTitle(roomName);
        current.setIcon(roomIcon);

    }

}
