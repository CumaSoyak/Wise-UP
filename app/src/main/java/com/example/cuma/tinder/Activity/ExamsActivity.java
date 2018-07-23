package com.example.cuma.tinder.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.cuma.tinder.Class.Profile;
import com.example.cuma.tinder.Class.PuanHesapla;
import com.example.cuma.tinder.R;
import com.example.cuma.tinder.TinderCard;
import com.example.cuma.tinder.Utils;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

public class ExamsActivity extends AppCompatActivity {

    //tam 0. sanıyede basarsam 3 can kaldı dıyor
    //Eğer popupta son ana kadar beklersem puan ve can değişiyor
    //custom alert dalog yapılacak

    //home // Restart // Devam et  Decam et kısmı düzelecek fakat çıkıç yapmak isterse görünecek

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Profile mProfile;
    private Toolbar toolbar;
    public Utils utils;
    public CountDownTimer countDownTimer;
    public TextView time;
    private ImageButton like,dislike;
    public PuanHesapla puanHesapla=new PuanHesapla(5);
    public Dialog dialog;
    public AlertDialog alertDialog;
    int dogrucevapsayisi;
    int cevapsira;
    int kirik_kalp;
    ImageView kirik_kalp_image1,kirik_kalp_image2,kirik_kalp_image3;
    public ArrayList<String> cevaplistesi = new ArrayList<String>();

    private static final String TAG = "ExamsActivity";
    public int  quiz;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = this;
        time = (TextView) findViewById(R.id.time);
        like=(ImageButton)findViewById(R.id.like_img);
        dislike=(ImageButton)findViewById(R.id.dislike_img);
        kirik_kalp_image1=(ImageView)findViewById(R.id.kalp1);
        kirik_kalp_image2=(ImageView)findViewById(R.id.kalp2);
        kirik_kalp_image3=(ImageView)findViewById(R.id.kalp3);
        mProfile = new Profile("Bu da bir cevap");//Burda örnek var buraya listeden çekmem lazım

        getCountDownTimer();// Timer kullanımını gerçekleştirdik
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quiz = getIntent().getIntExtra(MainActivity.sorukey, MainActivity.tarih);
        dialog = new Dialog(this, R.style.DialogNotitle);

        utils = new Utils();
        puanHesapla = new PuanHesapla(dogrucevapsayisi);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3) //Burası görünümde kaç kart göstereceği pek alakası yok
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f));

        // .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)           //Yana kaydırırken mesajın doğru oldğunu
        // .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));       //Yana kaydırırken mesajın yanlış olduğunu yazıyoruz


        for (Profile deger : Utils.loadProfiles(this.getApplicationContext(),quiz)) {       //her bir elemanda gez degere at : nerde gezineceksin
            mSwipeView.addView(new TinderCard(mContext, deger, mSwipeView, quiz));

            cevaplistesi.add(deger.getAnswer());
        }


        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
                String hayir = "Hayır";
                if (cevaplistesi.get(cevapsira).equals(hayir)) { puanHesapla.puanarti();
                }
                else {
                    kirik_kalp++;
                    if (kirik_kalp==1){ kirik_kalp_image1.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==2){ kirik_kalp_image2.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ kirik_kalp_image3.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ ShowPop(); } }

                cevapsira++;
                Log.i("KirikKalp",": :"+kirik_kalp);
            }
        });
        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
                String evet = "Evet";
                if (cevaplistesi.get(cevapsira).equals(evet)) { puanHesapla.puanarti();
                    //Log.i("Evet Puan",":",puanHesapla.puanarti());
                }
                else{
                       kirik_kalp++;
                    if (kirik_kalp==1){ kirik_kalp_image1.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==2){ kirik_kalp_image2.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ kirik_kalp_image3.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ ShowPop(); }
                }
                cevapsira++;
            }

        });
    }
    public ArrayList<String> listedondur() {
        return cevaplistesi;

    }
    public int cevapsiradonder(){
        return cevapsira;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_main, menu);
        Log.i("Menu", "goster");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   // Ayarlar bölumu
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                // Toast.makeText(Exams.this,"Ayarlara Tıkladınız",Toast.LENGTH_LONG).show();
                break;
            case R.id.skor:
                Toast.makeText(ExamsActivity.this, "Skora tıkladınız", Toast.LENGTH_LONG).show();
                Intent ıntent = new Intent(ExamsActivity.this, SkorActivity.class);
                startActivity(ıntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public CountDownTimer getCountDownTimer() {
        countDownTimer = new CountDownTimer(16900, 1000) { //Burdaki saniye 49 olması lazım
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                ShowPop();
            }
        }.start();

        return countDownTimer;
    }

    public void ShowPop() {    //Zaman bittiğinde kazanılan altın ve elmasları gösteriyoruz

        TextView textclose, kategori_text, popup_para, popup_can, popup_elmas;
        ImageButton home,again,devam;
        ImageView kategori_image;

        dialog.setContentView(R.layout.win_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        home = (ImageButton) dialog.findViewById(R.id.popup_home);
        again=(ImageButton)dialog.findViewById(R.id.popoup_restart);
        devam=(ImageButton)dialog.findViewById(R.id.popup_play);
        kategori_image = (ImageView) dialog.findViewById(R.id.kategori_image);
        kategori_text = (TextView) dialog.findViewById(R.id.kategori_popup);

        popup_can = (TextView) dialog.findViewById(R.id.popup_can);
        popup_para = (TextView) dialog.findViewById(R.id.pop_para);
        popup_elmas = (TextView) dialog.findViewById(R.id.pop_elmas);
        Log.i("Dogru", ":" + String.valueOf(puanHesapla.puanarti()));
        if (puanHesapla.puanarti() <= 0) {
            popup_para.setText(String.valueOf(puanHesapla.puanarti()));
            popup_elmas.setText(String.valueOf(puanHesapla.puanarti()));
        }
        popup_para.setText(String.valueOf(puanHesapla.puanarti()));
        popup_elmas.setText(String.valueOf(puanHesapla.puanarti()));
        Log.i("Kalpp",":"+kirik_kalp);
        if (kirik_kalp==0){popup_can.setText("3");}
        else if (kirik_kalp==1){popup_can.setText("2");}
        else if (kirik_kalp==2){popup_can.setText("1");}
        else if (kirik_kalp==3){popup_can.setText("0");}

      //  kirik_kalp=0;

        switch (quiz) {
            case MainActivity.tarih:
                kategori_image.setImageResource(R.drawable.tarihim);
                kategori_text.setText("Tarih");
                break;
            case MainActivity.bilim:
                kategori_image.setImageResource(R.drawable.bilim);
                kategori_text.setText("Bilim");
                break;
            case MainActivity.eglence:
                kategori_image.setImageResource(R.drawable.eglence);
                kategori_text.setText("Eğlence");
                break;
            case MainActivity.cografya:
                kategori_image.setImageResource(R.drawable.cografya);
                kategori_text.setText("Dünya");
                break;
            case MainActivity.sanat:
                kategori_image.setImageResource(R.drawable.sanat);
                kategori_text.setText("Sanat");
                break;
            case MainActivity.spor:
                kategori_image.setImageResource(R.drawable.spor);
                kategori_text.setText("Spor");
                break;

        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ıntent);
            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent=new Intent(getApplicationContext(),ExamsActivity.class);
                ıntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ıntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(ıntent);
            }
        });
        devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Burası düzelecek
            }
        });
        if (!isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {              //Çıkış yapmak istiyormmusun diye soruyoruz
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.moodbad)
                .setTitle("Dikkat")
                .setMessage("Çıkmak istediğine eminmisin ?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Hayır", null)
                .show();
    }





}
