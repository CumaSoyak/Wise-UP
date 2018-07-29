package com.example.cuma.tinder.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import android.widget.TextView;


import com.example.cuma.tinder.Activity.ExamsActivity;
import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

public class MainFragment extends Fragment implements Animation.AnimationListener {

    private   FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;

    public static final String sorukey = "key";
    public static final int tarih = 1;
    public static final int bilim = 2;
    public static final int eglence = 3;
    public static final int cografya = 4;
    public static final int sanat = 5;
    public static final int spor = 6;
    Random random;
    public int random_sayi;
    Animation animation;
    ImageView checked, checked1, checked2, checked3, checked4, checked5, checked6, isaret_oku;
    Button oyunu_baslat;
    TextView main_kategori_adi,main_motivasyon;
    TextView main_kalp_toplam,main_para_toplam,main_elmas_toplam;
    ImageView main_kategori_resmi;
    ImageButton main_tekrar_buton,main_basla_buton;
    Intent key_gonder;

    //TODO manin fragment deyken  geri dersem dialog açılıyorrr
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        user =firebaseAuth.getCurrentUser();
        user_id=user.getUid();


        oyunu_baslat = (Button) view.findViewById(R.id.baslat);
        isaret_oku = (ImageView) view.findViewById(R.id.isaret_oku);
        animation=AnimationUtils.loadAnimation(getActivity(),R.anim.rotate);
        animation.setAnimationListener(MainFragment.this);

        main_kalp_toplam=(TextView)view.findViewById(R.id.main_kalp_toplam);
        main_para_toplam=(TextView)view.findViewById(R.id.main_para_toplam);
        main_elmas_toplam=(TextView)view.findViewById(R.id.main_elmas_toplam);


        checked1 = (ImageView) view.findViewById(R.id.kategori_image_checked1);
        checked2 = (ImageView) view.findViewById(R.id.kategori_image_checked2);
        checked3 = (ImageView) view.findViewById(R.id.kategori_image_checked3);
        checked4 = (ImageView) view.findViewById(R.id.kategori_image_checked4);
        checked5 = (ImageView) view.findViewById(R.id.kategori_image_checked5);
        checked6 = (ImageView) view.findViewById(R.id.kategori_image_checked6);


        //TODO silinecek experimental
        setHasOptionsMenu(true);
        Firebase_get_data();

        oyunu_baslat.setOnClickListener(new View.OnClickListener() {
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
       // Log.i("Random", ":" + random_sayi);
          key_gonder=new Intent(getActivity(),ExamsActivity.class);
        switch (random_sayi) {
            case 1:
                isaret_oku.setRotation(315); //Tarih
                checked1.setImageResource(R.drawable.checked);
                key_gonder.putExtra(sorukey,tarih);
               // startActivity(key_gonder);
                show_pup();
                break;
            case 2:
                isaret_oku.setRotation(0);//Bilim
                checked2.setImageResource(R.drawable.checked);
                key_gonder.putExtra(sorukey,bilim);
               // startActivity(key_gonder);
                show_pup();
                break;
            case 3:
                isaret_oku.setRotation(45);//Eğlence
                checked3.setImageResource(R.drawable.checked);
                key_gonder.putExtra(sorukey,eglence);
               // startActivity(key_gonder);
                show_pup();
                break;
            case 4:
                isaret_oku.setRotation(225);//Coğrafya
                checked4.setImageResource(R.drawable.checked);
                key_gonder.putExtra(sorukey,cografya);
               // startActivity(key_gonder);
                show_pup();
                break;
            case 5:
                isaret_oku.setRotation(180);//Sanat
                checked5.setImageResource(R.drawable.checked);
                key_gonder.putExtra(sorukey,sanat);
               // startActivity(key_gonder);
                show_pup();
                break;
            case 6:
                isaret_oku.setRotation(135);//Sporsa
                checked6.setImageResource(R.drawable.checked);
                key_gonder.putExtra(sorukey,spor);
               // startActivity(key_gonder);
                show_pup();
                break;

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    private void show_pup() {
    final Dialog dialog=new Dialog(getActivity());
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    dialog.setContentView(R.layout.basla_dialog);
    dialog.setCancelable(false);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        main_kategori_adi=(TextView)dialog.findViewById(R.id.main_kategoriadi);
        main_kategori_resmi=(ImageView)dialog.findViewById(R.id.main_kategori_resmi);
        main_basla_buton=(ImageButton)dialog.findViewById(R.id.main_basla_buton);
        main_tekrar_buton=(ImageButton)dialog.findViewById(R.id.main_tekrar_buton);
        main_motivasyon=(TextView)dialog.findViewById(R.id.basla_motivasyon);
        switch (random_sayi){
            case 1: main_kategori_adi.setText("Tarih"); main_motivasyon.setText("Tarihte iyimisin Dostum !"); main_kategori_resmi.setImageResource(R.drawable.tarihim); break;
            case 2: main_kategori_adi.setText("Bilim"); main_motivasyon.setText("Seni gidi BilimAdamı"); main_kategori_resmi.setImageResource(R.drawable.bilim);break;
            case 3: main_kategori_adi.setText("Eğlence");main_motivasyon.setText("Hadi biraz Eğlenelim");main_kategori_resmi.setImageResource(R.drawable.eglence);break;
            case 4: main_kategori_adi.setText("Coğrafya");main_motivasyon.setText("Dünyayı turlamaya ne dersin!");main_kategori_resmi.setImageResource(R.drawable.cografya);break;
            case 5: main_kategori_adi.setText("Sanat");main_motivasyon.setText("Sanata yeteneğin olduğunu bilmiyordum !");main_kategori_resmi.setImageResource(R.drawable.sanat);break;
            case 6: main_kategori_adi.setText("Spor");main_motivasyon.setText("Yorucu bir kategori eminmisin Dostum");main_kategori_resmi.setImageResource(R.drawable.spor);break;

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

    public void carki_tekrar_cevir(){
        checked1.setImageResource(R.drawable.tarihim);
        checked2.setImageResource(R.drawable.bilim);
        checked3.setImageResource(R.drawable.eglence);
        checked4.setImageResource(R.drawable.cografya);
        checked5.setImageResource(R.drawable.sanat);
        checked6.setImageResource(R.drawable.spor);
        isaret_oku.setRotation(0);

    }
    public void Firebase_get_data(){
         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 Integer kalp=dataSnapshot.child("Puanlar").child(user_id).child("kalp").getValue(Integer.class);
                 main_kalp_toplam.setText(String.valueOf(kalp));


                 Integer para=  dataSnapshot.child("Puanlar").child(user_id).child("para").getValue(Integer.class);
                 main_para_toplam.setText(String.valueOf(para));

                Integer elmas=  dataSnapshot.child("Puanlar").child(user_id).child("elmas").getValue(Integer.class);
                main_elmas_toplam.setText(String.valueOf(elmas));

                //Database de değişiklik olursa ne yapayım
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
