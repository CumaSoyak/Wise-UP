package com.example.cuma.tinder.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;


import com.example.cuma.tinder.R;

import java.util.Random;

public class MainFragment extends Fragment implements Animation.AnimationListener {
    Random random;
    public int random_sayi;
    Animation animation;
    CardView cardView1, cardView2, cardView3, cardView4, cardView_trans;
    ImageView checked, checked1, checked2, checked3, checked4, checked5, checked6, isaret_oku;
    Button button;
    int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        button = (Button) view.findViewById(R.id.baslat);
        isaret_oku = (ImageView) view.findViewById(R.id.isaret_oku);
        animation=AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
        animation.setAnimationListener(MainFragment.this);

        cardView1 = (CardView) view.findViewById(R.id.card1);
        cardView2 = (CardView) view.findViewById(R.id.card2);
        cardView3 = (CardView) view.findViewById(R.id.card3);
        cardView4 = (CardView) view.findViewById(R.id.card4);
        checked1 = (ImageView) view.findViewById(R.id.kategori_image_checked1);
        checked2 = (ImageView) view.findViewById(R.id.kategori_image_checked2);
        checked3 = (ImageView) view.findViewById(R.id.kategori_image_checked3);
        checked4 = (ImageView) view.findViewById(R.id.kategori_image_checked4);
        checked5 = (ImageView) view.findViewById(R.id.kategori_image_checked5);
        checked6 = (ImageView) view.findViewById(R.id.kategori_image_checked6);

        //TODO silinecek experimental
        setHasOptionsMenu(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random = new Random();
                random_sayi = 1 + random.nextInt(6);
                isaret_oku.startAnimation(animation);
            }
        });



        return view;
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.i("Random", ":" + random_sayi);

        switch (random_sayi) {
            case 1:
                isaret_oku.setRotation(315); //Eğer tarihse
                checked1.setImageResource(R.drawable.checked);
                show_pup();
                break;
            case 2:
                isaret_oku.setRotation(0);//Bilim
                checked2.setImageResource(R.drawable.checked);
                break;
            case 3:
                isaret_oku.setRotation(45);//Eğlence
                checked3.setImageResource(R.drawable.checked);
                break;
            case 4:
                isaret_oku.setRotation(225);//Coğrafya
                checked4.setImageResource(R.drawable.checked);
                break;
            case 5:
                isaret_oku.setRotation(180);//Sanat
                checked5.setImageResource(R.drawable.checked);
                break;
            case 6:
                isaret_oku.setRotation(135);//Sporsa
                checked6.setImageResource(R.drawable.checked);
                break;
        }
    }
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    private void show_pup() {



    }
}
