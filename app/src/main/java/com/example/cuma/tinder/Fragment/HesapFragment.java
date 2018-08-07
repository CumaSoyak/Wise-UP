package com.example.cuma.tinder.Fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private TextView kullanici_adi, email, kademe, uyelik;
    Button parola_degistir, hesap_cikis_yap;
    String gecerli_kullanici_mail;
    EditText yeni_parola_edit;
    Dialog dialog;


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

        kullanici_adi = (TextView) view.findViewById(R.id.kullanici_adi);
        email = (TextView) view.findViewById(R.id.e_mail);
        kademe = (TextView) view.findViewById(R.id.rutbe);
        uyelik = (TextView) view.findViewById(R.id.uyelik);
        parola_degistir = (Button) view.findViewById(R.id.parola_degistir);
        hesap_cikis_yap = (Button) view.findViewById(R.id.hesap_cikis_yap);
        getir_data_firebase();
        parola_degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parola_degistir_dialog();

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

                String kullanıcıad = dataSnapshot.child("Kullanıcı_Adı").child(user_id).child("nickname").getValue(String.class);
                Log.i("Kullanıcıad", ":" + kullanıcıad);
                kullanici_adi.setText(kullanıcıad.toString());
                email.setText(gecerli_kullanici_mail.toString());
                Integer rutbe = dataSnapshot.child("Yarisma").child(user_id).child("siralama").getValue(Integer.class);
                kademe.setText(rutbe.toString());

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
                    Toast.makeText(getActivity(), "Parola Belirleyiniz", Toast.LENGTH_LONG).show();
                } else {
                    user.updatePassword(yeni_parola)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Parolanız Değiştirildi", Toast.LENGTH_LONG).show();
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

    private void cikis_yap_sor(){
        Button dialog_cikis_hayir,dialog_cikis_evet;
        dialog.setContentView(R.layout.dialog_cikis);
        dialog_cikis_evet=(Button)dialog.findViewById(R.id.dialog_cikis_evet);
        dialog_cikis_hayir=(Button)dialog.findViewById(R.id.dialog_cikis_hayir);
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


}
