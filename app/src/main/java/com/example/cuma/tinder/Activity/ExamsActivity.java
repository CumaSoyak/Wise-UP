package com.example.cuma.tinder.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.cuma.tinder.Class.Profile;
import com.example.cuma.tinder.Class.PuanHesapla;
import com.example.cuma.tinder.Class.Sorular;
import com.example.cuma.tinder.Fragment.MainFragment;
import com.example.cuma.tinder.R;
import com.example.cuma.tinder.TinderCard;
import com.example.cuma.tinder.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

public class ExamsActivity extends AppCompatActivity {

    //Eğer popupta son ana kadar beklersem puan ve can değişiyor
    //TODO dogru cevap sayısı fazladan değer geliyor
    // TODO //home // Restart // Devam et  Decam et kısmı düzelecek fakat çıkıç yapmak isterse görünecek
    //TODO uygulama açılacağı zaman status bar önce sarı sonra siyah oluyor
    //TODO doğru sayısımı ve yanlış sayısını farklı tutmak lazım
    //todo multıplayer olacak
    //todo ses felanda olabilir
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String useremail;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Profile mProfile;
    private Sorular mSorular;
    private Toolbar toolbar;
    public Utils utils;
    public CountDownTimer countDownTimer;
    public TextView time;
    public ImageButton like, dislike;
    private Button evet_buton, hayir_buton;
    public PuanHesapla puanHesapla;
    public Dialog dialog;
    public int evetsayisi, hayirsayisi;
    public int cevapsira, kirik_kalp = 0;
    ImageView kirik_kalp_image1, kirik_kalp_image2, kirik_kalp_image3;
    private long time_hatırla = 0;
    public ArrayList<String> cevaplistesi = new ArrayList<>();
    public ArrayList<Sorular> sorularList = new ArrayList<Sorular>();
    int para_topla;
    int gelen_kalp, gelen_para, gelen_elmas, kalp, para, elmas;

    public int dinle = 0;
    private static final String TAG = "ExamsActivity";
    public int quiz;

    public int getEvetsayisi() {
        return evetsayisi;
    }

    public void setEvetsayisi(int evetsayisi) {
        this.evetsayisi = evetsayisi;
    }

    public int getHayirsayisi() {
        return hayirsayisi;
    }

    public void setHayirsayisi(int hayirsayisi) {
        this.hayirsayisi = hayirsayisi;
    }

    public void incEvetsayisi() {
        this.evetsayisi += 1;
    }

    public void incHayirsayisi() {
        this.hayirsayisi += 1;
    }

    public void descEvetsayisi() {
        this.evetsayisi -= 1;
    }

    public void descHayirsayisi() {
        this.hayirsayisi -= 1;
    }

    //////////////
    public int getKirik_kalp() {
        return kirik_kalp;
    }

    public void setKirik_kalp(int kirik_kalp) {
        this.kirik_kalp = kirik_kalp;
    }

    public void artir_kalp_sayisi() {
        this.kirik_kalp += 1;
    }


    public ArrayList<String> getCevaplistesi() {
        return cevaplistesi;
    }

    public String getCurrentAnswer() {
        return cevaplistesi.get(cevapsira);
    }

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();


        MobileAds.initialize(this, "ca-app-pub-7740710689946524~4712663337");
        mAdView = findViewById(R.id.adView_exams);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#2f333f"));


        TinderCard tinderCard = new TinderCard();

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = this;
        time = (TextView) findViewById(R.id.time);
        like = (ImageButton) findViewById(R.id.like_img);
        dislike = (ImageButton) findViewById(R.id.dislike_img);
        evet_buton = findViewById(R.id.acceptBtn);
        hayir_buton = findViewById(R.id.rejectBtn);
        kirik_kalp_image1 = (ImageView) findViewById(R.id.kalp1);
        kirik_kalp_image2 = (ImageView) findViewById(R.id.kalp2);
        kirik_kalp_image3 = (ImageView) findViewById(R.id.kalp3);

        getCountDownTimer();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        quiz = getIntent().getIntExtra(MainFragment.sorukey, MainFragment.tarih);
        dialog = new Dialog(this, R.style.DialogNotitle);
        gecis_reklam();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f));
        mSwipeView.disableTouchSwipe();

        // .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
        // .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
    /*   for (Profile deger : Utils.loadProfiles(this.getApplicationContext(), quiz)) {
            mSwipeView.addView(new TinderCard(mContext, deger, mSwipeView, quiz));
            cevaplistesi.add(deger.getAnswer());
        }*/
        Log.i("Dil", ":" + Locale.getDefault().getDisplayLanguage());

        if (Locale.getDefault().getLanguage().equals("tr")) {
            soru_ve_cevapları_getir_turkce();

        } else {
            soru_ve_cevapları_getir_ingilizce();

        }
        puanlargetir();
        evet_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
                String evet = "Yes";
                if (getCurrentAnswer().equalsIgnoreCase(evet)) {
                    incEvetsayisi();

                } else {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(250);

                    Log.i("Kalp_Sayısı", ":" + getKirik_kalp());
                    descEvetsayisi();
                    artir_kalp_sayisi();//todo sadece kalbi bir kez kırıyor
                    kalp_patlat();

                }
                cevapsira++;
                cevabı_beklet();
                Log.i("EveteTıklandı", ":");
            }
        });

        hayir_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
                String hayir = "No";
                if (getCurrentAnswer().equalsIgnoreCase(hayir)) {
                    incHayirsayisi();

                } else {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(250);
                    descHayirsayisi();
                    artir_kalp_sayisi();
                    kalp_patlat();
                }
                cevapsira++;
                Log.i("KirikKalp", ": :" + kirik_kalp);
                cevabı_beklet();
            }
        });


    }


    public void soru_ve_cevapları_getir_ingilizce() {
        databaseReference.child("Sorular_ingilizce").child(String.valueOf(quiz)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mSorular = ds.getValue(Sorular.class);
                    sorularList.add(mSorular);
                }
                Log.i("Sorular_listesi", ":" + mSorular.getCevap());
                Collections.shuffle(sorularList);
                for (Sorular sorular : sorularList) {
                    mSwipeView.addView(new TinderCard(mContext, sorular, mSwipeView, quiz));
                    cevaplistesi.add(sorular.getCevap());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void soru_ve_cevapları_getir_turkce() {
        databaseReference.child("Sorular_turkce").child(String.valueOf(quiz)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mSorular = ds.getValue(Sorular.class);
                    sorularList.add(mSorular);
                }
                Log.i("Sorular_listesi", ":" + mSorular.getCevap());
                Collections.shuffle(sorularList);
                for (Sorular sorular : sorularList) {
                    mSwipeView.addView(new TinderCard(mContext, sorular, mSwipeView, quiz));
                    cevaplistesi.add(sorular.getCevap());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void kalp_patlat() {
        Log.i("Kalp_Sayısı", ":" + getKirik_kalp());
        switch (getKirik_kalp()) {
            case 1:
                kirik_kalp_image1.setImageResource(R.drawable.kirikalp);
                Log.i("Kırık_Kalp", "1");
                break;
            case 2:
                kirik_kalp_image2.setImageResource(R.drawable.kirikalp);
                Log.i("Kırık_Kalp", "2");
                break;
            case 3:
                kirik_kalp_image3.setImageResource(R.drawable.kirikalp);
                Log.i("Kırık_Kalp", "3");
                ShowPop();
                break;
        }
    }

    public ArrayList<String> listedondur() {
        return cevaplistesi;

    }

    //todo gif eklense daha iyi olur
    public int cevapsiradonder() {
        return cevapsira;
    }

    public CountDownTimer getCountDownTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) { //Burdaki saniye 49 olması lazım
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(millisUntilFinished / 1000).toString());
                time_hatırla = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Log.i("Saat_degersiz", ":");
                time.setText("0");
                ShowPop();
            }
        }.start();

        return countDownTimer;
    }

    public void ShowPop() {    //Zaman bittiğinde kazanılan altın ve elmasları gösteriyoruz
        countDownTimer.cancel();
        TextView textclose, kategori_text, popup_para, popup_can, popup_elmas;
        ImageButton home, again;
        ImageView kategori_image;
        dialog.setContentView(R.layout.win_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        home = (ImageButton) dialog.findViewById(R.id.popup_home);
        again = (ImageButton) dialog.findViewById(R.id.popoup_restart);
        kategori_image = (ImageView) dialog.findViewById(R.id.kategori_image);
        kategori_text = (TextView) dialog.findViewById(R.id.kategori_text);

        popup_can = (TextView) dialog.findViewById(R.id.pop_can);
        popup_para = (TextView) dialog.findViewById(R.id.pop_para);
        popup_elmas = (TextView) dialog.findViewById(R.id.pop_elmas);
        Log.i("Dogru", ":" + String.valueOf(getEvetsayisi()));
        if (puanHesapla.puanarti() <= 0) {
            popup_para.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
            popup_elmas.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
        }
        int cevir = (getEvetsayisi() + getHayirsayisi()) / 10;
        popup_para.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
        popup_elmas.setText(String.valueOf(cevir));
        Log.i("Kalpp", ":" + kirik_kalp);
        if (kirik_kalp == 0) {
            popup_can.setText("3");
        } else if (kirik_kalp == 1) {
            popup_can.setText("2");
        } else if (kirik_kalp == 2) {
            popup_can.setText("1");
        } else if (kirik_kalp == 3) {
            popup_can.setText("0");
        }
        //  kirik_kalp=0;

        ekleveritabani();
        switch (quiz) {
            case 1:
                kategori_image.setImageResource(R.drawable.tarih);
                kategori_text.setText(getResources().getString(R.string.savas_baslik_tarih));
                break;
            case 2:
                kategori_image.setImageResource(R.drawable.bilim_pop);
                kategori_text.setText(getResources().getString(R.string.savas_baslik_bilim));
                break;
            case 3:
                kategori_image.setImageResource(R.drawable.eglence_pop);
                kategori_text.setText(getResources().getString(R.string.savas_baslik_eglence));
                break;
            case 4:
                kategori_image.setImageResource(R.drawable.cografya_pop);
                kategori_text.setText(getResources().getString(R.string.savas_baslik_cografya));
                break;
            case 5:
                kategori_image.setImageResource(R.drawable.sanat_pop);
                kategori_text.setText(getResources().getString(R.string.savas_baslik_sanat));
                break;
            case 6:
                kategori_image.setImageResource(R.drawable.spor_pop);
                kategori_text.setText(getResources().getString(R.string.savas_baslik_spor));
                break;

        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);


            }
        });

        if (!isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        //TODO süre durduktan sonra devam etmesi lazım
        final Dialog cikis_dialog = new Dialog(this, R.style.DialogNotitle);
        cikis_dialog.setContentView(R.layout.dialog_cikis);
        cikis_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cikis_dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        // countDownTimer.cancel();
        Button cikis_hayir = (Button) cikis_dialog.findViewById(R.id.dialog_cikis_hayir);
        Button cikis_evet = (Button) cikis_dialog.findViewById(R.id.dialog_cikis_evet);
        cikis_hayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_dialog.dismiss();
            }
        });
        cikis_evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_dialog.dismiss();//Anasayfaya dönmeden önce dialogu kapatmak lazım
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                finish();
            }
        });
        cikis_dialog.show();
    }

    public void cevabı_beklet() {
        evet_buton.setEnabled(false);
        hayir_buton.setEnabled(false);
        int bekletsure = 400;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                evet_buton.setEnabled(true);
                hayir_buton.setEnabled(true);

            }
        }, bekletsure);
    }

    public void puanlargetir() {
        databaseReference.child("Puanlar").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                gelen_para = dataSnapshot.child("para").getValue(Integer.class);
                gelen_elmas = dataSnapshot.child("elmas").getValue(Integer.class);
                gelen_kalp = dataSnapshot.child("kalp").getValue(Integer.class);
                cagir();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void cagir() {
        para = gelen_para;
        elmas = gelen_elmas;
        kalp = gelen_kalp;
    }

    public void ekleveritabani() {
        user = firebaseAuth.getCurrentUser();
        useremail = user.getEmail().toString();
        UUID uuıd = UUID.randomUUID();
        String uuidString = uuıd.toString();

        if (kalp != 0) {
            if (kirik_kalp == 3) {
                databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(kalp - 1);
            }
        }
        databaseReference.child("Puanlar").child(user_id).child("useremail").setValue(useremail);
        databaseReference.child("Puanlar").child(user_id).child("para").setValue(getEvetsayisi() + getHayirsayisi() + para);//todo para değeri buraya gelmesi lazım
        int cevir = (getEvetsayisi() + getHayirsayisi()) / 10;
        Log.i("Cevir", ":" + cevir);
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(cevir + elmas);
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(100000 - (elmas + para + getEvetsayisi() + getHayirsayisi()));//Todo burda sadece göstermelik için 10 ile çarptım
        databaseReference.child("Yarisma").child(user_id).child("puan").setValue(elmas + para + getEvetsayisi() + getHayirsayisi());//Todo burda sadece göstermelik için 10 ile çarptım


    }

    public void gecis_reklam() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7740710689946524/1206061969");
        AdRequest request = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(request);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ıntent);
            }

        });

    }


}
