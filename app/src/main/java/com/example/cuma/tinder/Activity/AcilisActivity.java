package com.example.cuma.tinder.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cuma.tinder.Fragment.MainFragment;
import com.example.cuma.tinder.R;
import com.facebook.login.widget.LoginButton;

import java.net.InetAddress;


public class AcilisActivity extends AppCompatActivity {

    ImageView image1, image2, image3, image4, image5, image6;
    Dialog kontrol;
    MediaPlayer mediaPlayer;
    Intent ıntent;
    Animation animation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        ıntent = new Intent(this, LoginActivity.class);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mediaPlayer = MediaPlayer.create(AcilisActivity.this, R.raw.acilis);
        mediaPlayer.start();
        if (networkConnection()) {//TODO burayı normal hale getir
            zaman_durdur();
        } else {
            internet_dialog();
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.bluedark));


    }

    public void zaman_durdur() {
        image1.startAnimation(animation);
        image2.startAnimation(animation);
        image3.startAnimation(animation);
        image4.startAnimation(animation);
        image5.startAnimation(animation);
        image6.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(ıntent);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void internet_dialog() {
        kontrol = new Dialog(this, R.style.DialogNotitle);
        kontrol.setContentView(R.layout.dialog_internet);
        kontrol.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        kontrol.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        kontrol.setCancelable(false);
        ImageButton yenile_buton = (ImageButton) kontrol.findViewById(R.id.yenile_buton);
        kontrol.show();
        yenile_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

    }

    public boolean networkConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
