package com.example.cuma.tinder.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cuma.tinder.Activity.ExamsActivity;
import com.example.cuma.tinder.Activity.Meydan_OkuActivity;
import com.example.cuma.tinder.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import com.google.android.gms.ads.MobileAds;


public class MainFragment extends Fragment implements Animation.AnimationListener {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String etkin;

    public ArrayList<String> keylistesi = new ArrayList<>();
    public ArrayList<Iterable<DataSnapshot>> randomlistesi = new ArrayList<Iterable<DataSnapshot>>();
    public Dialog dialog, dialog_reklam;

    public static final String sorukey = "key";
    public static final String odakey = "odakey";
    public static final int default_oda_key = 5;
    public static final int tarih = 1;
    public static final int bilim = 2;
    public static final int eglence = 3;
    public static final int cografya = 4;
    public static final int sanat = 5;
    public static final int spor = 6;
    Random random;
    public int random_sayi;
    public int meydan_random_sayi;
    public int karsılasma = 1;
    private int progress_deger;
    private Handler handler = new Handler();
    private ProgressBar progressBar;
    public int meydanoku_random_sayi;
    public int meydanoku_random_sayi_int;
    public CountDownTimer countDownTimer;
    private int para, elmas, kalp, gelen_para, gelen_kalp, gelen_elmas;

    private InterstitialAd mInterstitialAd;


    public int getGelen_para() {
        return gelen_para;
    }

    public void setGelen_para(int gelen_para) {
        this.gelen_para = gelen_para;
    }

    public ArrayList<Integer> random_meydan_oku_list = new ArrayList<Integer>();
    Animation animation;
    ImageView checked, checked1, checked2, checked3, checked4, checked5, checked6, isaret_oku;
    ImageView oyunu_baslat_isaret_oku;
    ImageView meydan_oku, klasik;
    TextView main_kategori_adi, main_motivasyon, karsılasma_time;
    TextView main_kalp_toplam, main_para_toplam, main_elmas_toplam;
    ImageView main_kategori_resmi;
    ImageButton main_tekrar_buton, main_basla_buton;
    Intent key_gonder, meydan_oku_key_gonder, oda_ismi;
    MediaPlayer mediaPlayer;
    int kalp_deger, gelen_kalp_deger;
    Button onay_donusum;
    int reklam_kalp;

    private AdView mAdView;
    private RewardedVideoAd mRewardedVideoAd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();
        UUID uuıd = UUID.randomUUID();
        final String uuidString = uuıd.toString();

        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            ogretici();
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        MobileAds.initialize(getActivity(), "ca-app-pub-7740710689946524~4712663337");
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //todo oooooooooooooooooooooooo


        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;

        oyunu_baslat_isaret_oku = view.findViewById(R.id.isaret_oku);
        meydan_oku = view.findViewById(R.id.meydan_oku);
        klasik = view.findViewById(R.id.klasik);
        isaret_oku = (ImageView) view.findViewById(R.id.isaret_oku);

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        animation.setAnimationListener(MainFragment.this);

        main_kalp_toplam = (TextView) view.findViewById(R.id.main_kalp_toplam);
        main_para_toplam = (TextView) view.findViewById(R.id.main_para_toplam);
        main_elmas_toplam = (TextView) view.findViewById(R.id.main_elmas_toplam);
        meydan_oku_key_gonder = new Intent(getActivity(), Meydan_OkuActivity.class);

        checked1 = (ImageView) view.findViewById(R.id.kategori_image_checked1);
        checked2 = (ImageView) view.findViewById(R.id.kategori_image_checked2);
        checked3 = (ImageView) view.findViewById(R.id.kategori_image_checked3);
        checked4 = (ImageView) view.findViewById(R.id.kategori_image_checked4);
        checked5 = (ImageView) view.findViewById(R.id.kategori_image_checked5);
        checked6 = (ImageView) view.findViewById(R.id.kategori_image_checked6);
        klasik.setImageResource(R.drawable.checked);
        carki_tekrar_cevir();
        setHasOptionsMenu(true);
        Firebase_get_data();
        Firebase_get_kalp_deger();
        cagir_kalp();
        odullu_reklam();
        klasik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                klasik.setImageResource(R.drawable.checked);
                meydan_oku.setImageResource(R.drawable.savas);
                karsılasma = 1;

            }
        });
        meydan_oku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meydan_oku.setImageResource(R.drawable.checked);
                klasik.setImageResource(R.drawable.klasik);
                karsılasma = 2;
            }
        });

        oyunu_baslat_isaret_oku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnection()) {
                    if (cagir_kalp() == 0) {
                        odullu_reklam();
                        reklam_izle();
                    } else {
                        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.play_game);
                        mediaPlayer.start();
                        switch (karsılasma) {
                            case 1: //klasik yöntemdir
                                random = new Random();
                                random_sayi = 1 + random.nextInt(6);
                                isaret_oku.startAnimation(animation);
                                break;
                            case 2: //karşılaşmadır
                                kullanıcı_etkinlestirme();
                                random = new Random();
                                meydan_random_sayi = 1 + random.nextInt(6);
                                isaret_oku.startAnimation(animation);
                                break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_text), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void Firebase_get_kalp_deger() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Todo kalbe veriyi toplam kalp puanı felan br şey olması lazım satın aldığı kadar veya her 24 saatte 5 tane felan
                gelen_kalp_deger = dataSnapshot.child("Puanlar").child(user_id).child("kalp").getValue(Integer.class);
                cagir_kalp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int cagir_kalp() {
        kalp_deger = gelen_kalp_deger;
        return kalp_deger;
    }

    public void kullanıcı_beklet_dialog() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.meydan_oku_ses);
        mediaPlayer.start();
        dialog.setContentView(R.layout.dialog_kullanici_beklet);
        karsılasma_time = (TextView) dialog.findViewById(R.id.karsılasma_time);
        dialog.show();
        countDownTimer = new CountDownTimer(4000, 1000) { //Burdaki saniye 49 olması lazım
            @Override
            public void onTick(long millisUntilFinished) {
                karsılasma_time.setText(String.valueOf(millisUntilFinished / 1000).toString());
            }

            @Override
            public void onFinish() {
                startActivity(meydan_oku_key_gonder);
                dialog.dismiss();
            }
        }.start();

    }


    private void deneme() {
        Log.i("Deneme_veri", ":" + meydanoku_random_sayi);
        meydanoku_random_sayi_int = meydanoku_random_sayi;
    }


    @Override
    public void onAnimationStart(Animation animation) {
        oyunu_baslat_isaret_oku.setEnabled(false);

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Log.i("Random", ":" + random_sayi);
        // isaret_oku.clearAnimation();
        switch (karsılasma) {
            case 1:
                key_gonder = new Intent(getActivity(), ExamsActivity.class);
                switch (random_sayi) {
                    case 1:
                        isaret_oku.setRotation(315); //Tarih
                        checked1.setImageResource(R.drawable.checked);
                        key_gonder.putExtra(sorukey, tarih);
                        // startActivity(key_gonder);
                        show_pup();
                        break;
                    case 2:
                        isaret_oku.setRotation(0);//Bilim
                        checked2.setImageResource(R.drawable.checked);
                        key_gonder.putExtra(sorukey, bilim);
                        // startActivity(key_gonder);
                        show_pup();
                        break;
                    case 3:
                        isaret_oku.setRotation(45);//Eğlence
                        checked3.setImageResource(R.drawable.checked);
                        key_gonder.putExtra(sorukey, eglence);
                        // startActivity(key_gonder);
                        show_pup();
                        break;
                    case 4:
                        isaret_oku.setRotation(225);//Coğrafya
                        checked4.setImageResource(R.drawable.checked);
                        key_gonder.putExtra(sorukey, cografya);
                        // startActivity(key_gonder);
                        show_pup();
                        break;
                    case 5:
                        isaret_oku.setRotation(180);//Sanat
                        checked5.setImageResource(R.drawable.checked);
                        key_gonder.putExtra(sorukey, sanat);
                        // startActivity(key_gonder);
                        show_pup();
                        break;
                    case 6:
                        isaret_oku.setRotation(135);//Sporsa
                        checked6.setImageResource(R.drawable.checked);
                        key_gonder.putExtra(sorukey, spor);
                        show_pup();
                        break;

                }
                break;
            case 2:
                //  meydan_oku_ıntent = new Intent(getActivity(), Meydan_OkuActivity.class);/
                switch (meydan_random_sayi) {
                    case 1:
                        isaret_oku.setRotation(315); //Tarih
                        checked1.setImageResource(R.drawable.checked);
                        meydan_oku_key_gonder.putExtra(sorukey, tarih);
                        kullanıcı_beklet_dialog();
                        break;
                    case 2:
                        isaret_oku.setRotation(0);//Bilim
                        checked2.setImageResource(R.drawable.checked);
                        meydan_oku_key_gonder.putExtra(sorukey, bilim);
                        kullanıcı_beklet_dialog();
                        break;
                    case 3:
                        isaret_oku.setRotation(45);//Eğlence
                        checked3.setImageResource(R.drawable.checked);
                        meydan_oku_key_gonder.putExtra(sorukey, eglence);
                        kullanıcı_beklet_dialog();

                        break;
                    case 4:
                        isaret_oku.setRotation(225);//Coğrafya
                        checked4.setImageResource(R.drawable.checked);
                        meydan_oku_key_gonder.putExtra(sorukey, cografya);
                        kullanıcı_beklet_dialog();
                        break;
                    case 5:
                        isaret_oku.setRotation(180);//Sanat
                        checked5.setImageResource(R.drawable.checked);
                        meydan_oku_key_gonder.putExtra(sorukey, sanat);
                        kullanıcı_beklet_dialog();
                        break;
                    case 6:
                        isaret_oku.setRotation(135);//Sporsa
                        checked6.setImageResource(R.drawable.checked);
                        meydan_oku_key_gonder.putExtra(sorukey, spor);
                        kullanıcı_beklet_dialog();
                        break;

                }

                break;
        }
        oyunu_baslat_isaret_oku.setEnabled(true);
    }


    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void show_pup() {
        dialog.setContentView(R.layout.basla_dialog);
        main_kategori_adi = (TextView) dialog.findViewById(R.id.savas_baslik);
        main_kategori_resmi = (ImageView) dialog.findViewById(R.id.savas_icon);
        main_basla_buton = (ImageButton) dialog.findViewById(R.id.savas_basla_buton);
        main_tekrar_buton = (ImageButton) dialog.findViewById(R.id.savas_tekrar_buton);
        main_motivasyon = (TextView) dialog.findViewById(R.id.text_baslikkk);
        switch (random_sayi) {
            case 1:
                main_kategori_adi.setText(getResources().getString(R.string.savas_baslik_tarih));
                main_motivasyon.setText(getResources().getString(R.string.savas_aciklama_tarih));
                main_kategori_resmi.setImageResource(R.drawable.tarih);
                break;
            case 2:
                main_kategori_adi.setText(getResources().getString(R.string.savas_baslik_bilim));
                main_motivasyon.setText(getResources().getString(R.string.savas_aciklama_bilim));
                main_kategori_resmi.setImageResource(R.drawable.bilim_pop);
                break;
            case 3:
                main_kategori_adi.setText(getResources().getString(R.string.savas_baslik_eglence));
                main_motivasyon.setText(getResources().getString(R.string.savas_aciklama_eglence));
                main_kategori_resmi.setImageResource(R.drawable.eglence_pop);
                break;
            case 4:
                main_kategori_adi.setText(getResources().getString(R.string.savas_baslik_cografya));
                main_motivasyon.setText(getResources().getString(R.string.savas_aciklama_cografya));
                main_kategori_resmi.setImageResource(R.drawable.cografya_pop);
                break;
            case 5:
                main_kategori_adi.setText(getResources().getString(R.string.savas_baslik_sanat));
                main_motivasyon.setText(getResources().getString(R.string.savas_aciklama_sanat));
                main_kategori_resmi.setImageResource(R.drawable.sanat_pop);
                break;
            case 6:
                main_kategori_adi.setText(getResources().getString(R.string.savas_baslik_spor));
                main_motivasyon.setText(getResources().getString(R.string.savas_aciklama_spor));
                main_kategori_resmi.setImageResource(R.drawable.spor_pop);
                break;

        }
        main_basla_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(key_gonder);
                dialog.dismiss();
                carki_tekrar_cevir();

            }
        });
        main_tekrar_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                carki_tekrar_cevir();
            }
        });
        dialog.show();
    }


    public void carki_tekrar_cevir() {
        checked1.setImageResource(R.drawable.tarihim);
        checked2.setImageResource(R.drawable.bilim);
        checked3.setImageResource(R.drawable.eglence);
        checked4.setImageResource(R.drawable.cografya);
        checked5.setImageResource(R.drawable.sanat);
        checked6.setImageResource(R.drawable.spor);
        isaret_oku.setRotation(0);

    }

    public void Firebase_get_data() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Todo kalbe veriyi toplam kalp puanı felan br şey olması lazım satın aldığı kadar veya her 24 saatte 5 tane felan
                kalp = dataSnapshot.child("Puanlar").child(user_id).child("kalp").getValue(Integer.class);
                main_kalp_toplam.setText(String.valueOf(kalp));

                para = dataSnapshot.child("Puanlar").child(user_id).child("para").getValue(Integer.class);
                main_para_toplam.setText(String.valueOf(para));

                elmas = dataSnapshot.child("Puanlar").child(user_id).child("elmas").getValue(Integer.class);
                main_elmas_toplam.setText(String.valueOf(elmas));
                //cagir();
                if (para >= 50 || elmas > 5) {
                    donusum();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void donusum() {
        dialog.setContentView(R.layout.donusum_dialog);
        progressBar = dialog.findViewById(R.id.donusum_progressBar);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        onay_donusum = dialog.findViewById(R.id.onay_donusum);
        dialog.show();
        // progressBar.setVisibility(View.INVISIBLE);
        onay_donusum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                gelen_para = 0;
                gelen_elmas = 0;
                databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(kalp + 3);
                databaseReference.child("Puanlar").child(user_id).child("para").setValue(0);
                databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(0);
                Thread timer = new Thread() {
                    public void run() {
                        while (progress_deger < 100) {
                            progress_deger++;
                            android.os.SystemClock.sleep(50);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progress_deger);
                                }
                            });
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                            }
                        });

                         /*   try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                 startActivity(ıntent);
                            }   */
                    }
                };
                timer.start();

            }
        });


    }

    public void reklam_izle() {
        Button exit_dialog, izle_button;
        dialog_reklam = new Dialog(getActivity());
        Window window = dialog_reklam.getWindow();
        dialog_reklam.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.reklam_izle_dialog);
        dialog_reklam.setCancelable(false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        izle_button = (Button) dialog_reklam.findViewById(R.id.reklam_izle);
        exit_dialog = (Button) dialog_reklam.findViewById(R.id.exit_reklam);
        izle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    Toast.makeText(getActivity(), "Lütfen biraz bekleyin", Toast.LENGTH_LONG).show();
                }
            }
        });
        exit_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_reklam.dismiss();
            }
        });
        dialog_reklam.show();

    }

    public void odullu_reklam() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.loadAd("ca-app-pub-7740710689946524/6704363861", new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                odullu_reklam();

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                reklam_kalp = rewardItem.getAmount();
                Log.i("Reklamcan", ":" + reklam_kalp);

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {
                databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(2);
                dialog_reklam.dismiss();
                odullu_reklam();
            }
        });
    }

    public void ogretici() {
        Button next_buton;
        final Dialog dialog = new Dialog(getActivity());
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.dialog_ogretici);
        dialog.setCancelable(false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        next_buton = dialog.findViewById(R.id.gec_ogretici);
        next_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void kullanıcı_etkinlestirme() {
        databaseReference.child("Kullanıcı_Adı").child(user_id).child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etkin = dataSnapshot.getValue(String.class);
                databaseReference.child("Etkin").child(user_id).child("nickname").setValue(etkin);
                meydan_oku_key_gonder.putExtra("kullanici_adim", etkin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean networkConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
