package com.example.cuma.tinder.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuma.tinder.Class.Etkinler;
import com.example.cuma.tinder.Class.Profile;
import com.example.cuma.tinder.Class.PuanHesapla;
import com.example.cuma.tinder.Class.Sorular;
import com.example.cuma.tinder.Fragment.MainFragment;
import com.example.cuma.tinder.MeydanOku_TinderCard;
import com.example.cuma.tinder.R;
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
import java.util.Random;
import java.util.UUID;

public class Meydan_OkuActivity extends AppCompatActivity {
    //todo kaybedince can eksilmiyor
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
    Etkinler etkinler;
    private Toolbar toolbar;
    public Utils utils;
    public CountDownTimer countDownTimer;
    public TextView time;
    private TextView kullanici_bir, kullanici_iki;
    private ImageButton like, dislike;
    private Button evet_buton, hayir_buton;
    public PuanHesapla puanHesapla;
    public Dialog dialog;
    private MediaPlayer mediaPlayer;

    public int evetsayisi, hayirsayisi;
    public int cevapsira, kirik_kalp = 0;
    ImageView kirik_kalp_image1, kirik_kalp_image2, kirik_kalp_image3;
    private long time_hatırla = 0;
    public ArrayList<String> cevaplistesi = new ArrayList<>();
    public ArrayList<Sorular> sorularList = new ArrayList<Sorular>();
    public ArrayList<Etkinler> kullanıcılar_listesi = new ArrayList<Etkinler>();//todo kapasite ayarlamam lazım
    private TextView kullanıcı_bir_popup, kullanıcı_iki_popup;

    int gelen_kalp, gelen_para, gelen_elmas, kalp, para, elmas;

    public int dinle = 0;
    private static final String TAG = "ExamsActivity";
    public int meydanoku_quiz;
    Intent kullanici_adim_Intent;
    public String kullanici_adim;
    private String kullanici_iki_for, kullanici_iki_getir;
    private int kartsayısı = 0;
    private int random_sayi_kaybetmek, random_sayi_kazanmak, karsı_taraf_kaybetme;

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
        setContentView(R.layout.activity_meydan_oku);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();


        MobileAds.initialize(this, "ca-app-pub-7740710689946524~4712663337");
        mAdView = findViewById(R.id.adView_meydanoku);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        MeydanOku_TinderCard tinderCard = new MeydanOku_TinderCard();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar_meydan_oku);
        setSupportActionBar(toolbar);
        //////////// oda ismi
        kullanici_adim_Intent = getIntent();
        Bundle bundle = kullanici_adim_Intent.getExtras();
        kullanici_adim = (String) bundle.get("kullanici_adim");
        Log.i("kullanici_adim", ":" + kullanici_adim); //todo oda ismi gelmiyor
        ///////////////

        meydanoku_quiz = getIntent().getIntExtra(MainFragment.sorukey, MainFragment.tarih);
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

        Random random_karsı = new Random();
        karsı_taraf_kaybetme = random_karsı.nextInt(10 - 4) + 4;
        Log.i("Karsı_taraf_kaybetme", ":" + karsı_taraf_kaybetme);
        kullanıcılarıyazdır();
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
                    //descEvetsayisi();
                    artir_kalp_sayisi();//todo sadece kalbi bir kez kırıyor
                    kalp_patlat();

                }
                cevapsira++;
                cevabı_beklet();
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
                    //descHayirsayisi();
                    artir_kalp_sayisi();
                    kalp_patlat();
                }
                cevapsira++;
                Log.i("KirikKalp", ": :" + kirik_kalp);
                cevabı_beklet();
            }
        });


    }

    private void kullanıcılarıyazdır() {
        kullanici_bir = (TextView) findViewById(R.id.kullanici_bir);
        kullanici_iki = (TextView) findViewById(R.id.kullanici_iki);
        UUID uuıd = UUID.randomUUID();
        final String uuidString = uuıd.toString();
        databaseReference.child("Kullanıcı_Adı").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    etkinler = datas.getValue(Etkinler.class);
                    kullanıcılar_listesi.add(etkinler);
                    Collections.shuffle(kullanıcılar_listesi);
                }
                for (Etkinler etkinler1 : kullanıcılar_listesi) {
                    kullanici_bir.setText(kullanici_adim);
                    kullanici_iki.setText(etkinler1.getNickname());
                    kullanici_iki_for = etkinler1.getNickname();
                    kullanıcı_getir();
                    Log.i("Kullanıcılar", ":" + etkinler1.getNickname());

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void kullanıcı_getir() {
        kullanici_iki_getir = kullanici_iki_for;
    }

    public void soru_ve_cevapları_getir_turkce() {
        //todo sorular onay Sorular_turkce diye değişecek
        databaseReference.child("Sorular_turkce").child(String.valueOf(meydanoku_quiz)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mSorular = ds.getValue(Sorular.class);
                    sorularList.add(mSorular);
                }
                Collections.shuffle(sorularList);
                for (Sorular sorular : sorularList) {
                    mSwipeView.addView(new MeydanOku_TinderCard(mContext, sorular, mSwipeView, meydanoku_quiz));
                    cevaplistesi.add(sorular.getCevap());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void soru_ve_cevapları_getir_ingilizce() {
        databaseReference.child("Sorular_ingilizce").child(String.valueOf(meydanoku_quiz)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mSorular = ds.getValue(Sorular.class);
                    sorularList.add(mSorular);
                }
                Collections.shuffle(sorularList);
                for (Sorular sorular : sorularList) {
                    mSwipeView.addView(new MeydanOku_TinderCard(mContext, sorular, mSwipeView, meydanoku_quiz));
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

    public int cevapsiradonder() {
        return cevapsira;
    }

    public CountDownTimer getCountDownTimer() { //todo zaman her iki kullanıcı oyuna girince başlasa iyi olur
        countDownTimer = new CountDownTimer(20000, 1000) { //Burdaki saniye 49 olması lazım
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(millisUntilFinished / 1000));
                time_hatırla = millisUntilFinished;

            }

            @Override
            public void onFinish() {
                time.setText("0");
                ShowPop();
            }
        }.start();

        return countDownTimer;
    }

    @Override
    public void onBackPressed() {
        final Dialog cikis_dialog = new Dialog(this, R.style.DialogNotitle);
        cikis_dialog.setContentView(R.layout.exit_popup);
        cikis_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cikis_dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        // countDownTimer.cancel();
        Button evet = (Button) cikis_dialog.findViewById(R.id.dialog_cikis_evet);
        Button hayir = (Button) cikis_dialog.findViewById(R.id.dialog_cikis_hayir);
        TextView kaybettin = (TextView) cikis_dialog.findViewById(R.id.textview_normal);
        ImageView savas_image_cikis = (ImageView) cikis_dialog.findViewById(R.id.savas_image_cikis);
        kaybettin.setText(getResources().getString(R.string.oyun_kacis));
        savas_image_cikis.setImageResource(R.drawable.savas_cikis);

        evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_dialog.dismiss();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                databaseReference.child("Etkin").child(user_id).child("nickname").removeValue();
                finish();
            }
        });
        hayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_dialog.dismiss();


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
        kartsayısı++;
        if (karsı_taraf_kaybetme == kartsayısı) {
            ShowPop();
        }
    }

    public void ShowPop() {    //Zaman bittiğinde kazanılan altın ve elmasları gösteriyoruz
        countDownTimer.cancel();
        TextView pop_can_kullanici_bir, pop_para_kullanici_bir, pop_elmas_kullanici_bir, pop_can_kullanici_iki, pop_para_kullanici_iki, pop_elmas_kullanici_iki;
        Button meydan_buton_tamam;
        ImageView kategori_image;

        dialog.setContentView(R.layout.meydan_win_pop);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        meydan_buton_tamam = (Button) dialog.findViewById(R.id.meydan_buton_tamam);
        kullanıcı_bir_popup = (TextView) dialog.findViewById(R.id.kullanıcı_bir_eleman);
        kullanıcı_iki_popup = (TextView) dialog.findViewById(R.id.kullanıcı_iki_popup);
        kategori_image = dialog.findViewById(R.id.kategori_image);

        pop_can_kullanici_bir = (TextView) dialog.findViewById(R.id.pop_can_kullanicibir);
        pop_para_kullanici_bir = (TextView) dialog.findViewById(R.id.pop_para_kullanıcıbir);
        pop_elmas_kullanici_bir = (TextView) dialog.findViewById(R.id.pop_elmas_kullanıcıbir);

        pop_can_kullanici_iki = (TextView) dialog.findViewById(R.id.popup_can_kullanıcıiki);
        pop_para_kullanici_iki = (TextView) dialog.findViewById(R.id.pop_para_kullanıcıiki);
        pop_elmas_kullanici_iki = (TextView) dialog.findViewById(R.id.pop_elmas_kullanıcıiki);

        kullanıcı_bir_popup.setText(kullanici_adim);
        kullanıcı_iki_popup.setText(kullanici_iki_getir);


        pop_para_kullanici_bir.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
        pop_elmas_kullanici_bir.setText(String.valueOf((getEvetsayisi() + getHayirsayisi()) / 10));

        Random random = new Random();
        Log.i("Kartsayisi", ":" + kartsayısı);
        int max_kart_sayisi = kartsayısı + 2;
        int min_kart_sayisi = kartsayısı - 1;
        random_sayi_kaybetmek = random.nextInt(max_kart_sayisi - min_kart_sayisi) + min_kart_sayisi;
        if (kirik_kalp == 0) {
            pop_can_kullanici_bir.setText("3");
            pop_can_kullanici_iki.setText("0");
            pop_para_kullanici_iki.setText(String.valueOf(karsı_taraf_kaybetme - 3));
            pop_elmas_kullanici_iki.setText(String.valueOf((karsı_taraf_kaybetme - 3) / 10));
            databaseReference.child("Puanlar").child(user_id).child("para").setValue(12 + para);//todo para değeri buraya gelmesi lazım
            if ((karsı_taraf_kaybetme - 3) > getEvetsayisi() + getHayirsayisi()) {
                mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kaybetmek);
                mediaPlayer.start();
                kategori_image.setImageResource(R.drawable.sad);
            } else {
                kategori_image.setImageResource(R.drawable.winner);
                ekleveritabani();
                mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kazanan);
                mediaPlayer.start();

            }

        } else if (kirik_kalp == 1) {
            pop_can_kullanici_bir.setText("2");
            pop_can_kullanici_iki.setText("0");
            pop_para_kullanici_iki.setText(String.valueOf(karsı_taraf_kaybetme - 3));
            pop_elmas_kullanici_iki.setText(String.valueOf((karsı_taraf_kaybetme - 3) / 10));
            kategori_image.setImageResource(R.drawable.winner);
            if ((karsı_taraf_kaybetme - 3) > getEvetsayisi() + getHayirsayisi()) {
                mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kaybetmek);
                mediaPlayer.start();
                kategori_image.setImageResource(R.drawable.sad);
            } else {
                kategori_image.setImageResource(R.drawable.winner);
                ekleveritabani();
                mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kazanan);
                mediaPlayer.start();

            }

        } else if (kirik_kalp == 2) {
            pop_can_kullanici_bir.setText("1");
            pop_can_kullanici_iki.setText("0");
            pop_para_kullanici_iki.setText(String.valueOf(karsı_taraf_kaybetme - 3));
            pop_elmas_kullanici_iki.setText(String.valueOf((karsı_taraf_kaybetme - 3) / 10));
            kategori_image.setImageResource(R.drawable.winner);
            if ((karsı_taraf_kaybetme - 3) > getEvetsayisi() + getHayirsayisi()) {
                mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kaybetmek);
                mediaPlayer.start();
                kategori_image.setImageResource(R.drawable.sad);
            } else {
                kategori_image.setImageResource(R.drawable.winner);
                ekleveritabani();
                mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kazanan);
                mediaPlayer.start();

            }

        } else if (kirik_kalp == 3) {
            kategori_image.setImageResource(R.drawable.sad);
            mediaPlayer = MediaPlayer.create(Meydan_OkuActivity.this, R.raw.kaybetmek);
            mediaPlayer.start();
            pop_can_kullanici_bir.setText("0");
            Log.i("Random_sayi:", ":kart_sayisi" + random_sayi_kaybetmek + ":::" + kartsayısı);
            if (random_sayi_kaybetmek == (kartsayısı - 1)) {
                pop_can_kullanici_iki.setText(String.valueOf(1));
                pop_para_kullanici_iki.setText(String.valueOf(random_sayi_kaybetmek));
                pop_elmas_kullanici_iki.setText(String.valueOf(random_sayi_kaybetmek / 10));
            } else if (random_sayi_kaybetmek == (kartsayısı)) {
                pop_can_kullanici_iki.setText(String.valueOf(2));
                pop_para_kullanici_iki.setText(String.valueOf(random_sayi_kaybetmek));
                pop_elmas_kullanici_iki.setText(String.valueOf(random_sayi_kaybetmek / 10));


            } else if (random_sayi_kaybetmek == (kartsayısı + 1)) {
                pop_can_kullanici_iki.setText(String.valueOf(3));
                pop_para_kullanici_iki.setText(String.valueOf(random_sayi_kaybetmek));
                pop_elmas_kullanici_iki.setText(String.valueOf(random_sayi_kaybetmek / 10));


            }


        }

        meydan_buton_tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirik_kalp = 0;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                finish();
            }
        });
        if (!isFinishing()) {
            dialog.show();
        }
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
        kalp = gelen_kalp;
        para = gelen_para;
        elmas = gelen_elmas;
    }

    public void ekleveritabani() {
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
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(((getEvetsayisi() + getHayirsayisi()) / 10) + elmas);
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(100 - (elmas + para + getEvetsayisi() + getHayirsayisi()));//Todo burda sadece göstermelik için 10 ile çarptım
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

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }

        });

    }


}
