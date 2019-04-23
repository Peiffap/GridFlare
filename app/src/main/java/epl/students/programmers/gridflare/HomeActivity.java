package epl.students.programmers.gridflare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    ViewPager vp;
    BottomNavigationView navigationView;
    int oldItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        navigationView = findViewById(R.id.navigation_view);
        oldItem = 0;
        configureVP();
    }

    private void configureVP(){
        vp = findViewById(R.id.view_pager);
        vp.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(oldItem).setChecked(false);
                navigationView.getMenu().getItem(position).setChecked(true);
                oldItem = position;
            }
        });

        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.live_menu_btn:
                                vp.setCurrentItem(0);
                                break;
                            case R.id.main_menu_btn:
                                vp.setCurrentItem(1);
                                break;
                            case R.id.scan_menu_btn:
                                vp.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });
    }

    public void onClickSwitch(View v) {
        MenuActivity page;//////
        switch (v.getId()) {
            case R.id.delete_room_btn:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.deleteRoom(v);
                break;
            case R.id.d_room_template:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.displayRoomData(v);
                break;
            case R.id.d_place_name:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.startGlobalScan(v);
                break;
            case R.id.share_btn:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.shareButton(v);
                break;
            case R.id.delete_place_btn:
                //TODO
                break;
            case R.id.new_room_btn:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.newRoomPopup(v);
                break;
            case R.id.validate_new_room_btn:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.validateNewRoom(v);
                break;
            case R.id.cancel_new_room_btn:
                page = (MenuActivity)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + vp.getCurrentItem());
                page.cancelNewRoom(v);
                break;
        }
    }
}
