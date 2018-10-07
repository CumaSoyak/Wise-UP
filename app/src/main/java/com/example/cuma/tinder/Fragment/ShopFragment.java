package com.example.cuma.tinder.Fragment;

import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.cuma.tinder.Activity.MainActivity;
import com.example.cuma.tinder.Adapter.MoneyAdapter;
import com.example.cuma.tinder.Adapter.SatinalAdapter;
import com.example.cuma.tinder.Class.Satinal;
import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private ViewPager viewPager;
    private MoneyAdapter moneyAdapter;
    private TabLayout tablaLayout;
    private Toolbar toolbar;
    private int[] tabicons = {R.drawable.kalp, R.drawable.coins, R.drawable.elmas};
    Integer gelen_kalp, gelen_para, gelen_elmas;
    int kalp = 3, para, elmas;
    private CardView cardView;
    private ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();


        moneyAdapter = new MoneyAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tablaLayout = view.findViewById(R.id.tabs);
        tablaLayout.setupWithViewPager(viewPager);
        setupTabıcons();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.toolbar_satinal);
        activity.setSupportActionBar(toolbar);

        cardView = view.findViewById(R.id.cardview_satinal);
        constraintLayout = view.findViewById(R.id.constraint_satinal);


        return view;
    }

    public void Firebase_get_data() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Todo kalbe veriyi toplam kalp puanı felan br şey olması lazım satın aldığı kadar veya her 24 saatte 5 tane felan
                gelen_kalp = dataSnapshot.child("Puanlar").child(user_id).child("kalp").getValue(Integer.class);
                gelen_para = dataSnapshot.child("Puanlar").child(user_id).child("para").getValue(Integer.class);
                gelen_elmas = dataSnapshot.child("Puanlar").child(user_id).child("elmas").getValue(Integer.class);
                cagir();

                //Database de değişiklik olursa ne yapayım
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cagir() {
        kalp = gelen_kalp;
        para = gelen_para;
        elmas = gelen_elmas;

    }

    private void setupViewPager(ViewPager viewPager) {

        MoneyAdapter adapter = new MoneyAdapter(getChildFragmentManager());
        adapter.addFragment(new CanFragment(), String.valueOf(kalp));
        adapter.addFragment(new ParaFragment(), String.valueOf(para));
        adapter.addFragment(new ElmasFragment(), String.valueOf(elmas));
        viewPager.setAdapter(adapter);

    }

    private void setupTabıcons() {
        tablaLayout.getTabAt(0).setIcon(tabicons[0]);
        tablaLayout.getTabAt(1).setIcon(tabicons[1]);
        tablaLayout.getTabAt(2).setIcon(tabicons[2]);
    }


}




































/*
 */