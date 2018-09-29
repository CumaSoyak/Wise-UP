package com.example.cuma.tinder.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.cuma.tinder.R;
import com.facebook.login.widget.LoginButton;

import java.net.InetAddress;


public class AcilisActivity extends AppCompatActivity {

    ImageView image1,image2,image3,image4,image5,image6;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis);

        image1=(ImageView)findViewById(R.id.image1);
        image2=(ImageView)findViewById(R.id.image2);
        image3=(ImageView)findViewById(R.id.image3);
        image4=(ImageView)findViewById(R.id.image4);
        image5=(ImageView)findViewById(R.id.image5);
        image6=(ImageView)findViewById(R.id.image6);

       /* if (internet_kontrol() == false) {//TODO burayı normal hale getir
            internet_dialog();

        }
        else {
            zaman_durdur();
        } */
        zaman_durdur();
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.bluedark));



    }
    public void zaman_durdur(){

        Animation animation =AnimationUtils.loadAnimation(this,R.anim.rotate);
        image1.startAnimation(animation);
        image2.startAnimation(animation);
        image3.startAnimation(animation);
        image4.startAnimation(animation);
        image5.startAnimation(animation);
        image6.startAnimation(animation);
        final Intent ıntent=new Intent(this,LoginActivity.class);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(ıntent);
                    finish();

                }
            }
        };
        timer.start();
    }

    public void internet_dialog() {
        Dialog kontrol = new Dialog(this, R.style.DialogNotitle);
        kontrol.setContentView(R.layout.dialog_internet);
        kontrol.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        kontrol.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        kontrol.setCancelable(false);
        kontrol.show();

    }
    public boolean internet_kontrol() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
