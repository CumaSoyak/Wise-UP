package com.example.cuma.tinder.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    public ImageButton like, dislike, evet_buton, hayir_buton;
    public PuanHesapla puanHesapla;
    public Dialog dialog;
    public int evetsayisi, hayirsayisi;
    public int cevapsira, kirik_kalp = 0;
    ImageView kirik_kalp_image1, kirik_kalp_image2, kirik_kalp_image3;
    private long time_hatırla = 0;
    public ArrayList<String> cevaplistesi = new ArrayList<>();
    public ArrayList<Sorular> sorularList = new ArrayList<Sorular>();
    int para_topla;
    int gelen_para,gelen_elmas, para,elmas;

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


        TinderCard tinderCard = new TinderCard();
        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = this;
        time = (TextView) findViewById(R.id.time);
        like = (ImageButton) findViewById(R.id.like_img);
        dislike = (ImageButton) findViewById(R.id.dislike_img);
        evet_buton = (ImageButton) findViewById(R.id.acceptBtn);
        hayir_buton = (ImageButton) findViewById(R.id.rejectBtn);
        kirik_kalp_image1 = (ImageView) findViewById(R.id.kalp1);
        kirik_kalp_image2 = (ImageView) findViewById(R.id.kalp2);
        kirik_kalp_image3 = (ImageView) findViewById(R.id.kalp3);

        getCountDownTimer();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        quiz = getIntent().getIntExtra(MainFragment.sorukey, MainFragment.tarih);
        dialog = new Dialog(this, R.style.DialogNotitle);


        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f));
        // .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
        // .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
    /*   for (Profile deger : Utils.loadProfiles(this.getApplicationContext(), quiz)) {
            mSwipeView.addView(new TinderCard(mContext, deger, mSwipeView, quiz));
            cevaplistesi.add(deger.getAnswer());
        }*/


        soru_ve_cevapları_getir();
        puanlargetir();
        evet_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
                String evet = "Yes";
                if (getCurrentAnswer().equalsIgnoreCase(evet)) {
                    incEvetsayisi();

                } else {
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


    public void soru_ve_cevapları_getir() {
        databaseReference.child("Sorular").child(String.valueOf(quiz)).addValueEventListener(new ValueEventListener() {
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
                ShowPop();
            }
        }.start();

        return countDownTimer;
    }

    public void ShowPop() {    //Zaman bittiğinde kazanılan altın ve elmasları gösteriyoruz
        TextView textclose, kategori_text, popup_para, popup_can, popup_elmas;
        ImageButton home, again, devam;
        ImageView kategori_image;
        dialog.setContentView(R.layout.win_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        home = (ImageButton) dialog.findViewById(R.id.popup_home);
        again = (ImageButton) dialog.findViewById(R.id.popoup_restart);
        devam = (ImageButton) dialog.findViewById(R.id.popup_play);
        kategori_image = (ImageView) dialog.findViewById(R.id.kategori_image);
        kategori_text = (TextView) dialog.findViewById(R.id.kullanıcı_bir_popup);

        popup_can = (TextView) dialog.findViewById(R.id.pop_para_kullanıcıiki);
        popup_para = (TextView) dialog.findViewById(R.id.pop_para_kullanıcıbir);
        popup_elmas = (TextView) dialog.findViewById(R.id.pop_elmas_kullanıcıbir);
        Log.i("Dogru", ":" + String.valueOf(getEvetsayisi()));
        if (puanHesapla.puanarti() <= 0) {
            popup_para.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
            popup_elmas.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
        }
        popup_para.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
        popup_elmas.setText(String.valueOf(getEvetsayisi() + getHayirsayisi()));
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
                kategori_image.setImageResource(R.drawable.tarihim);
                kategori_text.setText("Tarih");
                break;
            case 2:
                kategori_image.setImageResource(R.drawable.bilim);
                kategori_text.setText("Bilim");
                break;
            case 3:
                kategori_image.setImageResource(R.drawable.eglence);
                kategori_text.setText("Eğlence");
                break;
            case 4:
                kategori_image.setImageResource(R.drawable.cografya);
                kategori_text.setText("Dünya");
                break;
            case 5:
                kategori_image.setImageResource(R.drawable.sanat);
                kategori_text.setText("Sanat");
                break;
            case 6:
                kategori_image.setImageResource(R.drawable.spor);
                kategori_text.setText("Spor");
                break;

        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirik_kalp = 0;
                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ıntent);

            }
        });
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                kirik_kalp = 0;
                Intent intent = getIntent();
                finish();
                startActivity(intent);


            }
        });
        devam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //Burası düzelecek
                //TODO devam yapamaması lazım çunku süre bitince veya yanınca popup açılıyor
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
        cikis_dialog.setContentView(R.layout.exit_popup);
        cikis_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cikis_dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        // countDownTimer.cancel();
        Button devam_et = (Button) cikis_dialog.findViewById(R.id.dialog_cikis_evet);
        Button cikis_yap = (Button) cikis_dialog.findViewById(R.id.dialog_cikis_hayir);
        devam_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_dialog.dismiss();
            }
        });
        cikis_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_dialog.dismiss();//Anasayfaya dönmeden önce dialogu kapatmak lazım
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
                cagir();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void cagir() {
        para = gelen_para;
        elmas=gelen_elmas;
    }

    public void ekleveritabani() {
        user = firebaseAuth.getCurrentUser();
        useremail = user.getEmail().toString();
        UUID uuıd = UUID.randomUUID();
        String uuidString = uuıd.toString();

        Log.i("ParaDegeri", ":" + para);
        if (kirik_kalp == 0) {
            databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(3);
        } else if (kirik_kalp == 1) {
            databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(2);
        } else if (kirik_kalp == 2) {
            databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(1);
        } else if (kirik_kalp == 3) {
            databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(0);
        }
        databaseReference.child("Puanlar").child(user_id).child("useremail").setValue(useremail);
        databaseReference.child("Puanlar").child(user_id).child("para").setValue(getEvetsayisi() + getHayirsayisi() + para);//todo para değeri buraya gelmesi lazım
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(getEvetsayisi() + getHayirsayisi()+elmas);
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(100-(elmas+para+getEvetsayisi()+getHayirsayisi()));//Todo burda sadece göstermelik için 10 ile çarptım
        databaseReference.child("Yarisma").child(user_id).child("puan").setValue(elmas+para+getEvetsayisi()+getHayirsayisi());//Todo burda sadece göstermelik için 10 ile çarptım


    }


}
