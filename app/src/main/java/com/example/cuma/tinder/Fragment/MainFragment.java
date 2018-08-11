package com.example.cuma.tinder.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.cuma.tinder.Activity.ExamsActivity;
import com.example.cuma.tinder.Activity.Meydan_OkuActivity;
import com.example.cuma.tinder.R;
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

public class MainFragment extends Fragment implements Animation.AnimationListener {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String etkin;

    public ArrayList<String> keylistesi = new ArrayList<>();
    public ArrayList<Iterable<DataSnapshot>> randomlistesi = new ArrayList<Iterable<DataSnapshot>>();
    public Dialog dialog;

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
    public int meydanoku_random_sayi;
    public int meydanoku_random_sayi_int;

    public ArrayList<Integer> random_meydan_oku_list = new ArrayList<Integer>();
    Animation animation;
    ImageView checked, checked1, checked2, checked3, checked4, checked5, checked6, isaret_oku;
    Button oyunu_baslat;
    ImageButton meydan_oku, klasik;
    TextView main_kategori_adi, main_motivasyon;
    TextView main_kalp_toplam, main_para_toplam, main_elmas_toplam;
    ImageView main_kategori_resmi;
    ImageButton main_tekrar_buton, main_basla_buton;
    Intent key_gonder, meydan_oku_key_gonder, oda_ismi;

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

        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;

        oyunu_baslat = (Button) view.findViewById(R.id.baslat);
        meydan_oku = (ImageButton) view.findViewById(R.id.meydan_oku);
        klasik = (ImageButton) view.findViewById(R.id.klasik);
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
        oyunu_baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        return view;
    }

    public void kullanıcı_beklet_dialog() {
        dialog.setContentView(R.layout.dialog_kullanici_beklet);
        dialog.show();
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(meydan_oku_key_gonder);
                    dialog.dismiss();

                }
            }
        };
        timer.start();
    }


    private void deneme() {
        Log.i("Deneme_veri", ":" + meydanoku_random_sayi);
        meydanoku_random_sayi_int = meydanoku_random_sayi;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        oyunu_baslat.setEnabled(false);

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
        oyunu_baslat.setEnabled(true);
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
                main_kategori_adi.setText("Tarih");
                main_motivasyon.setText("Tarihte iyimisin Dostum !");
                main_kategori_resmi.setImageResource(R.drawable.tarihim);
                break;
            case 2:
                main_kategori_adi.setText("Bilim");
                main_motivasyon.setText("Seni gidi BilimAdamı");
                main_kategori_resmi.setImageResource(R.drawable.bilim);
                break;
            case 3:
                main_kategori_adi.setText("Eğlence");
                main_motivasyon.setText("Hadi biraz Eğlenelim");
                main_kategori_resmi.setImageResource(R.drawable.eglence);
                break;
            case 4:
                main_kategori_adi.setText("Coğrafya");
                main_motivasyon.setText("Dünyayı turlamaya ne dersin!");
                main_kategori_resmi.setImageResource(R.drawable.cografya);
                break;
            case 5:
                main_kategori_adi.setText("Sanat");
                main_motivasyon.setText("Sanata yeteneğin olduğunu bilmiyordum !");
                main_kategori_resmi.setImageResource(R.drawable.sanat);
                break;
            case 6:
                main_kategori_adi.setText("Spor");
                main_motivasyon.setText("Yorucu bir kategori eminmisin Dostum");
                main_kategori_resmi.setImageResource(R.drawable.spor);
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
                Integer kalp = dataSnapshot.child("Puanlar").child(user_id).child("kalp").getValue(Integer.class);
                main_kalp_toplam.setText(String.valueOf(kalp));
                if (kalp == 0) {
                    //    reklam_izle();
                }
                Integer para = dataSnapshot.child("Puanlar").child(user_id).child("para").getValue(Integer.class);
                main_para_toplam.setText(String.valueOf(para));

                Integer elmas = dataSnapshot.child("Puanlar").child(user_id).child("elmas").getValue(Integer.class);
                main_elmas_toplam.setText(String.valueOf(elmas));


                //Database de değişiklik olursa ne yapayım
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void reklam_izle() {
        Button exit_dialog, izle_button;
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.reklam_izle_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        izle_button = (Button) dialog.findViewById(R.id.reklam_izle);
        exit_dialog = (Button) dialog.findViewById(R.id.exit_reklam);
        izle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Rekla İzleyebilirsiniz", Toast.LENGTH_LONG).show();
            }
        });
        exit_dialog.setOnClickListener(new View.OnClickListener() {
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
                meydan_oku_key_gonder.putExtra("kullanici_adim",etkin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




}
