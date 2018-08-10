package com.example.cuma.tinder.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.cuma.tinder.Adapter.MainAdapter;
import com.example.cuma.tinder.Fragment.HesapFragment;
import com.example.cuma.tinder.Fragment.KupaFragment;
import com.example.cuma.tinder.Fragment.MainFragment;
import com.example.cuma.tinder.Fragment.OneriFragment;
import com.example.cuma.tinder.Fragment.ShopFragment;
import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;


    private ViewPager viewPager;
    private MainAdapter mainAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int[] tabicons = {R.mipmap.house, R.mipmap.alisveris, R.mipmap.kazanan, R.mipmap.soru_oneri, R.mipmap.menu};

    EditText al_kullanici_adi;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

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
        tabLayout.getTabAt(3).setIcon(tabicons[3]);
        tabLayout.getTabAt(4).setIcon(tabicons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "dasd");
        adapter.addFragment(new ShopFragment(), "dasd");
        adapter.addFragment(new KupaFragment(), "dasd");
        adapter.addFragment(new OneriFragment(), "Öneri");
        adapter.addFragment(new HesapFragment(), "hesap");
        viewPager.setAdapter(adapter);
    }

    public void moneyclick(View view) {
        Intent ıntent = new Intent(getApplicationContext(), MoneyActivity.class);
        startActivity(ıntent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
