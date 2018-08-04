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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;

import java.util.ArrayList;
import java.util.UUID;

public class ExamsActivity extends AppCompatActivity {

    //Eğer popupta son ana kadar beklersem puan ve can değişiyor
    //TODO dogru cevap sayısı fazladan değer geliyor
    // TODO //home // Restart // Devam et  Decam et kısmı düzelecek fakat çıkıç yapmak isterse görünecek
    //TODO uygulama açılacağı zaman status bar önce sarı sonra siyah oluyor
    //TODO doğru sayısımı ve yanlış sayısını farklı tutmak lazım
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    private String user_id;
    Integer para_toplam;

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Profile mProfile;
    private Sorular mSorular;
    private Toolbar toolbar;
    public Utils utils;
    public CountDownTimer countDownTimer;
    public TextView time;
    private ImageButton like, dislike, evet_buton, hayir_buton;
    public PuanHesapla puanHesapla;
    public Dialog dialog;
    public int evetsayisi = 0, hayirsayisi = 0;
    public int cevapsira, kirik_kalp = 0;
    ImageView kirik_kalp_image1, kirik_kalp_image2, kirik_kalp_image3;
    private long time_hatırla = 0;
    public ArrayList<String> cevaplistesi = new ArrayList<>();
    public ArrayList<Sorular> sorularList = new ArrayList<>();
    int tut = 0;
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
        //    mProfile = new Profile("Bu da bir cevap");//Burda örnek var buraya listeden çekmem lazım

        getCountDownTimer();// Timer kullanımını gerçekleştirdik
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quiz = getIntent().getIntExtra(MainFragment.sorukey, MainFragment.tarih);
        dialog = new Dialog(this, R.style.DialogNotitle);

        //       utils = new Utils();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3) //Burası görünümde kaç kart göstereceği pek alakası yok
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f));
        // .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)           //Yana kaydırırken mesajın doğru oldğunu
        // .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));       //Yana kaydırırken mesajın yanlış olduğunu yazıyoruz
    /*   for (Profile deger : Utils.loadProfiles(this.getApplicationContext(), quiz)) {       //her bir elemanda gez degere at : nerde gezineceksin
            mSwipeView.addView(new TinderCard(mContext, deger, mSwipeView, quiz));  //swipe içini dolduruyor
            cevaplistesi.add(deger.getAnswer());
        }*/
        soru_ve_cevapları_getir();
        evet_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
                String evet = "Yes";
                if (getCurrentAnswer().equalsIgnoreCase(evet)) {
                    incEvetsayisi();
                    descHayirsayisi();
                } else {
                    Log.i("Kalp_Sayısı", ":" + getKirik_kalp());
                    descEvetsayisi();
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
                    descEvetsayisi();
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
                    mSwipeView.addView(new TinderCard(mContext, mSorular, mSwipeView, quiz));
                    cevaplistesi.add(mSorular.getCevap());


                }
                Log.i("Sorular_listesi", ":" + mSorular.getCevap());

                //    Log.i("Liste_Cek", ":" +sorularList.get(2).toString());


                //todo cevaplar ve sorular gelecek gelen değerlere göre doğru yanlış hesaplanacak
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
        countDownTimer = new CountDownTimer(36900, 1000) { //Burdaki saniye 49 olması lazım
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(millisUntilFinished / 1000).toString());
                time_hatırla = millisUntilFinished;
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
        ImageButton home, again, devam;
        ImageView kategori_image;
        dialog.setContentView(R.layout.win_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        home = (ImageButton) dialog.findViewById(R.id.popup_home);
        again = (ImageButton) dialog.findViewById(R.id.popoup_restart);
        devam = (ImageButton) dialog.findViewById(R.id.popup_play);
        kategori_image = (ImageView) dialog.findViewById(R.id.kategori_image);
        kategori_text = (TextView) dialog.findViewById(R.id.kategori_popup);

        popup_can = (TextView) dialog.findViewById(R.id.popup_can);
        popup_para = (TextView) dialog.findViewById(R.id.pop_para);
        popup_elmas = (TextView) dialog.findViewById(R.id.pop_elmas);
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

    public void ekleveritabani() {
        int para_topla;
        user = firebaseAuth.getCurrentUser();
        String useremail = user.getEmail().toString();
        UUID uuıd = UUID.randomUUID();
        String uuidString = uuıd.toString();

        // todo Databasedek verileri önce çekip sonra üstüne kaydetmemiz lazım
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Integer para = dataSnapshot.child("Puanlar").child(user_id).child("para").getValue(Integer.class);
                //para_topla= getDogrucevapsayisi() + para;
                Log.i("ParaDegeri", ":" + para);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        databaseReference.child("Puanlar").child(user_id).child("para").setValue(getEvetsayisi() + getHayirsayisi());
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(getEvetsayisi() + getHayirsayisi());
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(getEvetsayisi() * 10);//Todo burda sadece göstermelik için 10 ile çarptım

    }


}
