package com.example.cuma.tinder.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class MoneyAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mfragmentlist= new ArrayList<>();
        private  final  List<String> mfragmentTitles=new ArrayList<>();

    public  void addFragment(Fragment fragment, String title){
        mfragmentlist.add(fragment);
        mfragmentTitles.add(title);
    }
    public MoneyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mfragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mfragmentlist.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return null;//        // sadece icon istiyorsak return null yapmak yeterli

    }
}
