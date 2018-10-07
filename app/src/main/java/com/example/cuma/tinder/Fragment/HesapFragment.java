package com.example.cuma.tinder.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuma.tinder.Activity.LoginActivity;
import com.example.cuma.tinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HesapFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mauthStateListener;
    private String user_id;
    private TextView kullanici_adi, email, uyelik, karakter_name;
    Button parola_degistir, hesap_cikis_yap;
    String gecerli_kullanici_mail;
    EditText yeni_parola_edit;
    Dialog dialog;
    private ImageView savasci_image, rutbem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hesap, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();
        gecerli_kullanici_mail = user.getEmail();

        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;

        savasci_image = view.findViewById(R.id.savasci_image);
        rutbem = view.findViewById(R.id.rutbe);
        kullanici_adi = (TextView) view.findViewById(R.id.kullanici_adi);
        email = (TextView) view.findViewById(R.id.e_mail);
        uyelik = (TextView) view.findViewById(R.id.uyelik);
        karakter_name = (TextView) view.findViewById(R.id.karakter_name);
        parola_degistir = (Button) view.findViewById(R.id.parola_degistir);
        hesap_cikis_yap = (Button) view.findViewById(R.id.hesap_cikis_yap);
        getir_data_firebase();
        parola_degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkConnection()) {
                    parola_degistir_dialog();
                }
                else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.internet_text),Toast.LENGTH_LONG).show();

                }

            }
        });
        hesap_cikis_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cikis_yap_sor();
            }
        });


        return view;
    }


    private void getir_data_firebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Integer savasci = dataSnapshot.child("Kullanıcı_Adı").child(user_id).child("photo").getValue(Integer.class);
                switch (savasci) {
                    case 1:
                        savasci_image.setImageResource(R.drawable.tarihadami);
                        karakter_name.setText(getResources().getString(R.string.savasci_tarih));
                        break;
                    case 2:
                        savasci_image.setImageResource(R.drawable.bilimadami);
                        karakter_name.setText(getResources().getString(R.string.savasci_bilim));
                        break;
                    case 3:
                        savasci_image.setImageResource(R.drawable.eglenceadami);
                        karakter_name.setText(getResources().getString(R.string.savasci_eglence));
                        break;
                    case 4:
                        savasci_image.setImageResource(R.drawable.cografyadami);
                        karakter_name.setText(getResources().getString(R.string.savasci_cografya));
                        break;
                    case 5:
                        savasci_image.setImageResource(R.drawable.sanatadami);
                        karakter_name.setText(getResources().getString(R.string.savasci_sanat));
                        break;
                    case 6:
                        savasci_image.setImageResource(R.drawable.sporadami);
                        karakter_name.setText(getResources().getString(R.string.savasci_spor));
                        break;
                }
                String kullanıcıad = dataSnapshot.child("Kullanıcı_Adı").child(user_id).child("nickname").getValue(String.class);
                kullanici_adi.setText(kullanıcıad.toString());
                email.setText(gecerli_kullanici_mail.toString());
                int rutbe = dataSnapshot.child("Yarisma").child(user_id).child("siralama").getValue(Integer.class);
                if (0 < rutbe && rutbe < 10) {
                    rutbem.setImageResource(R.drawable.rutbe1);
                } else if (10 < rutbe && rutbe < 20) {
                    rutbem.setImageResource(R.drawable.rutbe3);
                } else if (20 < rutbe && rutbe < 30) {
                    rutbem.setImageResource(R.drawable.rutbe4);
                } else if (30 < rutbe && rutbe < 40) {
                    rutbem.setImageResource(R.drawable.rutbe6);
                } else if (40 < rutbe && rutbe < 50) {
                    rutbem.setImageResource(R.drawable.rutbe7);
                } else if (50 < rutbe && rutbe < 60) {
                    rutbem.setImageResource(R.drawable.rutbe8);
                } else if (70 < rutbe && rutbe < 80) {
                    rutbem.setImageResource(R.drawable.rutbe9);
                } else if (80 < rutbe && rutbe < 90) {
                    rutbem.setImageResource(R.drawable.rutbe10);
                } else if (90 < rutbe && rutbe < 95) {
                    rutbem.setImageResource(R.drawable.rutbe11);
                } else if (95 < rutbe && rutbe < 100) {
                    rutbem.setImageResource(R.drawable.rutbe5);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void parola_degistir_dialog() {
        Button parola_degistir_dialog;
        dialog.setContentView(R.layout.dialog_parola_degistir);
        parola_degistir_dialog = (Button) dialog.findViewById(R.id.dialog_cikis_evet);
        yeni_parola_edit = (EditText) dialog.findViewById(R.id.yeni_parola_al);
        parola_degistir_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yeni_parola = yeni_parola_edit.getText().toString().trim();
                if (yeni_parola.matches("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.parola_belirle), Toast.LENGTH_LONG).show();
                } else {
                    user.updatePassword(yeni_parola)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.parola_tamam), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Hesap :", ":" + e.getLocalizedMessage());
                        }
                    });
                }


            }
        });
        dialog.show();

    }

    private void cikis_yap_sor() {
        Button dialog_cikis_hayir, dialog_cikis_evet;
        dialog.setContentView(R.layout.dialog_cikis);
        dialog_cikis_evet = (Button) dialog.findViewById(R.id.dialog_cikis_evet);
        dialog_cikis_hayir = (Button) dialog.findViewById(R.id.dialog_cikis_hayir);
        dialog_cikis_evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent ıntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(ıntent);
            }
        });
        dialog_cikis_hayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


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
