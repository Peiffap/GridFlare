package epl.students.programmers.gridflare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager manager){
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return LiveScanningActivity.newInstance();
            case 1:
                return MenuActivity.newInstance();
            case 2:
                return NewScanActivity.newInstance();
        }
        return null;//A supprimer
    }

    @Override
    public int getCount() {
        return 3;
    }

}
