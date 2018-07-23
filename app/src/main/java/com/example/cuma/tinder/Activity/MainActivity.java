package com.example.cuma.tinder.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.cuma.tinder.Adapter.MainAdapter;
import com.example.cuma.tinder.Fragment.KupaFragment;
import com.example.cuma.tinder.Fragment.MainFragment;
import com.example.cuma.tinder.Fragment.ShopFragment;
import com.example.cuma.tinder.R;

public class MainActivity extends AppCompatActivity {
    public static final String sorukey = "key";
    public static final int tarih = 1;
    public static final int bilim = 2;
    public static final int eglence = 3;
    public static final int cografya = 4;
    public static final int sanat = 5;
    public static final int spor = 6;

    private ViewPager viewPager;
    private MainAdapter mainAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int[] tabicons = {R.drawable.home, R.drawable.market, R.drawable.kupa};


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainAdapter = new MainAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs_main);
        tabLayout.setupWithViewPager(viewPager);
        setupTabıcons();

      /*  viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    private void setupTabıcons() {
        tabLayout.getTabAt(0).setIcon(tabicons[0]);
        tabLayout.getTabAt(1).setIcon(tabicons[1]);
        tabLayout.getTabAt(2).setIcon(tabicons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "dasd");
        adapter.addFragment(new ShopFragment(), "dasd");
        adapter.addFragment(new KupaFragment(), "dasd");
        viewPager.setAdapter(adapter);
    }


    public void moneyclick(View view) {
        Intent ıntent = new Intent(getApplicationContext(), MoneyActivity.class);
        startActivity(ıntent);
    }

    public void cardviewclick(View view) {
        Intent ıntent = new Intent(getApplicationContext(), ExamsActivity.class);

        switch (view.getId()) {

            case R.id.card1:
                ıntent.putExtra(sorukey, tarih);
                startActivity(ıntent);
                break;
            case R.id.card2:
                ıntent.putExtra(sorukey, bilim);
                startActivity(ıntent);
                break;
            case R.id.card3:
                ıntent.putExtra(sorukey, eglence);
                startActivity(ıntent);
                break;
            case R.id.card4:
                ıntent.putExtra(sorukey, cografya);
                startActivity(ıntent);
                break;
            case R.id.card5:
                ıntent.putExtra(sorukey, sanat);
                startActivity(ıntent);
                break;
            case R.id.card6:
                ıntent.putExtra(sorukey, spor);
                startActivity(ıntent);
                break;

        }

    }


}
