package com.example.cuma.tinder.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.cuma.tinder.Fragment.CanFragment;
import com.example.cuma.tinder.Fragment.ElmasFragment;
import com.example.cuma.tinder.Fragment.ParaFragment;
import com.example.cuma.tinder.Adapter.MoneyAdapter;
import com.example.cuma.tinder.R;


public class MoneyActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MoneyAdapter moneyAdapter;
    private TabLayout tablaLayout;
    private Toolbar toolbar;
    private int[] tabicons = {R.drawable.kalp, R.drawable.coins, R.drawable.elmas};
    private ImageView back;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        moneyAdapter = new MoneyAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tablaLayout = (TabLayout) findViewById(R.id.tabs);
        tablaLayout.setupWithViewPager(viewPager);
        setupTabıcons();

        toolbar = (Toolbar) findViewById(R.id.toolbar_satinal);
        setSupportActionBar(toolbar);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {
        MoneyAdapter adapter = new MoneyAdapter(getSupportFragmentManager());
        adapter.addFragment(new CanFragment(), "Tab1");
        adapter.addFragment(new ParaFragment(), "Para");
        adapter.addFragment(new ElmasFragment(), "Elmas");
        viewPager.setAdapter(adapter);

    }

    private void setupTabıcons() {
        tablaLayout.getTabAt(0).setIcon(tabicons[0]);
        tablaLayout.getTabAt(1).setIcon(tabicons[1]);
        tablaLayout.getTabAt(2).setIcon(tabicons[2]);
    }
}
