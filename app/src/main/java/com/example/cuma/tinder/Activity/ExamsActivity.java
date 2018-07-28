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
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.example.cuma.tinder.Fragment.MainFragment;
import com.example.cuma.tinder.R;
import com.example.cuma.tinder.TinderCard;
import com.example.cuma.tinder.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Integer  para_toplam;

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Profile mProfile;
    private Toolbar toolbar;
    public Utils utils;
    public CountDownTimer countDownTimer;
    public TextView time;
    private ImageButton like,dislike,evet_buton,hayir_buton;
    public PuanHesapla puanHesapla;
    public Dialog dialog;
    public int  dogrucevapsayisi=0,yanlis_dogrucevapsayisi;
    public int  cevapsira,kirik_kalp;
    ImageView kirik_kalp_image1,kirik_kalp_image2,kirik_kalp_image3;
    private long time_hatırla=0;
    public ArrayList<String> cevaplistesi = new ArrayList<>();

    private static final String TAG = "ExamsActivity";
    public int  quiz;

    public int getDogrucevapsayisi() {
        return dogrucevapsayisi;
    }

    public void setDogrucevapsayisi(int dogrucevapsayisi) {
        this.dogrucevapsayisi = dogrucevapsayisi;
    }

    public void incDogrucevapsayisi() {
        this.dogrucevapsayisi +=1;
    }

    public void decDogrucevapsayisi() {
        this.dogrucevapsayisi -=1;
    }

    public ArrayList<String> getCevaplistesi() {
        return cevaplistesi;
    }

    public String getCurrentAnswer(){
        return cevaplistesi.get(cevapsira);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        user=firebaseAuth.getCurrentUser();
        user_id=user.getUid();



        //databaseReference.setValue("Hello World");


        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = this;
        time = (TextView) findViewById(R.id.time);
        like=(ImageButton)findViewById(R.id.like_img);
        dislike=(ImageButton)findViewById(R.id.dislike_img);
        evet_buton=(ImageButton)findViewById(R.id.acceptBtn);
        hayir_buton=(ImageButton)findViewById(R.id.rejectBtn);
        kirik_kalp_image1=(ImageView)findViewById(R.id.kalp1);
        kirik_kalp_image2=(ImageView)findViewById(R.id.kalp2);
        kirik_kalp_image3=(ImageView)findViewById(R.id.kalp3);
    //    mProfile = new Profile("Bu da bir cevap");//Burda örnek var buraya listeden çekmem lazım

        getCountDownTimer();// Timer kullanımını gerçekleştirdik
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quiz = getIntent().getIntExtra(MainFragment.sorukey, MainFragment.tarih);
        dialog = new Dialog(this, R.style.DialogNotitle);

        utils = new Utils();
        puanHesapla = new PuanHesapla();

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

        hayir_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
                String hayir = "Hayır";
                if (getCurrentAnswer().equalsIgnoreCase(hayir)) { incDogrucevapsayisi();
                }
                else {

                    kirik_kalp++;
                    if (kirik_kalp==1){ kirik_kalp_image1.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==2){ kirik_kalp_image2.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ kirik_kalp_image3.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ ShowPop(); } }

                cevapsira++;
                Log.i("KirikKalp",": :"+kirik_kalp);
                cevabı_beklet();


            }
        });
        evet_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
                String evet = "Evet";
                if (getCurrentAnswer().equalsIgnoreCase(evet)) {   incDogrucevapsayisi();
                    //Log.i("Evet Puan",":",puanHesapla.puanarti());
                }
                else{
                    decDogrucevapsayisi();
                    kirik_kalp++;
                    if (kirik_kalp==1){ kirik_kalp_image1.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==2){ kirik_kalp_image2.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ kirik_kalp_image3.setImageResource(R.drawable.kirikalp); }
                    if (kirik_kalp==3){ ShowPop(); }
                }
                cevapsira++;
                cevabı_beklet();



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
                time.setText( String.valueOf(millisUntilFinished / 1000).toString());
                time_hatırla=millisUntilFinished;
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
        Log.i("Dogru", ":" + String.valueOf(getDogrucevapsayisi()));
        if (puanHesapla.puanarti() <= 0) {
            popup_para.setText(String.valueOf(getDogrucevapsayisi()));
            popup_elmas.setText(String.valueOf(getDogrucevapsayisi()));
        }
        popup_para.setText(String.valueOf(getDogrucevapsayisi()));
        popup_elmas.setText(String.valueOf(getDogrucevapsayisi()));
        Log.i("Kalpp",":"+kirik_kalp);
        if (kirik_kalp==0){popup_can.setText("3");}
        else if (kirik_kalp==1){popup_can.setText("2");}
        else if (kirik_kalp==2){popup_can.setText("1");}
        else if (kirik_kalp==3){popup_can.setText("0");}

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
                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ıntent);
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
    public void onBackPressed() {              //Çıkış yapmak istiyormmusun diye soruyoruz
        //TODO süre durduktan sonra devam etmesi lazım
         final Dialog cikis_dialog=new Dialog(this,R.style.DialogNotitle);
         cikis_dialog.setContentView(R.layout.exit_popup);
         cikis_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         cikis_dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
         countDownTimer.cancel();
         Button devam_et=(Button)cikis_dialog.findViewById(R.id.devam_et);
         Button cikis_yap=(Button)cikis_dialog.findViewById(R.id.cikis_yap);
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

    public void cevabı_beklet(){
        evet_buton.setEnabled(false);
        hayir_buton.setEnabled(false);
        int bekletsure=400;
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                evet_buton.setEnabled(true);
                hayir_buton.setEnabled(true);

            }
        },bekletsure);


    }

    public void ekleveritabani(){

        int para_topla;
        user=firebaseAuth.getCurrentUser();
        String useremail=user.getEmail().toString();
        UUID uuıd=UUID.randomUUID();
        String uuidString=uuıd.toString();

        //Databasedek verileri önce çekip sonra üstüne kaydetmemiz lazım
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             Integer para= dataSnapshot.child("Puanlar").child(user_id).child("para").getValue(Integer.class);
             //para_topla= getDogrucevapsayisi() + para;
                Log.i("ParaDegeri",":"+para);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Puanlar").child(user_id).child("useremail").setValue(useremail);
        databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(kirik_kalp);
        databaseReference.child("Puanlar").child(user_id).child("para").setValue(getDogrucevapsayisi());
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(getDogrucevapsayisi());

    }


}
