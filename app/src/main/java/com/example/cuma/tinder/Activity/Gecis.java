package com.example.cuma.tinder.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Gecis extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    private Intent ıntent;
    private ProgressBar progressBar;
    private Handler handler=new Handler();
    private int progress_deger;
    private View mProgressView;
    Button tamam;
    EditText al_kullanici_adi;
    private TextView guc_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecis);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        tamam = (Button) findViewById(R.id.gecis_tamam);
        al_kullanici_adi = (EditText) findViewById(R.id.kullanici_adi_editext);
        progressBar = (ProgressBar) findViewById(R.id.gecis_progressBar);
        guc_text=(TextView)findViewById(R.id.guc_text);
        guc_text.setVisibility(View.INVISIBLE);
        guc_text.setText("Hayatta kalabilmen için güç yükleniyor ...");
        progressBar.setVisibility(View.GONE);
        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (al_kullanici_adi.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Kullanıcı Adı Giriniz !", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    guc_text.setVisibility(View.VISIBLE);
                     ıntent = new Intent(getApplicationContext(), MainActivity.class);
                    ekle_Firebase_kullanici_adi();
                    Thread timer = new Thread() {
                        public void run() {
                            while (progress_deger<100){
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
                                    startActivity(ıntent);
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
            }
        });
    }

    private void ekle_Firebase_kullanici_adi() {
        user = firebaseAuth.getCurrentUser();
        String useremail = user.getEmail().toString();
        databaseReference.child("Kullanıcı_Adı").child(user_id).child("nickname").setValue(al_kullanici_adi.getText().toString());
        databaseReference.child("Yarisma").child(user_id).child("nickname").setValue(al_kullanici_adi.getText().toString());
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(0); //default olarak değer atadık
        databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(5);
        databaseReference.child("Puanlar").child(user_id).child("para").setValue(0);
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(0);
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(100);

    }

}
